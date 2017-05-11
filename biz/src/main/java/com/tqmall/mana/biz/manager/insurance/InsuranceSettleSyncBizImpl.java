package com.tqmall.mana.biz.manager.insurance;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.insurance.domain.result.*;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;
import com.tqmall.mana.beans.VO.settle.AddMaterialAllowanceVO;
import com.tqmall.mana.beans.entity.settle.SettleShopCheckDetailDO;
import com.tqmall.mana.beans.param.settle.SettleShopCheckDetailQueryPO;
import com.tqmall.mana.biz.manager.settle.settleCalculate.InsuranceSettleCalculateBiz;
import com.tqmall.mana.biz.mq.insurance.settle.*;
import com.tqmall.mana.component.annotation.lock.ManaLock;
import com.tqmall.mana.component.annotation.lock.ManaLockKey;
import com.tqmall.mana.component.enums.insurance.dict.MaterialTypeEnum;
import com.tqmall.mana.component.enums.insurance.dict.SettleProjectEnum;
import com.tqmall.mana.component.redis.RedisKeyBean;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.dao.mapper.settle.SettleShopCheckDetailDOMapper;
import com.tqmall.mana.external.dubbo.insurance.ExtInsuranceFormService;
import com.tqmall.mana.external.dubbo.insurance.ExtInsuranceUserService;
import com.tqmall.mana.external.dubbo.insurance.ExtInsuranceVirtualFormService;
import com.tqmall.mana.external.dubbo.insurance.ExtMaterialAllowanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhangting on 17/3/15.
 */
@Slf4j
@Service
public class InsuranceSettleSyncBizImpl implements InsuranceSettleSyncBiz {
    private final Integer defaultPageSize = 20;

    @Autowired
    private ExtInsuranceFormService extInsuranceFormService;
    @Autowired
    private ExtInsuranceUserService extInsuranceUserService;
    @Autowired
    private ExtInsuranceVirtualFormService extInsuranceVirtualFormService;
    @Autowired
    private ExtMaterialAllowanceService extMaterialAllowanceService;
    @Autowired
    private InsuranceSettleCalculateBiz insuranceSettleCalculateBiz;
    @Autowired
    private SettleShopCheckDetailDOMapper shopCheckDetailDOMapper;


    @ManaLock(lockKeyName = RedisKeyBean.LOCK_SYNC_SETTLE_DATA_BY_VEHICLE_SN)
    @Override
    public void syncDataByVehicleSn(@ManaLockKey String vehicleSn) {
        Assert.hasLength(vehicleSn, "车牌号不能为空");
        //fixme 真实场景，查一次就够了，后续有问题再优化
        PageEntityDTO<InsuranceFormDTO> pageEntityDTO = extInsuranceFormService.getInsuredForms(vehicleSn, defaultPageSize, 1);
        if(pageEntityDTO==null || CollectionUtils.isEmpty(pageEntityDTO.getRecordList())){
            //在真实保单表里未查到数据，则去虚拟保单表查询
            syncVirtualFormByVehicleSn(vehicleSn);
            return;
        }
        Map<String, InsuranceSettleBasicMsg> basicMsgMap = new HashMap<>();
        List<InsuranceFormDTO> formDTOList = pageEntityDTO.getRecordList();
        for(InsuranceFormDTO formDTO : formDTOList){
            //这个接口可以获取险种信息
            InsuranceFormDTO insuranceFormDTO = extInsuranceFormService.getInsuranceFormById(formDTO.getId());
            Assert.notNull(insuranceFormDTO, "未查到保单详情，id："+formDTO.getId());

            InsuranceSettleBasicMsg basicMsg = packageBasicMsgWithForm(basicMsgMap, insuranceFormDTO);

            Integer packageId = insuranceFormDTO.getUserServicePackageId();
            if(packageId!=null){
                InsuranceUserServicePackageDTO packageDTO = extInsuranceUserService.getPackageById(packageId);
                if(packageDTO!=null){
                    //这个接口只返回商业险虚拟保单
                    InsuranceVirtualBasicDTO virtualBasicDTO = extInsuranceVirtualFormService.selectVirtualFormByUserPackageId(packageId);
                    if(virtualBasicDTO!=null){
                        packageBasicMsgWithServicePackage(basicMsg, packageDTO, virtualBasicDTO);

//                        virtualBasicDTO =  extInsuranceVirtualFormService.getVirtualInsuranceFormDetailByBasicId(virtualBasicDTO.getId());
//                        System.out.println("虚拟保单："+JsonUtil.objectToStr(virtualBasicDTO));

                    }

                    //同步机滤补贴
                    syncMaterialAllowance(packageDTO.getOrderSn(), basicMsg);
                }
            }
        }
        log.info("手动同步，保单结算数据：" + JsonUtil.objectToStr(basicMsgMap));
        for(InsuranceSettleBasicMsg basicMsg : basicMsgMap.values()){
            insuranceSettleCalculateBiz.calculateSettleData(basicMsg, false);
        }
    }

