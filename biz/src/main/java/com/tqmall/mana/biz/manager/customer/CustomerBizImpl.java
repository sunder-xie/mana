package com.tqmall.mana.biz.manager.customer;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.customer.*;
import com.tqmall.mana.beans.BO.shop.SimpleShopBO;
import com.tqmall.mana.beans.VO.CustomerPreferentialVO;
import com.tqmall.mana.beans.VO.InsuranceInfoVO;
import com.tqmall.mana.beans.entity.customer.*;
import com.tqmall.mana.beans.param.CommonPageParam;
import com.tqmall.mana.beans.param.CommonVOPageParam;
import com.tqmall.mana.beans.param.PreferentialVOPageParam;
import com.tqmall.mana.biz.manager.insurance.InsuranceBiz;
import com.tqmall.mana.biz.manager.insurance.InsuranceDicBiz;
import com.tqmall.mana.biz.manager.shiro.ShiroBiz;
import com.tqmall.mana.component.bean.DataError;
import com.tqmall.mana.component.exception.BusinessException;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.component.util.mana.ManaUtil;
import com.tqmall.mana.dao.mapper.customer.ManaCustomerAddressDOMapper;
import com.tqmall.mana.dao.mapper.customer.ManaCustomerCommunicateRecordDOMapper;
import com.tqmall.mana.dao.mapper.customer.ManaCustomerDOMapper;
import com.tqmall.mana.dao.mapper.customer.ManaCustomerVehicleDOMapper;
import com.tqmall.mana.external.dubbo.insurance.ExtInsuranceCompanyService;
import com.tqmall.mana.external.dubbo.stall.ExtRegionService;
import com.tqmall.mana.external.dubbo.uc.ExtShopInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhangting on 16/12/14.
 */
@Slf4j
@Service
public class CustomerBizImpl implements CustomerBiz {
    @Autowired
    private ShiroBiz shiroBiz;
    @Autowired
    private ExtRegionService extRegionService;
    @Autowired
    private ManaCustomerDOMapper customerDOMapper;
    @Autowired
    private ManaCustomerAddressDOMapper addressDOMapper;
    @Autowired
    private ManaCustomerVehicleDOMapper customerVehicleDOMapper;
    @Autowired
    private ManaCustomerCommunicateRecordDOMapper communicateRecordDOMapper;
    @Autowired
    private InsuranceBiz insuranceBiz;
    @Autowired
    private ExtInsuranceCompanyService extInsuranceCompanyService;
    @Autowired
    private CommunicateRecordBiz communicateRecordBiz;
    @Autowired
    private ExtShopInfoService extShopInfoService;
    @Autowired
    private PreferentialLogBiz preferentialLogBiz;
    @Autowired
    private InsuranceDicBiz insuranceDicBiz;


    private String checkAddCustomer(AddCustomerBO addCustomerBO) {
        if (addCustomerBO == null) {
            return "参数不能为空";
        }
        if (!ManaUtil.isMobile(addCustomerBO.getCustomerMobile())) {
            return "请输入正确的手机号";
        }
        if (StringUtils.isEmpty(addCustomerBO.getCustomerName())) {
            return "请输入车主姓名";
        }
        return null;
    }

    //设置客户属性
    private void setCustomerField(ManaCustomerDO customerDO, AddCustomerBO addCustomerBO) {
        customerDO.setCustomerName(addCustomerBO.getCustomerName());
        customerDO.setCustomerSource(addCustomerBO.getCustomerSource());
        customerDO.setCertificateType(addCustomerBO.getCertificateType());
        customerDO.setCertificateNo(addCustomerBO.getCertificateNo());
        customerDO.setGmtCreate(new Date());
        customerDO.setCreator(shiroBiz.getCurrentUserRealName());
    }

    // 组装地址对象
    private ManaCustomerAddressDO packageAddressDO(Integer customerId, AddCustomerBO addCustomerBO) {
        ManaCustomerAddressDO addressDO = BdUtil.do2bo(addCustomerBO, ManaCustomerAddressDO.class);
        if (addressDO == null) {
            throw new BusinessException("系统发生内部错误");
        }
        Integer addressId = addCustomerBO.getAddressId();
        if (addressId == null) {
            if (addressDO.isEmpty()) {
                return null;
            }
            addressDO.setGmtCreate(new Date());
            addressDO.setCreator(shiroBiz.getCurrentUserRealName());
        } else {
            addressDO.setGmtModified(new Date());
            addressDO.setModifier(shiroBiz.getCurrentUserRealName());
            addressDO.setId(addressId);
            addressDO.setDefaultProperty(); //允许清空地址属性
        }
        addressDO.setCustomerId(customerId);

        return addressDO;
    }

