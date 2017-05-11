package com.tqmall.mana.biz.manager.customer;

import com.tqmall.mana.beans.BO.shop.SimpleShopBO;
import com.tqmall.mana.beans.entity.customer.ManaCustomerDO;
import com.tqmall.mana.beans.entity.customer.ManaCustomerInfoReplaceLogDO;
import com.tqmall.mana.beans.entity.customer.ManaCustomerVehicleDO;
import com.tqmall.mana.beans.entity.insurance.ManaInsuranceFormSyncLogDO;
import com.tqmall.mana.biz.mq.insurance.InsuranceBasicMsg;
import com.tqmall.mana.biz.mq.insurance.InsuranceFormMsg;
import com.tqmall.mana.component.enums.CustomerSourceEnum;
import com.tqmall.mana.component.enums.InsureIntentionEnum;
import com.tqmall.mana.component.enums.InsureStatusEnum;
import com.tqmall.mana.component.enums.QuitInsureStatusEnum;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.dao.mapper.customer.ManaCustomerDOMapper;
import com.tqmall.mana.dao.mapper.customer.ManaCustomerInfoReplaceLogDOMapper;
import com.tqmall.mana.dao.mapper.customer.ManaCustomerVehicleDOMapper;
import com.tqmall.mana.dao.mapper.insurance.ManaInsuranceFormSyncLogDOMapper;
import com.tqmall.mana.external.dubbo.uc.ExtShopInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huangzhangting on 16/12/21.
 */
@Slf4j
@Service
public class CustomerSyncBiz {
    @Autowired
    private ManaCustomerDOMapper customerDOMapper;
    @Autowired
    private ManaCustomerVehicleDOMapper vehicleDOMapper;
    @Autowired
    private ManaInsuranceFormSyncLogDOMapper formSyncLogDOMapper;
    @Autowired
    private ManaCustomerInfoReplaceLogDOMapper replaceLogDOMapper;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private ExtShopInfoService extShopInfoService;


    public void syncCustomerInfo(InsuranceBasicMsg basicMsg) {
        if (basicMsg == null) {
            log.info("not syncCustomerInfo, param is null");
            return;
        }
        List<ManaInsuranceFormSyncLogDO> syncLogDOList = packageFormSyncLogDOList(basicMsg);
        ManaInsuranceFormSyncLogDO syncLogDO = syncLogDOList.get(0);

        Integer vehicleId = syncCustomer(syncLogDO);

        addInsuranceFormSyncLog(syncLogDOList, vehicleId);
    }

    /**
     * 同步客户信息
     */
    private Integer syncCustomer(ManaInsuranceFormSyncLogDO syncLogDO) {
        ManaCustomerDO checkCustomer = new ManaCustomerDO();
        checkCustomer.setCustomerMobile(syncLogDO.getVehicleOwnerPhone());
        List<ManaCustomerDO> customerDOList = customerDOMapper.selectListByDO(checkCustomer);

        ManaCustomerDO customerDO;
        ManaCustomerDO oldCustomerDO = null;
        if (customerDOList.isEmpty()) {
            customerDO = new ManaCustomerDO();
            customerDO.setGmtCreate(new Date());
            customerDO.setCreator("system");
            customerDO.setCustomerSource(CustomerSourceEnum.SHOP.getCode());
            customerDO.setCustomerMobile(syncLogDO.getVehicleOwnerPhone());

            packageCustomer(customerDO, syncLogDO);

            customerDOMapper.insertSelective(customerDO);

        } else {
            customerDO = customerDOList.get(0);
            oldCustomerDO = BdUtil.do2bo(customerDO, ManaCustomerDO.class); //拷贝一份老数据，用户记录客户信息替换历史

            customerDO.setGmtModified(new Date());
            customerDO.setModifier("system");

            packageCustomer(customerDO, syncLogDO);

            customerDOMapper.updateByPrimaryKeySelective(customerDO);
        }

        return syncCustomerVehicle(customerDO.getId(), syncLogDO, oldCustomerDO);
    }