    //未投保的虚拟保单
    private void syncVirtualFormByVehicleSn(String vehicleSn){
        //fixme 真实场景，查一次就够了，后续有问题再优化
        PageEntityDTO<InsuranceVirtualFormDTO> pageEntityDTO = extInsuranceVirtualFormService.getVirtualForms(vehicleSn, defaultPageSize, 1);
        Assert.isTrue(pageEntityDTO!=null && !CollectionUtils.isEmpty(pageEntityDTO.getRecordList()),
                "未查到数据，车牌号："+vehicleSn);

        for(InsuranceVirtualFormDTO formDTO : pageEntityDTO.getRecordList()){
            Integer packageId = formDTO.getUserServicePackageId();
            if(packageId!=null){
                InsuranceUserServicePackageDTO packageDTO = extInsuranceUserService.getPackageById(packageId);
                if(packageDTO!=null){
                    InsuranceSettleBasicMsg basicMsg = packageBasicMsgWithVirtualForm(formDTO, packageDTO);
                    log.info("手动同步，虚拟保单结算数据：" + JsonUtil.objectToStr(basicMsg));
                    insuranceSettleCalculateBiz.calculateSettleData(basicMsg, false);

                    //TODO 第二次已支付 ？？？ 这种情况貌似不存在，因为在真实数据已到真实保单里面，代码走不到这


                    //同步机滤补贴
                    syncMaterialAllowance(packageDTO.getOrderSn(), basicMsg);
                }

            }
        }
    }

    //材料补贴
    private void syncMaterialAllowance(String orderSn, InsuranceSettleBasicMsg basicMsg){
        if(StringUtils.isEmpty(orderSn)){
            return;
        }
        CheckAllowanceDTO checkAllowanceDTO = extMaterialAllowanceService.checkAllowanceList(orderSn);
        if(checkAllowanceDTO==null){
            return;
        }
        PagingResult<InsuranceMaterialAllowanceDTO> pagingResult = checkAllowanceDTO.getAllowanceDTOPagingResult();
        if(pagingResult==null || CollectionUtils.isEmpty(pagingResult.getList())){
            return;
        }
        InsuranceMaterialAllowanceDTO allowanceDTO = pagingResult.getList().get(0);
        InsuranceSettleMaterialAllowanceMsg allowanceMsg = BdUtil.do2bo(basicMsg, InsuranceSettleMaterialAllowanceMsg.class);
        allowanceMsg.setMaterialOrderSn(orderSn);
        allowanceMsg.setMaterialType(allowanceDTO.getMaterialType());
        allowanceMsg.setPayableAmount(allowanceDTO.getPayableAmount());
        allowanceMsg.setAllowanceEffectTime(allowanceDTO.getGmtCreate());
        allowanceMsg.setWhsId(allowanceDTO.getWhsId());
        allowanceMsg.setWhsName(allowanceDTO.getWhsName());
        allowanceMsg.setMaterialNum(allowanceDTO.getMaterialNum());

        log.info("手动同步，材料补贴数据："+JsonUtil.objectToStr(allowanceMsg));
        insuranceSettleCalculateBiz.calculateSettleDataForMaterialAllowance(allowanceMsg);
    }