    // 组装客户车辆对象
    private ManaCustomerVehicleDO packageCustomerVehicleDO(Integer customerId, AddCustomerBO addCustomerBO) {
        ManaCustomerVehicleDO vehicleDO = BdUtil.do2bo(addCustomerBO, ManaCustomerVehicleDO.class);
        if (vehicleDO == null) {
            throw new BusinessException("系统发生内部错误");
        }
        //默认创建一辆空车
        vehicleDO.setCustomerId(customerId);
        vehicleDO.setGmtCreate(new Date());
        vehicleDO.setCreator(shiroBiz.getCurrentUserRealName());
        return vehicleDO;
    }

    // 组装沟通信息对象
    private ManaCustomerCommunicateRecordDO packageCommunicateRecordDO(Integer customerVehicleId, AddCommunicateRecordBO communicateRecordBO) {
        ManaCustomerCommunicateRecordDO communicateRecordDO = BdUtil.do2bo(communicateRecordBO, ManaCustomerCommunicateRecordDO.class);
        if (communicateRecordDO == null) {
            throw new BusinessException("系统发生内部错误");
        }
        if (communicateRecordDO.isEmpty()) {
            return null;
        }
        communicateRecordDO.setCustomerVehicleId(customerVehicleId);
        communicateRecordDO.setGmtCreate(new Date());
        communicateRecordDO.setCreator(shiroBiz.getCurrentUserRealName());
        return communicateRecordDO;
    }

    @Transactional
    @Override
    public Result addCustomer(AddCustomerBO addCustomerBO) {
        String checkStr = checkAddCustomer(addCustomerBO);
        if (checkStr != null) {
            return ResultUtil.errorResult("", checkStr);
        }

        ManaCustomerDO customerDO = new ManaCustomerDO();
        customerDO.setCustomerMobile(addCustomerBO.getCustomerMobile());
        List<ManaCustomerDO> customerDOList = customerDOMapper.selectListByDO(customerDO);
        if (!customerDOList.isEmpty()) {
            return ResultUtil.errorResult("", "该手机号已经存在，不能重复添加");
        }

        /** 新增客户 */
        setCustomerField(customerDO, addCustomerBO);
        customerDOMapper.insertSelective(customerDO);
        Integer customerId = customerDO.getId();
        if (customerId == null) {
            return ResultUtil.errorResult("", "新增客户失败，请重新添加");
        }

        /** 新增地址 */
        ManaCustomerAddressDO addressDO = packageAddressDO(customerId, addCustomerBO);
        if (addressDO != null) {
            addressDOMapper.insertSelective(addressDO);
        }

        /** 新增车辆（默认创建一辆空车） */
        ManaCustomerVehicleDO customerVehicleDO = packageCustomerVehicleDO(customerId, addCustomerBO);
        customerVehicleDOMapper.insertSelective(customerVehicleDO);
        Integer customerVehicleId = customerVehicleDO.getId();
        if (customerVehicleId == null) {
            throw new BusinessException("新增车辆信息失败，请重新添加");
        }

        /** 新增沟通记录 */
        ManaCustomerCommunicateRecordDO communicateRecordDO = packageCommunicateRecordDO(customerVehicleId, addCustomerBO);
        //没有沟通信息，则直接返回
        if (communicateRecordDO == null) {
            return ResultUtil.successResult(customerId);
        }
        communicateRecordDOMapper.insertSelective(communicateRecordDO);

        return ResultUtil.successResult(customerId);
    }


    /**
     * 修改车辆信息
     */
    private void updateVehicleInfo(UpdateCustomerInfoBO updateInfoBO, ManaCustomerVehicleDO oldVehicle) {
        String licencePlate = updateInfoBO.getLicencePlate();
        if (licencePlate == null) { //允许用户有一辆没有车牌的车
            licencePlate = "";
        }

        if (licencePlate.equals(oldVehicle.getLicencePlate())) { //车牌不变

        } else {
            ManaCustomerVehicleDO check = new ManaCustomerVehicleDO();
            check.setCustomerId(updateInfoBO.getId());
            check.setLicencePlate(licencePlate);
            List<ManaCustomerVehicleDO> checkVehicleList = customerVehicleDOMapper.selectListByDO(check);
            if (!checkVehicleList.isEmpty()) {
                throw new BusinessException("该车主的该车牌已经存在，无法重复添加");
            }

        }
        ManaCustomerVehicleDO vehicleDO = BdUtil.do2bo(updateInfoBO, ManaCustomerVehicleDO.class);
        vehicleDO.setId(updateInfoBO.getVehicleId());
        vehicleDO.setModifier(shiroBiz.getCurrentUserRealName());
        vehicleDO.setGmtModified(new Date());

        customerVehicleDOMapper.updateByPrimaryKeySelective(vehicleDO);
    }

