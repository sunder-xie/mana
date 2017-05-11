package com.tqmall.mana.biz.manager.insurance;

import com.google.common.collect.Lists;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.result.*;
import com.tqmall.mana.beans.BO.InsuranceBasicBO;
import com.tqmall.mana.beans.BO.insurance.InsuranceCompanyBO;
import com.tqmall.mana.beans.BO.insurance.InsuranceTypeBO;
import com.tqmall.mana.beans.BO.outInsurance.InsuranceFormBO;
import com.tqmall.mana.beans.BO.outInsurance.InsuranceItemBO;
import com.tqmall.mana.beans.BO.shop.SimpleShopBO;
import com.tqmall.mana.beans.VO.InsuranceFormInfoVO;
import com.tqmall.mana.beans.VO.InsuranceInfoVO;
import com.tqmall.mana.beans.VO.InsurancePriceParityVO;
import com.tqmall.mana.beans.entity.insurance.ManaInsuranceFormSyncLogDO;
import com.tqmall.mana.beans.entity.insurance.ManaInsuranceItemDO;
import com.tqmall.mana.beans.param.CommonPageParam;
import com.tqmall.mana.beans.param.CommonVOPageParam;
import com.tqmall.mana.component.annotation.cache.ManaCache;
import com.tqmall.mana.component.bean.DataError;
import com.tqmall.mana.component.enums.insurance.dict.InsuranceTypeEnum;
import com.tqmall.mana.component.enums.YesNoEnum;
import com.tqmall.mana.component.redis.RedisKeyBean;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.DateUtil;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.dao.mapper.insurance.ManaInsuranceFormSyncLogDOMapper;
import com.tqmall.mana.dao.mapper.insurance.ManaInsuranceItemDOMapper;
import com.tqmall.mana.external.dubbo.insurance.ExtInsuranceCompanyService;
import com.tqmall.mana.external.dubbo.insurance.ExtInsuranceFormService;
import com.tqmall.mana.external.dubbo.insurance.ExtInsuranceUserService;
import com.tqmall.mana.external.dubbo.insurance.ExtInsuranceVirtualFormService;
import com.tqmall.mana.external.dubbo.uc.ExtShopInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by huangzhangting on 16/12/2.
 */
@Slf4j
@Service
public class InsuranceBizImpl implements InsuranceBiz {
    @Autowired
    private ExtInsuranceFormService extInsuranceFormService;
    @Autowired
    private ExtShopInfoService extShopInfoService;
    @Autowired
    private ExtInsuranceVirtualFormService extInsuranceVirtualFormService;
    @Autowired
    private ManaInsuranceItemDOMapper insuranceItemDOMapper;
    @Autowired
    private ManaInsuranceFormSyncLogDOMapper insuranceFormSyncLogDOMapper;
    @Autowired
    private ExtInsuranceUserService extInsuranceUserService;
    @Autowired
    private ExtInsuranceCompanyService extInsuranceCompanyService;
    @Autowired
    private InsuranceDicBiz insuranceDicBiz;