    /** ========== 组装msg ========== */
    //通过真实保单组装msg
    private InsuranceSettleBasicMsg packageBasicMsgWithForm(Map<String, InsuranceSettleBasicMsg> basicMsgMap,
                                                            InsuranceFormDTO formDTO){
        String insuranceOrderSn = formDTO.getBasicDTO().getInsuranceOrderSn();
        InsuranceSettleBasicMsg basicMsg = basicMsgMap.get(insuranceOrderSn);
        if(basicMsg==null){
            basicMsg = getBasicMsgByForm(formDTO);
            basicMsgMap.put(insuranceOrderSn, basicMsg);
        }
        basicMsg.getFormMsgList().add(getFormMsg(formDTO));
        return basicMsg;
    }
    private InsuranceSettleBasicMsg getBasicMsgByForm(InsuranceFormDTO formDTO){
        InsuranceBasicDTO basicDTO = formDTO.getBasicDTO();
        InsuranceSettleBasicMsg basicMsg = BdUtil.do2bo(basicDTO, InsuranceSettleBasicMsg.class);
        basicMsg.setCooperationModeId(formDTO.getCooperationMode());
        basicMsg.setBeAppliedName(basicDTO.getInsuredName());
        basicMsg.setCarOwnerName(basicDTO.getVehicleOwnerName());
        basicMsg.setFormMsgList(new ArrayList<InsuranceSettleFormMsg>());
        return basicMsg;
    }
    private InsuranceSettleFormMsg getFormMsg(InsuranceFormDTO formDTO){
        InsuranceSettleFormMsg formMsg = new InsuranceSettleFormMsg();
        formMsg.setId(formDTO.getId());
        formMsg.setInsuredApplyNo(formDTO.getOuterInsuranceApplyNo());
        formMsg.setInsuredFormNo(formDTO.getOuterInsuranceFormNo());
        formMsg.setInsuranceTypeId(formDTO.getInsuranceType());
        formMsg.setCooperationModeId(formDTO.getCooperationMode());
        formMsg.setBillSignTime(formDTO.getPayTime());
        formMsg.setInsuredStartTime(formDTO.getPackageStartTime());
        formMsg.setInsuredFee(formDTO.getInsuredFee());
        formMsg.setTax(formDTO.getInsuredTax());
        formMsg.setItemMsgList(getItemMsgList(formDTO.getItemDTOList()));
        return formMsg;
    }
    private List<InsuranceSettleItemMsg> getItemMsgList(List<InsuranceItemDTO> itemDTOList){
        return BdUtil.do2bo4List(itemDTOList, InsuranceSettleItemMsg.class);
    }

    //组装服务包信息
    private void packageBasicMsgWithServicePackage(InsuranceSettleBasicMsg basicMsg,
                                                   InsuranceUserServicePackageDTO packageDTO,
                                                   InsuranceVirtualBasicDTO virtualBasicDTO){
        //目前这么处理即可，因为上个接口只会返回商业险虚拟保单
        List<InsuranceVirtualFormDTO> virtualFormDTOList = virtualBasicDTO.getInsuranceVirtualFormDTOList();
        InsuranceSettlePackageMsg packageMsg = getPackageMsg(packageDTO, virtualFormDTOList.get(0));
        basicMsg.setPackageMsg(packageMsg);
    }
    private InsuranceSettlePackageMsg getPackageMsg(InsuranceUserServicePackageDTO packageDTO,
                                                    InsuranceVirtualFormDTO virtualFormDTO){
        InsuranceSettlePackageMsg packageMsg = BdUtil.do2bo(virtualFormDTO, InsuranceSettlePackageMsg.class);
        packageMsg.setPackageOrderSn(packageDTO.getOrderSn());
        packageMsg.setPackageName(packageDTO.getPackageName());
        packageMsg.setPackagePrice(packageDTO.getMarketPrice());
        packageMsg.setPackageFee(packageDTO.getRewardAmount());
        return packageMsg;
    }