    /**
     * 新增或修改地址信息
     */
    private void updateAddressInfo(UpdateCustomerInfoBO updateInfoBO) {
        ManaCustomerAddressDO addressDO = BdUtil.do2bo(updateInfoBO, ManaCustomerAddressDO.class);
        Integer addressId = updateInfoBO.getAddressId();
        if (addressId == null) {
            if (addressDO.isEmpty()) { //如果没有属性，则不添加
                return;
            }
            addressDO.setId(null);
            addressDO.setCreator(shiroBiz.getCurrentUserRealName());
            addressDO.setGmtCreate(new Date());
            addressDO.setCustomerId(updateInfoBO.getId());
            addressDOMapper.insertSelective(addressDO);
        } else {
            addressDO.setId(addressId);
            addressDO.setModifier(shiroBiz.getCurrentUserRealName());
            addressDO.setGmtModified(new Date());
            addressDO.setDefaultProperty(); //支持清空地址
            addressDOMapper.updateByPrimaryKeySelective(addressDO);
        }
    }

    /**
     * 修改客户信息
     */
    private void updateCustomerInfo(UpdateCustomerInfoBO updateInfoBO, ManaCustomerDO oldCustomer) {
        String mobile = updateInfoBO.getCustomerMobile();
        if (oldCustomer.getCustomerMobile().equals(mobile)) { //手机号没有变

        } else {
            ManaCustomerDO check = new ManaCustomerDO();
            check.setCustomerMobile(mobile);
            List<ManaCustomerDO> checkList = customerDOMapper.selectListByDO(check);
            if (!checkList.isEmpty()) {
                throw new BusinessException("该手机号已存在，无法重复添加");
            }
        }
        ManaCustomerDO customerDO = BdUtil.do2bo(updateInfoBO, ManaCustomerDO.class);
        customerDO.setModifier(shiroBiz.getCurrentUserRealName());
        customerDO.setGmtModified(new Date());

        customerDOMapper.updateByPrimaryKeySelective(customerDO);
    }