    private void packageCustomer(ManaCustomerDO customerDO, ManaInsuranceFormSyncLogDO syncLogDO) {
        customerDO.setCustomerName(syncLogDO.getVehicleOwnerName());
        customerDO.setCertificateType(syncLogDO.getVehicleOwnerCertType());
        customerDO.setCertificateNo(syncLogDO.getVehicleOwnerCertCode());
        customerDO.setHasSync(1);

    }

    /**
     * 同步车辆信息
     */
    private Integer syncCustomerVehicle(Integer customerId, ManaInsuranceFormSyncLogDO syncLogDO, ManaCustomerDO oldCustomerDO) {
        ManaCustomerVehicleDO checkVehicle = new ManaCustomerVehicleDO();
        checkVehicle.setCustomerId(customerId);
        checkVehicle.setLicencePlate(syncLogDO.getLicencePlate());
        List<ManaCustomerVehicleDO> vehicleDOList = vehicleDOMapper.selectListByDO(checkVehicle);

        ManaCustomerVehicleDO vehicleDO;
        if (vehicleDOList.isEmpty()) {
            vehicleDO = new ManaCustomerVehicleDO();
            vehicleDO.setGmtCreate(new Date());
            vehicleDO.setCreator("system");
            vehicleDO.setCustomerId(customerId);
            vehicleDO.setLicencePlate(syncLogDO.getLicencePlate());

            packageVehicleDO(vehicleDO, syncLogDO);

            vehicleDOMapper.insertSelective(vehicleDO);

            //记录第一次同步前的数据
            addCustomerInfoReplaceLog(oldCustomerDO, null);

        } else {
            vehicleDO = vehicleDOList.get(0);

            //记录第一次同步前的数据
            if (vehicleDO.getHasSync() == 0) {
                addCustomerInfoReplaceLog(oldCustomerDO, BdUtil.do2bo(vehicleDO, ManaCustomerVehicleDO.class));
            }

            vehicleDO.setGmtModified(new Date());
            vehicleDO.setModifier("system");

            packageVehicleDO(vehicleDO, syncLogDO);

            vehicleDOMapper.updateByPrimaryKeySelective(vehicleDO);
        }
        return vehicleDO.getId();
    }

    private void packageVehicleDO(ManaCustomerVehicleDO vehicleDO, ManaInsuranceFormSyncLogDO syncLogDO) {
        vehicleDO.setHasLicencePlate(syncLogDO.getHasLicencePlate());
        vehicleDO.setInsureProvince(syncLogDO.getInsuredProvince());
        vehicleDO.setInsureProvinceCode(syncLogDO.getInsuredProvinceCode());
        vehicleDO.setInsureCity(syncLogDO.getInsuredCity());
        vehicleDO.setInsureCityCode(syncLogDO.getInsuredCityCode());
        vehicleDO.setInsureVehicleType(syncLogDO.getConfigType());
        vehicleDO.setEngineNo(syncLogDO.getEngineNo());
        vehicleDO.setVinNo(syncLogDO.getVinNo());
        vehicleDO.setVehicleRegDate(syncLogDO.getVehicleRegDate());
        vehicleDO.setCooperationMode(syncLogDO.getCooperationMode());
        vehicleDO.setAgentType(syncLogDO.getAgentType());
        vehicleDO.setAgentId(syncLogDO.getAgentId());
        vehicleDO.setAgentName(syncLogDO.getAgentName());
        vehicleDO.setHasSync(1);

        Integer isVirtual = syncLogDO.getIsVirtual();
        if (isVirtual != null && isVirtual == 1) { //如果是虚拟保单，只是购买了服务包

        } else {
            vehicleDO.setInsuranceFormId(syncLogDO.getInsuranceFormId());
            vehicleDO.setInsureStatus(InsureStatusEnum.HAS_INSURED.getCode()); //车险状态
            vehicleDO.setInsureIntention(InsureIntentionEnum.HAS_INSURED.getCode()); //投保意向
            vehicleDO.setQuitInsureStatus(QuitInsureStatusEnum.HAS_INSURED.getCode()); //在淘汽投保状态
            vehicleDO.setInsureStartDate(syncLogDO.getInsureStartTime());
            vehicleDO.setInsureEndDate(syncLogDO.getInsureEndTime());
            vehicleDO.setInsureCompanyId(syncLogDO.getInsureCompanyId());
        }
    }