    //通过虚拟保单组装msg
    private InsuranceSettleBasicMsg packageBasicMsgWithVirtualForm(InsuranceVirtualFormDTO formDTO,
                                                                   InsuranceUserServicePackageDTO packageDTO){
        InsuranceSettleBasicMsg basicMsg = getBasicMsgByVirtualForm(formDTO);
        InsuranceSettlePackageMsg packageMsg = getPackageMsg(packageDTO, formDTO);
        basicMsg.setPackageMsg(packageMsg);
        return basicMsg;
    }
    private InsuranceSettleBasicMsg getBasicMsgByVirtualForm(InsuranceVirtualFormDTO formDTO){
        InsuranceVirtualBasicDTO basicDTO = formDTO.getBasicVirtualDTO();
        InsuranceSettleBasicMsg basicMsg = BdUtil.do2bo(basicDTO, InsuranceSettleBasicMsg.class);
        basicMsg.setCooperationModeId(formDTO.getCooperationMode());
        basicMsg.setBeAppliedName(basicDTO.getInsuredName());
        basicMsg.setCarOwnerName(basicDTO.getVehicleOwnerName());
        return basicMsg;
    }


    @Override
    public void addMaterialAllowance(AddMaterialAllowanceVO allowanceVO) {
        Assert.notNull(allowanceVO, "参数不能为空");
        Assert.hasLength(allowanceVO.getInsuranceOrderSn(), "淘汽保单号不能为空");
        Assert.hasLength(allowanceVO.getMaterialOrderSn(), "物料订单号不能为空");
        BigDecimal payableAmount = allowanceVO.getPayableAmount();
        Assert.isTrue(payableAmount!=null && payableAmount.compareTo(BigDecimal.ZERO)>0, "请填写正确的应补贴金额");
        Integer materialNum = allowanceVO.getMaterialNum();
        Assert.isTrue(materialNum!=null && materialNum>0, "请填写正确的物料数量");
        Integer whsId = allowanceVO.getWhsId();
        Assert.isTrue(whsId!=null && whsId>0, "请填写正确的仓库id");
        Assert.hasLength(allowanceVO.getWhsName(), "订单仓库名称不能为空");
        Assert.notNull(allowanceVO.getAllowanceEffectTime(), "补贴生效时间不能为空");

        //检查机滤补贴
        SettleShopCheckDetailQueryPO queryPO = new SettleShopCheckDetailQueryPO();
        List<String> insuranceOrderSnList = new ArrayList<>();
        insuranceOrderSnList.add(allowanceVO.getInsuranceOrderSn());
        queryPO.setInsuranceOrderSnList(insuranceOrderSnList);
        queryPO.setSettleProjectId(SettleProjectEnum.OIL_FILTER_REBATE.getCode());
        List<SettleShopCheckDetailDO> detailDOList = shopCheckDetailDOMapper.selectByCondition(queryPO);
        Assert.isTrue(detailDOList.isEmpty(), "机滤补贴数据已存在");

        //查询服务包返利
        queryPO.setSettleProjectId(SettleProjectEnum.PACKAGE_REBATE.getCode());
        detailDOList = shopCheckDetailDOMapper.selectByCondition(queryPO);
        Assert.notEmpty(detailDOList, "未查到服务包返利数据");

        SettleShopCheckDetailDO checkDetailDO = detailDOList.get(0);
        InsuranceSettleMaterialAllowanceMsg materialAllowanceMsg = BdUtil.do2bo(allowanceVO, InsuranceSettleMaterialAllowanceMsg.class);
        materialAllowanceMsg.setCooperationModeId(checkDetailDO.getCooperationModeId());
        materialAllowanceMsg.setCarOwnerName(checkDetailDO.getCarOwnerName());
        materialAllowanceMsg.setVehicleSn(checkDetailDO.getVehicleSn());
        materialAllowanceMsg.setAgentId(checkDetailDO.getAgentId());
        materialAllowanceMsg.setAgentName(checkDetailDO.getAgentName());
        materialAllowanceMsg.setInsuranceCompanyId(checkDetailDO.getInsuranceCompanyId());
        materialAllowanceMsg.setMaterialType(MaterialTypeEnum.OIL_FILTER.getCode());

        insuranceSettleCalculateBiz.calculateSettleDataForMaterialAllowance(materialAllowanceMsg);
    }

}