    @Transactional
    @Override
    public Result updateCustomerAndVehicle(UpdateCustomerInfoBO updateCustomerInfoBO) {
        if (updateCustomerInfoBO == null) {
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        ManaCustomerDO manaCustomerDO = customerDOMapper.selectByPrimaryKey(updateCustomerInfoBO.getId());
        if (manaCustomerDO == null) {
            return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
        }
        ManaCustomerVehicleDO vehicleDO = customerVehicleDOMapper.selectByPrimaryKey(updateCustomerInfoBO.getVehicleId());
        if (vehicleDO == null) {
            return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
        }

        if (manaCustomerDO.getHasSync() == 1) {
            if (vehicleDO.getHasSync() == 1) {
                return ResultUtil.errorResult("", "数据已同步，不能修改");
            }
        } else {
            /** 修改车主 */
            updateCustomerInfo(updateCustomerInfoBO, manaCustomerDO);
        }

        /** 修改车辆 */
        updateVehicleInfo(updateCustomerInfoBO, vehicleDO);

        /** 新增或者修改地址 */
        updateAddressInfo(updateCustomerInfoBO);

        return ResultUtil.successResult(1);

/*
        if (manaCustomerDO != null) {
            Integer sync = manaCustomerDO.getHasSync();
            if (sync == 0) {//未同步可以修改

                //通过手机号查询该客户是否存在
                Integer customerId = updateCustomerInfoBO.getId();
                String customerName = updateCustomerInfoBO.getCustomerName();
                String customerMobile = updateCustomerInfoBO.getCustomerMobile();
                if (StringUtils.isEmpty(customerName)) {
                    return ResultUtil.errorResult("", "请输入车主姓名");
                }
                if (!ManaUtil.isMobile(customerMobile)) {
                    return ResultUtil.errorResult("", "请输入正确的手机号");
                }
                if (!manaCustomerDO.getCustomerMobile().equals(customerMobile)) {
                    ManaCustomerDO record = new ManaCustomerDO();
                    record.setCustomerMobile(customerMobile);
                    List<ManaCustomerDO> list = customerDOMapper.selectListByDO(record);
                    if (!CollectionUtils.isEmpty(list)) {
                        return ResultUtil.errorResult("", "该手机号已存在");
                    }
                }
                //设置客户信息
                ManaCustomerDO customerDO = BdUtil.do2bo(updateCustomerInfoBO, ManaCustomerDO.class);
                Integer vehicleId = updateCustomerInfoBO.getVehicleId();
                //设置车辆信息
                ManaCustomerVehicleDO customerVehicleDO = BdUtil.do2bo(updateCustomerInfoBO, ManaCustomerVehicleDO.class);
                customerVehicleDO.setId(vehicleId);
                //设置地址信息
                ManaCustomerAddressDO customerAddressDO = BdUtil.do2bo(updateCustomerInfoBO, ManaCustomerAddressDO.class);
                //设置地址默认属性
                customerAddressDO.setDefaultProperty();
                Integer addressId = updateCustomerInfoBO.getAddressId();
                customerAddressDO.setId(addressId);
                customerDOMapper.updateByPrimaryKeySelective(customerDO);
                if (addressId != null) {//如果地址不为空则更新地址,否则新增地址
                    ManaCustomerAddressDO manaCustomerAddressDO = addressDOMapper.selectByPrimaryKey(addressId);
                    if (manaCustomerAddressDO != null) {
                        customerAddressDO.setGmtModified(new Date());
                        customerAddressDO.setModifier(shiroBiz.getCurrentUserRealName());
                        addressDOMapper.updateByPrimaryKeySelective(customerAddressDO);
                    }
                } else {//地址为空,新增地址
                    customerAddressDO.setGmtCreate(new Date());
                    customerAddressDO.setCreator(shiroBiz.getCurrentUserRealName());
                    customerAddressDO.setCustomerId(customerId);
                    addressDOMapper.insertSelective(customerAddressDO);
                }
                if (vehicleId != null) {
                    ManaCustomerVehicleDO vehicleDO = customerVehicleDOMapper.selectByPrimaryKey(vehicleId);
                    if (vehicleDO != null) {
                        String licencePlate = updateCustomerInfoBO.getLicencePlate();
                        if (!vehicleDO.getLicencePlate().equals(licencePlate)) {
                            ManaCustomerVehicleDO vehicle = new ManaCustomerVehicleDO();
                            vehicle.setCustomerId(customerId);
                            vehicle.setLicencePlate(licencePlate);
                            List<ManaCustomerVehicleDO> list = customerVehicleDOMapper.selectListByDO(vehicle);
                            if (!CollectionUtils.isEmpty(list)) {
                                return ResultUtil.errorResult("", "该车辆信息已存在");
                            }
                        }
                        customerVehicleDOMapper.updateByPrimaryKeySelective(customerVehicleDO);
                    }
                }
                return Result.wrapSuccessfulResult(1);
            }
            return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
        }
        return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
        */
    }

    /**
     * ========== 新增车辆 ==========
     */
    private String checkAddVehicle(AddCustomerBO addCustomerBO) {
        if (addCustomerBO == null || addCustomerBO.getCustomerId() == null) {
            return "参数不能为空";
        }
        if (addCustomerBO.getLicencePlate() == null) { //允许一个客户有一辆没有车牌的车
            addCustomerBO.setLicencePlate("");
        }

        return null;
    }

    @Transactional
    @Override
    public Result addCustomerVehicle(AddCustomerBO addCustomerBO) {
        String checkStr = checkAddVehicle(addCustomerBO);
        if (checkStr != null) {
            return ResultUtil.errorResult("", checkStr);
        }

        ManaCustomerVehicleDO checkVehicle = new ManaCustomerVehicleDO();
        checkVehicle.setCustomerId(addCustomerBO.getCustomerId());
        checkVehicle.setLicencePlate(addCustomerBO.getLicencePlate());
        List<ManaCustomerVehicleDO> vehicleDOList = customerVehicleDOMapper.selectListByDO(checkVehicle);
        if (!vehicleDOList.isEmpty()) {
            return ResultUtil.errorResult("", "该客户该车辆已经存在，不能重复添加");
        }

        ManaCustomerVehicleDO customerVehicleDO = BdUtil.do2bo(addCustomerBO, ManaCustomerVehicleDO.class);
        if (customerVehicleDO == null) {
            throw new BusinessException("系统发生内部错误");
        }
        customerVehicleDO.setGmtCreate(new Date());
        customerVehicleDO.setCreator(shiroBiz.getCurrentUserRealName());

        customerVehicleDOMapper.insertSelective(customerVehicleDO);
        Integer vehicleId = customerVehicleDO.getId();
        if (vehicleId == null) {
            return ResultUtil.errorResult("", "新增车辆信息失败，请重新添加");
        }

        /** 修改客户信息 */
        ManaCustomerDO customerDO = BdUtil.do2bo(addCustomerBO, ManaCustomerDO.class);
        customerDO.setId(addCustomerBO.getCustomerId());
        customerDO.setCustomerMobile(null); //不能修改手机号
        customerDO.setModifier(shiroBiz.getCurrentUserRealName());
        customerDO.setGmtModified(new Date());
        customerDOMapper.updateByPrimaryKeySelective(customerDO);

        /** 新增或修改地址信息 */
        ManaCustomerAddressDO addressDO = packageAddressDO(addCustomerBO.getCustomerId(), addCustomerBO);
        if (addressDO != null) {
            if (addressDO.getId() == null) {
                addressDOMapper.insertSelective(addressDO);
            } else {
                addressDOMapper.updateByPrimaryKeySelective(addressDO);
            }
        }

        /** 新增沟通记录 */
        ManaCustomerCommunicateRecordDO communicateRecordDO = packageCommunicateRecordDO(vehicleId, addCustomerBO);
        //没有沟通信息，则直接返回
        if (communicateRecordDO == null) {
            return ResultUtil.successResult(vehicleId);
        }
        communicateRecordDOMapper.insertSelective(communicateRecordDO);

        return ResultUtil.successResult(vehicleId);
    }

    /**
     * 内部 : 设置查询参数
     *
     * @param commonVOPageParam
     * @param queryStr
     */
    private void packageParam(CommonVOPageParam commonVOPageParam, String queryStr) {
        String agentName = commonVOPageParam.getAgentName();
        if(agentName!= null){
            if("".equals(agentName)){
               commonVOPageParam.setAgentName(null);
            }
        }
        //判断是手机号,车牌号或姓名
        String str = checkQueryStrType(queryStr);
        switch (str) {
            case "vehicle":
                commonVOPageParam.setLicencePlate(queryStr);
                break;
            case "mobile":
                commonVOPageParam.setCustomerMobile(queryStr);
                break;
            case "name":
                commonVOPageParam.setCustomerName(queryStr);
                break;
        }
    }

    @Override
    public PagingResult<CustomerVehiclePageBO> searchCustomer(String queryStr, Integer pageSize, Integer pageNumber) {
        CommonVOPageParam commonVOPageParam = new CommonVOPageParam();
        if (!StringUtils.isEmpty(queryStr)) {
            packageParam(commonVOPageParam, queryStr);
        }
        return searchCustomerByParam(commonVOPageParam, pageSize, pageNumber);
    }

    @Override
    public PagingResult<CustomerVehiclePageBO> searchCustomerByParam(CommonVOPageParam commonVOPageParam, Integer pageSize, Integer pageNumber) {

        CommonPageParam commonPageParam = BdUtil.do2bo(commonVOPageParam, CommonPageParam.class);

        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 8;
        }
        Integer offerSet = (pageNumber - 1) * pageSize;
        commonPageParam.setOfferSet(offerSet);
        commonPageParam.setPageSize(pageSize);
        List<CustomerVehiclePageBO> customerBOList = new ArrayList<>();
        List<CustomerVehiclePageDO> customerVehiclePageDOList = customerDOMapper.selectPagingList(commonPageParam);
        if (!customerVehiclePageDOList.isEmpty()) {
            for (CustomerVehiclePageDO customerVehiclePageDO : customerVehiclePageDOList) {
                CustomerVehiclePageBO customerVehiclePageBO = BdUtil.do2bo(customerVehiclePageDO, CustomerVehiclePageBO.class);
                customerBOList.add(customerVehiclePageBO);
            }
            //分页总数
            Integer count = customerDOMapper.selectCountByDO(commonPageParam);

            return PagingResult.wrapSuccessfulResult(customerBOList, count);
        }

        return PagingResult.wrapErrorResult("", "无查询结果");
    }