    /**
     * 组装表单同步信息对象集合
     */
    private List<ManaInsuranceFormSyncLogDO> packageFormSyncLogDOList(InsuranceBasicMsg basicMsg) {
        List<ManaInsuranceFormSyncLogDO> list = new ArrayList<>();
        List<InsuranceFormMsg> formMsgList = basicMsg.getFormMsgList();

        if (CollectionUtils.isEmpty(formMsgList)) {
            list.add(packageFormSyncLogDO(basicMsg));
        } else {
            for (InsuranceFormMsg formMsg : formMsgList) {
                ManaInsuranceFormSyncLogDO syncLogDO = packageFormSyncLogDO(basicMsg);
                syncLogDO.setInsuranceFormId(formMsg.getId());
                syncLogDO.setGmtCreate(formMsg.getGmtCreate());
                syncLogDO.setInsureStartTime(formMsg.getPackageStartTime());
                syncLogDO.setInsureEndTime(formMsg.getPackageEndTime());
                syncLogDO.setCooperationMode(formMsg.getCooperationMode());
                syncLogDO.setVirtualInsuranceFormId(formMsg.getVirtualFormId());

                list.add(syncLogDO);
            }

            //一次创建了两张表单，把id大的排第一个，然后取最新的那张，更新客户、车辆信息
            if (list.size() > 1) {
                Collections.sort(list, new Comparator<ManaInsuranceFormSyncLogDO>() {
                    @Override
                    public int compare(ManaInsuranceFormSyncLogDO o1, ManaInsuranceFormSyncLogDO o2) {
                        return o2.getInsuranceFormId().compareTo(o1.getInsuranceFormId());
                    }
                });
            }
        }

        return list;
    }

    private ManaInsuranceFormSyncLogDO packageFormSyncLogDO(InsuranceBasicMsg basicMsg) {
        ManaInsuranceFormSyncLogDO syncLogDO = new ManaInsuranceFormSyncLogDO();
        syncLogDO.setInsuranceBasicId(basicMsg.getId());
        syncLogDO.setInsureCompanyId(basicMsg.getInsuranceCompanyId());
        syncLogDO.setAgentType(basicMsg.getAgentType());
        syncLogDO.setAgentId(basicMsg.getAgentId());

        //设置门店名称，从uc取门店名称，不存储insurance那边直接同步过来的，因为那是legend_shop的name
        SimpleShopBO shopBO = extShopInfoService.getSimpleShopInfo(basicMsg.getAgentId());
        if(shopBO!=null){
            syncLogDO.setAgentName(shopBO.getCompanyName());
        }else {
            syncLogDO.setAgentName(basicMsg.getAgentName());
        }

        syncLogDO.setVehicleOwnerName(basicMsg.getVehicleOwnerName());
        syncLogDO.setVehicleOwnerCertType(basicMsg.getVehicleOwnerCertType());
        syncLogDO.setVehicleOwnerCertCode(basicMsg.getVehicleOwnerCertCode());
        syncLogDO.setVehicleOwnerPhone(basicMsg.getVehicleOwnerPhone());
        syncLogDO.setLicencePlate(basicMsg.getVehicleSn());
        syncLogDO.setHasLicencePlate(basicMsg.getHasLicense());
        syncLogDO.setConfigType(basicMsg.getCarConfigType());
        syncLogDO.setEngineNo(basicMsg.getCarEngineSn());
        syncLogDO.setVinNo(basicMsg.getCarFrameSn());
        syncLogDO.setVehicleRegDate(basicMsg.getCarEgisterDate());
        syncLogDO.setInsuredProvinceCode(basicMsg.getInsuredProvinceCode());
        syncLogDO.setInsuredProvince(basicMsg.getInsuredProvince());
        syncLogDO.setInsuredCityCode(basicMsg.getInsuredCityCode());
        syncLogDO.setInsuredCity(basicMsg.getInsuredCity());
        syncLogDO.setIsVirtual(basicMsg.getIsVirtual());

        return syncLogDO;
    }