    @Override
    public Result<List<InsurancePriceParityVO>> getInsurancePriceParityVOList(String vehicleSn) {
        if(StringUtils.isEmpty(vehicleSn)){
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        List<InsuranceFormDTO> formDTOList = extInsuranceFormService.getInsuranceForms(vehicleSn);
        if(formDTOList.isEmpty()){
            return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
        }

        Map<Integer, InsurancePriceParityVO> voMap = new HashMap<>();
        for(InsuranceFormDTO formDTO : formDTOList){
            InsuranceBasicDTO basicDTO = formDTO.getBasicDTO();
            if(basicDTO==null){
                log.info("组装比价VO时, 存在保单没有关联insurance_basic, form id:{}", formDTO.getId());
                continue;
            }
            Integer insuranceBasicId = formDTO.getInsuranceBasicId();
            InsurancePriceParityVO parityVO = voMap.get(insuranceBasicId);
            if(parityVO==null){
                parityVO = new InsurancePriceParityVO();
                voMap.put(insuranceBasicId, parityVO);

                parityVO.setInsuranceBasicId(insuranceBasicId);
                parityVO.setGmtCreateStr(DateUtil.dateToString(formDTO.getGmtCreate(), DateUtil.yyyy_MM_dd_HH_mm_ss));
                parityVO.setBasicBO(BdUtil.do2bo(basicDTO, InsuranceBasicBO.class));
                setInsuranceNo(parityVO, formDTO);
            }else{
                if(parityVO.getBasicBO()==null){ //实际上没什么必要，暂时先写着
                    parityVO.setBasicBO(BdUtil.do2bo(basicDTO, InsuranceBasicBO.class));
                }
                setInsuranceNo(parityVO, formDTO);
            }
        }
        int size = voMap.size();
        if(size == 0){
            return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
        }
        /** 根据时间降序排列 */
        List<InsurancePriceParityVO> list = new ArrayList<>(voMap.values());
        if(size > 1) {
            Collections.sort(list, new Comparator<InsurancePriceParityVO>() {
                @Override
                public int compare(InsurancePriceParityVO o1, InsurancePriceParityVO o2) {
                    return o2.getGmtCreateStr().compareTo(o1.getGmtCreateStr());
                }
            });

            //todo 只取3条给前端
            if(size > 3){
                for(int i=size-1; i>2; i--){
                    list.remove(i);
                }
            }
        }

        return ResultUtil.successResult(list);
    }

    /** 设置投保单号 */
    private void setInsuranceNo(InsurancePriceParityVO parityVO, InsuranceFormDTO formDTO){
        switch (formDTO.getInsuranceType()){
            case 1: parityVO.setTciNo(formDTO.getOuterInsuranceApplyNo()); break; //交强险
            case 2: parityVO.setVciNo(formDTO.getOuterInsuranceApplyNo()); break; //商业险
            default: break;
        }
    }

    @Override
    public Result<List<ManaInsuranceItemDO>> getInsuranceItemDOList(Integer insuranceBasicId) {
        if(insuranceBasicId==null || insuranceBasicId<1){
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        List<ManaInsuranceItemDO> itemDOList = insuranceItemDOMapper.selectByBasicId(insuranceBasicId);
        if(itemDOList.isEmpty()){
            return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
        }

        return ResultUtil.successResult(itemDOList);
    }

    @Override
    public Result<ManaInsuranceItemDO> getInsuranceItemDOByBasicIdAndName(Integer insuranceBasicId, String insuranceName) {
        if(insuranceBasicId==null || insuranceBasicId<1 || StringUtils.isEmpty(insuranceName)){
            log.info("wrong.insuranceBasicId:{},insuranceName:{}",insuranceBasicId,insuranceName);
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }

        Result<List<ManaInsuranceItemDO>> insuranceItemResult = getInsuranceItemDOList(insuranceBasicId);
        if(insuranceItemResult.isSuccess()){
            List<ManaInsuranceItemDO> list = insuranceItemResult.getData();
            for (ManaInsuranceItemDO insuranceItemDO : list){
                if(insuranceItemDO.getInsuranceName().equals(insuranceName)){
                    //存在记录
                    return Result.wrapSuccessfulResult(insuranceItemDO);
                }
            }
        }
        // 数据库里面没有记录
        return ResultUtil.successResult(null);
    }

    @Override
    public Result<String> updateInsuranceItem(ManaInsuranceItemDO upDO, ManaInsuranceItemDO whereDO) {

        try {
            insuranceItemDOMapper.updateByDO(upDO,whereDO);
        }catch (Exception e){
            log.error("update insuranceItemDOMapper error.", e);
            return Result.wrapErrorResult("00","up wrong");
        }
        return ResultUtil.successResult("success");
    }

    @Override
    public Result<String> saveInsuranceItem(List<ManaInsuranceItemDO> saveList) {
        try {
            if(!CollectionUtils.isEmpty(saveList)) {
                insuranceItemDOMapper.insertBatch(saveList);
            }
        }catch (Exception e){
            log.error("update insuranceItemDOMapper error.", e);
            return Result.wrapErrorResult("00","insert batch wrong");
        }
        return ResultUtil.successResult("success");
    }

    @Override
    public Result<List<InsuranceFormBO>> getInsuranceFormPagingResult(CommonVOPageParam commonVOPageParam) {
        if(commonVOPageParam.getCustomerVehicleId() == null){
            ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        //参数设置
        CommonPageParam commonPageParam = BdUtil.do2bo(commonVOPageParam,CommonPageParam.class);

        //通过车辆id查询保单日志信息
        List<ManaInsuranceFormSyncLogDO> insuranceFormSyncLogDOList = insuranceFormSyncLogDOMapper.selectInsuranceFormList(commonPageParam);
        if(!CollectionUtils.isEmpty(insuranceFormSyncLogDOList)){
            //1:按照basic_id分组
            Map<Integer,List<ManaInsuranceFormSyncLogDO>> map = new HashMap<>();
            for(ManaInsuranceFormSyncLogDO insuranceFormSyncLogDO : insuranceFormSyncLogDOList){
                Integer basicId = insuranceFormSyncLogDO.getInsuranceBasicId();

//                boolean findFlag = false;
//                for(Integer key : map.keySet()){
//                    if(key.equals(basic_id)){
//                        List<ManaInsuranceFormSyncLogDO> list = map.get(key);
//                        list.add(insuranceFormSyncLogDO);
//                        map.put(key,list);
//                        findFlag = true;
//                    }
//                }
//                if(!findFlag){//map中没有找到该key
//                    List<ManaInsuranceFormSyncLogDO> list = new ArrayList<>();
//                    list.add(insuranceFormSyncLogDO);
//                    map.put(basic_id,list);
//                }

                List<ManaInsuranceFormSyncLogDO> list = map.get(basicId);
                if(list==null){
                    list = new ArrayList<>();
                    map.put(basicId, list);
                }
                list.add(insuranceFormSyncLogDO);
            }

            List<InsuranceFormBO> insuranceFormBOList = new ArrayList<>();
            for(Integer key : map.keySet()){
                List<ManaInsuranceFormSyncLogDO> formSyncLogDOList = map.get(key);
                ManaInsuranceFormSyncLogDO insuranceFormSyncLogDO = formSyncLogDOList.get(0);
                InsuranceFormBO insuranceFormBO = BdUtil.do2bo(insuranceFormSyncLogDO,InsuranceFormBO.class);
                String cooperationName = insuranceDicBiz.getCooperationModeNameByDicId(insuranceFormBO.getCooperationMode());
                insuranceFormBO.setCooperationModeDescription(cooperationName);
                //查询保单信息
                Integer formId = insuranceFormSyncLogDO.getInsuranceFormId();//保单id
                Integer agentType = insuranceFormSyncLogDO.getAgentType();//代理人类型
                Integer agentId = insuranceFormSyncLogDO.getAgentId();//代理人id

                //TODO 目前只有门店，暂时不做判断了
//                if(agentType.equals(AgentTypeEnum.SHOP.getCode())) {//表示代理人 类型为门店
                    //通过门店id获取门店信息
                    SimpleShopBO simpleShopBO = extShopInfoService.getSimpleShopInfo(agentId);
                    if (simpleShopBO != null) {
                        StringBuffer sb = new StringBuffer();
                        sb.append(simpleShopBO.getCompanyName());
                        if(simpleShopBO.getDefaultAccountMobile() != null) {
                            sb.append(" ").append(simpleShopBO.getDefaultAccountMobile());
                        }
                        insuranceFormBO.setAgentName(sb.toString());
                    }
//                }

                //设置保费:需要通过外部接口获取
                List<InsuranceTypeBO> insuranceTypeBOList = new ArrayList<>();
                for(ManaInsuranceFormSyncLogDO formSyncLogDO : formSyncLogDOList){
                    Integer form_id = formSyncLogDO.getInsuranceFormId();//保单id
                    Integer agent_id = formSyncLogDO.getAgentId();//代理人id
                    //获取通过保单id和代理人id获取保单信息
                    InsuranceFormDTO insuranceFormDTO  = extInsuranceFormService.getInsuranceFormDetail(form_id,agent_id);
                    if(insuranceFormDTO != null){
                        InsuranceTypeBO insuranceTypeBO = BdUtil.do2bo(insuranceFormDTO,InsuranceTypeBO.class);
                        insuranceTypeBOList.add(insuranceTypeBO);
                        insuranceFormBO.setInsuranceTypeBOList(insuranceTypeBOList);
                    }
                }
                //保费选择:is_virtual = 1时表示虚拟保单
                if(insuranceFormSyncLogDO.getIsVirtual().equals(YesNoEnum.YES.getCode())
                        && insuranceFormSyncLogDO.getVirtualInsuranceFormId() != null){
                    //虚拟保单 :保单金额
                    InsuranceVirtualFormDTO insuranceVirtualFormDTO = extInsuranceVirtualFormService.getVirtualInsuranceFormDetailByFormId(formId,agentId);
                    if(insuranceVirtualFormDTO != null){
                        BigDecimal insuredFee = insuranceVirtualFormDTO.getInsuredFee();
                        insuranceFormBO.setInsuredFee(insuredFee);
                    }
                }else {
                    //真实保单
                    //获取通过保单id和代理人id获取保单信息
                    InsuranceFormDTO insuranceFormDTO = extInsuranceFormService.getInsuranceFormDetail(formId, agentId);
                    if (insuranceFormDTO != null) {
                        BigDecimal insuredFee = insuranceFormDTO.getInsuredFee();
                        insuranceFormBO.setInsuredFee(insuredFee);
                    }
                }

                insuranceFormBOList.add(insuranceFormBO);
            }

            return Result.wrapSuccessfulResult(insuranceFormBOList);
        }

        return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
    }

    @Override
    public Result<InsuranceFormBO> getInsuranceFormInfo(Integer formId, Integer agentId) {
        if(formId == null || agentId == null){
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        InsuranceFormDTO insuranceFormDTO  = extInsuranceFormService.getInsuranceFormDetail(formId,agentId);
        if(insuranceFormDTO != null ){
            InsuranceFormBO insuranceFormBO = BdUtil.do2bo(insuranceFormDTO,InsuranceFormBO.class);
            String cooperationName = insuranceDicBiz.getCooperationModeNameByDicId(insuranceFormBO.getCooperationMode());
            insuranceFormBO.setCooperationModeDescription(cooperationName);
            InsuranceBasicDTO insuranceBasicDTO = insuranceFormDTO.getBasicDTO();
            List<InsuranceItemDTO> insuranceItemDTOList = insuranceFormDTO.getItemDTOList();
            InsuranceBasicBO insuranceBasicBO = BdUtil.do2bo(insuranceBasicDTO,InsuranceBasicBO.class);
            List<InsuranceItemBO> insuranceItemBOList = new ArrayList<>();
            for(InsuranceItemDTO insuranceItemDTO : insuranceItemDTOList){
                InsuranceItemBO insuranceItemBO = BdUtil.do2bo(insuranceItemDTO,InsuranceItemBO.class);
                for(YesNoEnum yesNoEnum : YesNoEnum.values()){
                    if(yesNoEnum.getCode().equals(insuranceItemDTO.getIsDeductible())){
                        String deductiveName = YesNoEnum.codeDescription(yesNoEnum.getCode());
                        insuranceItemBO.setDeductiveName(deductiveName);
                        break;
                    }
                }
                insuranceItemBOList.add(insuranceItemBO);
            }
            insuranceFormBO.setBasicBO(insuranceBasicBO);
            insuranceFormBO.setItemBOList(insuranceItemBOList);

            return Result.wrapSuccessfulResult(insuranceFormBO);
        }

        return ResultUtil.errorResult(DataError.NO_DATA_ERROR);

    }

    @ManaCache(cacheName = RedisKeyBean.INSURANCE_COMPANY_KEY)
    @Override
    public List<InsuranceCompanyBO> getAllCompanyList() {

        List<InsuranceCompanyDTO> companyDTOList = extInsuranceCompanyService.allCompanyList();
        if(!CollectionUtils.isEmpty(companyDTOList)){
            return BdUtil.do2bo4List(companyDTOList, InsuranceCompanyBO.class);
        }
        return Lists.newArrayList();
    }

    @Override
    public Result<String> getInsureCompanyNameById(Integer insureCompanyId) {
        if(insureCompanyId == null){
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        List<InsuranceCompanyBO> companyBOList = getAllCompanyList();
        for (InsuranceCompanyBO insuranceCompanyBO : companyBOList) {
            if (insuranceCompanyBO.getId().equals(insureCompanyId)) {
                String companyName = insuranceCompanyBO.getCompanyName();
                return Result.wrapSuccessfulResult(companyName);
            }
        }
        return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
    }

    @Override
    public Result<List<InsuranceInfoVO>> getInsuranceInfoVOList(Integer vehicleId) {
        if(vehicleId==null || vehicleId<1){
            return ResultUtil.errorResult(DataError.ARG_ERROR);
        }
        CommonPageParam param = new CommonPageParam();
        param.setCustomerVehicleId(vehicleId);

        //通过车辆id查询保单日志信息
        List<ManaInsuranceFormSyncLogDO> logDOList = insuranceFormSyncLogDOMapper.selectInsuranceFormList(param);
        if(logDOList.isEmpty()){
            return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
        }

        Map<String, InsuranceInfoVO> map = new HashMap<>();
        for(ManaInsuranceFormSyncLogDO logDO : logDOList){
            String key = logDO.getInsuranceBasicId()+ "_" + logDO.getIsVirtual();
            InsuranceInfoVO insuranceInfoVO = map.get(key);
            if(insuranceInfoVO==null){
                map.put(key, packageInsuranceInfoVO(logDO));
            }else{
                packageInsuranceFormInfoVO(logDO, insuranceInfoVO);
            }
        }

        List<InsuranceInfoVO> infoVOList = new ArrayList<>(map.values());
        Collections.sort(infoVOList, new Comparator<InsuranceInfoVO>() {
            @Override
            public int compare(InsuranceInfoVO o1, InsuranceInfoVO o2) {
                return o2.getGmtCreate().compareTo(o1.getGmtCreate());
            }
        });

        return ResultUtil.successResult(infoVOList);
    }
    private InsuranceInfoVO packageInsuranceInfoVO(ManaInsuranceFormSyncLogDO logDO){
        InsuranceInfoVO infoVO = new InsuranceInfoVO();
        infoVO.setGmtCreate(logDO.getGmtCreate());
        infoVO.setInsuranceBasicId(logDO.getInsuranceBasicId());
        Integer cooperateMode = logDO.getCooperationMode();
        String cooperationCodeName = insuranceDicBiz.getCooperationModeNameByDicId(cooperateMode);
        infoVO.setCooperationModeStr(cooperationCodeName);
        infoVO.setConfigType(logDO.getConfigType());
        infoVO.setAgentName(logDO.getAgentName());

        Integer agentId = logDO.getAgentId();
        infoVO.setAgentId(agentId);
        
        /** 设置门店信息 */
        //TODO 目前只有门店，暂时不做判断了
//        if(AgentTypeEnum.SHOP.getCode().equals(logDO.getAgentType())){
            //通过门店id获取门店信息
            SimpleShopBO simpleShopBO = extShopInfoService.getSimpleShopInfo(agentId);
            if(simpleShopBO != null){
                StringBuilder sb = new StringBuilder();
                sb.append(simpleShopBO.getCompanyName());
                if(simpleShopBO.getDefaultAccountMobile() != null){
                    sb.append(" ").append(simpleShopBO.getDefaultAccountMobile());
                }
                infoVO.setAgentName(sb.toString());
            }
//        }

        Integer isVirtual = logDO.getIsVirtual();
        infoVO.setIsVirtual(isVirtual);
        Integer viFormId = logDO.getVirtualInsuranceFormId();
        /** 虚拟保单，即买服务包 */
        if(YesNoEnum.YES.getCode().equals(isVirtual)){
            infoVO.setCanAddPayAmount(false);
            if(viFormId != null && viFormId > 0) {
                InsuranceVirtualFormDTO virtualFormDTO =
                        extInsuranceVirtualFormService.getVirtualInsuranceFormDetailByFormId(viFormId, agentId);
                if (virtualFormDTO != null) {
                    infoVO.setPayAmount(virtualFormDTO.getFirstPaidAmount()); // 第一次支付金额

                    InsuranceUserServicePackageDTO packageDTO =
                            extInsuranceUserService.getPackageById(virtualFormDTO.getUserServicePackageId());
                    if(packageDTO != null) {
                        infoVO.setPackageName(packageDTO.getPackageName());
                        infoVO.setTotalMaterialPrice(packageDTO.getTotalMaterialPrice());
                        infoVO.setMarketPrice(packageDTO.getMarketPrice());
                    }
                }
            }
            return infoVO;
        }

        /** 买服务包送保险 */
        if(viFormId != null && viFormId > 0){
            infoVO.setCanAddPayAmount(false);
            InsuranceVirtualFormDTO virtualFormDTO =
                    extInsuranceVirtualFormService.getVirtualInsuranceFormDetailByFormId(viFormId, agentId);
            if(virtualFormDTO != null){
                infoVO.setPayAmount(virtualFormDTO.getSecondPaidAmount()); // 第二次支付金额
            }
        }

        packageInsuranceFormInfoVO(logDO, infoVO);

        return infoVO;
    }
    private void packageInsuranceFormInfoVO(ManaInsuranceFormSyncLogDO logDO, InsuranceInfoVO infoVO){
        Integer formId = logDO.getInsuranceFormId();
        InsuranceFormInfoVO formInfoVO = new InsuranceFormInfoVO();
        formInfoVO.setFormId(formId);

        //真实保单
        //获取通过保单id和代理人id获取保单信息
        InsuranceFormDTO insuranceFormDTO = extInsuranceFormService.getInsuranceFormDetail(formId, logDO.getAgentId());
        if (insuranceFormDTO != null) {
            formInfoVO.setInsuredFee(insuranceFormDTO.getInsuredFee());
            formInfoVO.setInsureTypeStr(InsuranceTypeEnum.codeDescription(insuranceFormDTO.getInsuranceType()));
        }

        /** 设置支付金额 */
        if(infoVO.isCanAddPayAmount()){
            BigDecimal insuredFee = formInfoVO.getInsuredFee();
            if(insuredFee != null){
                BigDecimal payAmount = infoVO.getPayAmount();
                if(payAmount == null){
                    infoVO.setPayAmount(insuredFee);
                }else{
                    infoVO.setPayAmount(payAmount.add(insuredFee));
                }
            }
        }

        /** 组装保单对象 */
        List<InsuranceFormInfoVO> formInfoVOList = infoVO.getFormInfoVOList();
        if(formInfoVOList == null){
            formInfoVOList = new ArrayList<>();
            infoVO.setFormInfoVOList(formInfoVOList);
        }
        formInfoVOList.add(formInfoVO);

    }

}