    @Override
    public Result<SearchCustomerBO> searchCustomerDetailById(Integer vehicleId) {
        if (vehicleId == null) {
            ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        //通过车辆ID获取车辆信息
        ManaCustomerVehicleDO vehicleDO = customerVehicleDOMapper.selectByPrimaryKey(vehicleId);
        if (vehicleDO != null) {
            //通过id查询客户信息
            Integer customerId = vehicleDO.getCustomerId();
            ManaCustomerDO customerDO = customerDOMapper.selectByPrimaryKey(customerId);
            if (customerDO != null) {
                //客户地址列表:暂时默认第一个地址
                SearchCustomerBO customerBO = BdUtil.do2bo(customerDO, SearchCustomerBO.class);
                Result<List<SearchCustomerAddressBO>> result = searchCustomerAddressList(customerId);
                if (result.isSuccess() && !CollectionUtils.isEmpty(result.getData())) {
                    customerBO.setAddressBO(result.getData().get(0));
                }
                SearchCustomerVehicleBO vehicleBO = BdUtil.do2bo(vehicleDO, SearchCustomerVehicleBO.class);
                //设置保险公司名称
                Integer insureCompanyId = vehicleBO.getInsureCompanyId();
                Result<String> stringResult = insuranceBiz.getInsureCompanyNameById(insureCompanyId);
                if(stringResult.isSuccess()){
                    vehicleBO.setInsureCompanyName(stringResult.getData());
                }

                //TODO 每页条数需要跟前端保持一致，获取客户沟通信息列表
                CommonVOPageParam commonVOPageParam = new CommonVOPageParam();
                commonVOPageParam.setCustomerVehicleId(vehicleId);

                //设置沟通信息
                PagingResult<SearchCommunicateRecordBO> pagingResult = communicateRecordBiz.searchCommunicationRecordPagingResult(commonVOPageParam);
                if (pagingResult.isSuccess()) {
                    vehicleBO.setRecordBOPagingResult(pagingResult);
                }

                /** 黄章挺 修改 */
                //组装投保信息
                Result<List<InsuranceInfoVO>> insuranceInfoResult = insuranceBiz.getInsuranceInfoVOList(vehicleId);
                if (insuranceInfoResult.isSuccess()) {
                    customerBO.setInsuranceInfoVOList(insuranceInfoResult.getData());
                }
                //组装优惠信息
                PreferentialVOPageParam preferentialVOPageParam = new PreferentialVOPageParam();
                preferentialVOPageParam.setCustomerVehicleId(vehicleId);
                preferentialVOPageParam.setPageIndex(1);
                preferentialVOPageParam.setPageSize(50);
                PagingResult<AddPreferentialLogBO> preferentialLogBOPagingResult =
                        preferentialLogBiz.getPreferentialBOList(preferentialVOPageParam);
                if (preferentialLogBOPagingResult.isSuccess()) {
                    List<AddPreferentialLogBO> preferentialLogBOs = preferentialLogBOPagingResult.getList();
                    List<CustomerPreferentialVO> customerPreferentialVOs = new ArrayList<>();
                    for (AddPreferentialLogBO logBO : preferentialLogBOs) {
                        CustomerPreferentialVO preferentialVO = new CustomerPreferentialVO();
                        preferentialVO.setCreator(logBO.getCreator());
                        preferentialVO.setSendDate(logBO.getSendDate());
                        preferentialVO.setPreferentialContent(logBO.getPreferentialTypeName() + "x" + logBO.getPreferentialNum());

                        customerPreferentialVOs.add(preferentialVO);
                    }
                    customerBO.setPreferentialVOList(customerPreferentialVOs);
                }

                customerBO.setVehicleBO(vehicleBO);
                return Result.wrapSuccessfulResult(customerBO);
            }
            return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
        }

        return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
    }

    @Override
    public PagingResult<CustomerPageBO> searchCustomerFormPagingResult(CommonVOPageParam commonVOPageParam) {

        String queryStr = commonVOPageParam.getQueryStr();
        if (!StringUtils.isEmpty(queryStr)) {
            //判断是手机号,车牌号或姓名
            packageParam(commonVOPageParam, queryStr);
        }
        CommonPageParam commonPageParam = BdUtil.do2bo(commonVOPageParam, CommonPageParam.class);

        Integer pageIndex = commonPageParam.getPageIndex();
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        Integer pageSize = commonPageParam.getPageSize();
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
            commonPageParam.setPageSize(pageSize);
        }
        Integer offerSet = (pageIndex - 1) * pageSize;
        commonPageParam.setOfferSet(offerSet);
        //客户和车辆信息查询
        List<CustomerPageDO> pageDOList = customerDOMapper.selectCustomerFormPageList(commonPageParam);
        if (!CollectionUtils.isEmpty(pageDOList)) {
            List<CustomerPageBO> pageBOList = new ArrayList<>();
            for (CustomerPageDO customerPageDO : pageDOList) {
                Integer insureCompanyId = customerPageDO.getInsureCompanyId();
                Integer agent_id = customerPageDO.getAgentId();
                CustomerPageBO customerPageBO = BdUtil.do2bo(customerPageDO, CustomerPageBO.class);
                Integer cooperationCode = customerPageBO.getCooperationMode();
                String cooperationCodeName = insuranceDicBiz.getCooperationModeNameByDicId(cooperationCode);
                customerPageBO.setCooperationModeDescription(cooperationCodeName);
                //TODO 目前只有门店，暂时不做判断了
//                if (AgentTypeEnum.SHOP.getCode().equals(agent_type)) {//1:表示代理人为门店类型
                //通过门店id获取门店信息
                SimpleShopBO simpleShopBO = extShopInfoService.getSimpleShopInfo(agent_id);
                if (simpleShopBO == null) {
                    simpleShopBO = new SimpleShopBO();
                    simpleShopBO.setCompanyName(customerPageDO.getAgentName());
                }
                customerPageBO.setSimpleShopBO(simpleShopBO);
//                }
                //通过保险公司id获取保险公司名称
                Result<String> stringResult = insuranceBiz.getInsureCompanyNameById(insureCompanyId);
                if(stringResult.isSuccess()){
                    customerPageBO.setInsureCompanyName(stringResult.getData());
                }

                //如果有沟通记录,获取沟通记录的登记人和时间
                //如果没有沟通记录,获取车辆信息创建人和时间
                Integer customerVehicleId = customerPageBO.getCustomerVehicleId();
                ManaCustomerCommunicateRecordDO communicateRecordDO = communicateRecordDOMapper.selectLatestCommunicateRecordDOById(customerVehicleId);
                if (communicateRecordDO != null) {
                    String creator = communicateRecordDO.getCreator();
                    Date gmtCreate = communicateRecordDO.getGmtCreate();
                    customerPageBO.setCreator(creator);
                    customerPageBO.setRegDate(gmtCreate);
                } else {
                    Date gmtCreate = customerPageBO.getGmtCreate();
                    customerPageBO.setRegDate(gmtCreate);
                }
                //获取最近一页5条优惠信息
                PreferentialVOPageParam preferentialVOPageParam = new PreferentialVOPageParam();
                preferentialVOPageParam.setCustomerVehicleId(customerVehicleId);
                PagingResult<AddPreferentialLogBO> pagingResult = preferentialLogBiz.getPreferentialBOList(preferentialVOPageParam);
                if (pagingResult.isSuccess()) {
                    List<AddPreferentialLogBO> preferentialLogBOList = pagingResult.getList();
                    customerPageBO.setPreferentialLogBOList(preferentialLogBOList);
                }

                pageBOList.add(customerPageBO);
            }
            //客户业务分页总数
            Integer count = customerDOMapper.selectCustomerFormPageCount(commonPageParam);

            return PagingResult.wrapSuccessfulResult(pageBOList, count);
        }

        return PagingResult.wrapErrorResult("", "无查询结果");
    }