    /**
     * 记录表单同步日志（创建线程去做）
     */
    private void addInsuranceFormSyncLog(final List<ManaInsuranceFormSyncLogDO> formSyncLogDOList, final Integer vehicleId) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                for (ManaInsuranceFormSyncLogDO syncLogDO : formSyncLogDOList) {
                    syncLogDO.setGmtCreate(new Date());
                    syncLogDO.setCreator("system");
                    syncLogDO.setCustomerVehicleId(vehicleId);

                    try {
                        formSyncLogDOMapper.insertSelective(syncLogDO);
                    } catch (Exception e) {
                        log.error("addInsuranceFormSyncLog error", e);
                    }
                }
            }
        });
    }


    private static final Map<String, Date> map = new ConcurrentHashMap<>();

    private String getKey(ManaCustomerDO customerDO, ManaCustomerVehicleDO vehicleDO) {
        String key = customerDO.getId() + "_";
        if (vehicleDO != null) {
            return key + vehicleDO.getId();
        }
        return key;
    }

    private void clearMap() {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<String, Date> entry : map.entrySet()) {
                    /** 清理超过10分钟的数据 */
                    long num = (System.currentTimeMillis() - entry.getValue().getTime()) / 60000;
                    if (num > 10) {
                        map.remove(entry.getKey());
                    }
                }
            }
        });
    }

    /**
     * 记录客户信息替换历史
     */
    private void addCustomerInfoReplaceLog(final ManaCustomerDO customerDO, final ManaCustomerVehicleDO vehicleDO) {
        if (customerDO == null) {
            return;
        }

        //每次记录前，先清理下map，不然时间长了，就oom了
        clearMap();

        //处理短时间内，重复数据问题，理论上不会出现，自己测试的时候遇到了
        final String key = getKey(customerDO, vehicleDO);
        log.info("addCustomerInfoReplaceLog key:{}", key);
        if (map.get(key) != null) {
            log.info("数据已经存在, 不需要插入数据, key:{}", key);
            return;
        }
        map.put(key, new Date());


        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                log.info("addCustomerInfoReplaceLog customerDO:{}", JsonUtil.objectToStr(customerDO));
                ManaCustomerInfoReplaceLogDO replaceLogDO;
                if (vehicleDO == null) {
                    replaceLogDO = BdUtil.do2bo(customerDO, ManaCustomerInfoReplaceLogDO.class);
                } else {
                    log.info("addCustomerInfoReplaceLog vehicleDO:{}", JsonUtil.objectToStr(vehicleDO));
                    replaceLogDO = BdUtil.do2bo(vehicleDO, ManaCustomerInfoReplaceLogDO.class);

                    replaceLogDO.setCustomerMobile(customerDO.getCustomerMobile());
                    replaceLogDO.setCustomerName(customerDO.getCustomerName());
                    replaceLogDO.setCustomerSource(customerDO.getCustomerSource());
                    replaceLogDO.setCertificateType(customerDO.getCertificateType());
                    replaceLogDO.setCertificateNo(customerDO.getCertificateNo());
                }
                replaceLogDO.setId(null);
                replaceLogDO.setModifier(null);
                replaceLogDO.setGmtModified(null);
                replaceLogDO.setGmtCreate(new Date());
                replaceLogDO.setCreator("system");

                replaceLogDOMapper.insertSelective(replaceLogDO);

            }
        });
    }

}