    @Override
    public Result<List<SearchCustomerAddressBO>> searchCustomerAddressList(Integer customerId) {
        if (customerId == null) {
            ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        List<ManaCustomerAddressDO> addressDOList = addressDOMapper.selectAddressListById(customerId);
        if (!addressDOList.isEmpty()) {
            List<SearchCustomerAddressBO> addressBOList = BdUtil.do2bo4List(addressDOList, SearchCustomerAddressBO.class);
            for (SearchCustomerAddressBO addressBO : addressBOList) {

                List<Integer> regionIds = new ArrayList<>();
                regionIds.add(addressBO.getCustomerProvince());
                regionIds.add(addressBO.getCustomerCity());
                regionIds.add(addressBO.getCustomerDistrict());
                Map<Integer, String> map = extRegionService.getRegionNameMap(regionIds);
                StringBuilder stringAddress = new StringBuilder();
                if (map != null) {
                    String provinceName = map.get(addressBO.getCustomerProvince());
                    String cityName = map.get(addressBO.getCustomerCity());
                    String districtName = map.get(addressBO.getCustomerDistrict());

                    if (provinceName != null) {
                        stringAddress.append(provinceName);
                    }
                    if (cityName != null) {
                        stringAddress.append(cityName);
                    }
                    if (districtName != null) {
                        stringAddress.append(districtName);
                    }

                    addressBO.setCustomerProvinceName(provinceName);
                    addressBO.setCustomerCityName(cityName);
                    addressBO.setCustomerDistrictName(districtName);
                }

                String address = addressBO.getCustomerAddress();
                if (address != null) {
                    stringAddress.append(address);
                }
                addressBO.setAddressStr(stringAddress.toString());
            }
            return Result.wrapSuccessfulResult(addressBOList);
        }
        return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
    }

    @Override
    public Result<ManaCustomerDO> getCustomerById(Integer id) {
        if (id == null || id < 1) {
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        ManaCustomerDO customerDO = customerDOMapper.selectByPrimaryKey(id);
        if (customerDO == null) {
            return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
        }
        return ResultUtil.successResult(customerDO);
    }

    /**
     * 内部:判断入参是手机号.车牌号或姓名
     *
     * @param queryStr
     * @return
     */
    private String checkQueryStrType(String queryStr) {

        if (ManaUtil.isMobile(queryStr)) {
            return "mobile";
        }
        if (ManaUtil.isVehicle(queryStr)) {
            return "vehicle";
        }
        return "name";
    }

    @Override
    public Result modifyAddress(UpdateCustomerAddressBO addressBO) {
        if (addressBO == null || addressBO.getId() == null) {
            return ResultUtil.errorResult("", "参数不能为空");
        }
        Integer id = addressBO.getId();
        ManaCustomerAddressDO manaCustomerAddressDO = addressDOMapper.selectByPrimaryKey(id);
        if (manaCustomerAddressDO == null) {
            return ResultUtil.errorResult("", "地址不存在，id：" + id);
        }
        ManaCustomerAddressDO customerAddressDO = BdUtil.do2bo(addressBO, ManaCustomerAddressDO.class);
        if (customerAddressDO == null) {
            throw new BusinessException("系统内部发生错误");
        }
        customerAddressDO.setGmtModified(new Date());
        customerAddressDO.setModifier(shiroBiz.getCurrentUserRealName());

        return ResultUtil.successResult(addressDOMapper.updateByPrimaryKeySelective(customerAddressDO));
    }

    @Override
    public Result<CustomerInfoBO> getCustomerInfoById(Integer id) {
        if (id == null || id < 1) {
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        ManaCustomerDO customerDO = customerDOMapper.selectByPrimaryKey(id);
        if (customerDO == null) {
            return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
        }
        CustomerInfoBO customerInfoBO = BdUtil.do2bo(customerDO, CustomerInfoBO.class);
        List<ManaCustomerAddressDO> addressDOList = addressDOMapper.selectAddressListById(id);
        if (!addressDOList.isEmpty()) {
            CustomerAddressBO addressBO = BdUtil.do2bo(addressDOList.get(0), CustomerAddressBO.class);
            customerInfoBO.setAddressBO(addressBO);
        }
        return ResultUtil.successResult(customerInfoBO);
    }

    @Override
    public Result<CustomerAddressBO> getAddressByCustomerId(Integer customerId) {
        if (customerId == null || customerId < 1) {
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        List<ManaCustomerAddressDO> addressDOList = addressDOMapper.selectAddressListById(customerId);
        if (addressDOList.isEmpty()) {
            return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
        }
        CustomerAddressBO addressBO = BdUtil.do2bo(addressDOList.get(0), CustomerAddressBO.class);
        List<Integer> regionIds = new ArrayList<>();
        Integer cityId = addressBO.getCustomerCity();
        Integer districtId = addressBO.getCustomerDistrict();
        if(cityId!=null && cityId>0){
            regionIds.add(cityId);
        }
        if(districtId!=null && districtId>0){
            regionIds.add(districtId);
        }
        if(!regionIds.isEmpty()){
            Map<Integer, String> map = extRegionService.getRegionNameMap(regionIds);
            if(map!=null){
                if(cityId!=null && cityId>0){
                    addressBO.setCityName(map.get(cityId));
                }
                if(districtId!=null && districtId>0){
                    addressBO.setDistrictName(map.get(districtId));
                }
            }
        }

        return ResultUtil.successResult(addressBO);
    }

}
