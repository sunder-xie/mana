package com.tqmall.mana.biz.manager.offline;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.errorcode.support.SourceKey;
import com.tqmall.insurance.domain.param.insurance.offline.InsuranceOffLineTempInfoParam;
import com.tqmall.insurance.domain.param.insurance.offline.SearchTempInfoListParam;
import com.tqmall.insurance.domain.result.offline.InsuranceOfflineTempInfoDTO;
import com.tqmall.mana.beans.BO.insurance.InsuranceCompanyBO;
import com.tqmall.mana.beans.BO.offline.InsuranceOfflineTempInfoBO;
import com.tqmall.mana.beans.BO.settle.calculate.InsuranceSettleBasicBO;
import com.tqmall.mana.beans.BO.settle.calculate.InsuranceSettleFormBO;
import com.tqmall.mana.beans.BO.sys.UserBO;
import com.tqmall.mana.beans.VO.insurance.InsuranceOffLineTempInfoVO;
import com.tqmall.mana.beans.param.insurance.SearchTempInfoParam;
import com.tqmall.mana.biz.manager.insurance.InsuranceBiz;
import com.tqmall.mana.biz.manager.settle.settleCalculate.InsuranceSettleCalculateExtendBiz;
import com.tqmall.mana.biz.manager.shiro.ShiroBiz;
import com.tqmall.mana.component.enums.insurance.dict.CooperationModeEnum;
import com.tqmall.mana.component.enums.insurance.dict.InsuranceOffLineAuditStatusEnum;
import com.tqmall.mana.component.enums.insurance.dict.InsuranceTypeEnum;
import com.tqmall.mana.component.enums.insurance.dict.OffLineAuditStatusEnum;
import com.tqmall.mana.component.exception.BusinessCheckException;
import com.tqmall.mana.component.exception.BusinessException;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.excel.PoiUtil;
import com.tqmall.mana.component.util.mana.ManaUtil;
import com.tqmall.mana.external.dubbo.offline.ExtInsuranceOffLineService;
import com.tqmall.mana.external.dubbo.uc.ExtShopInfoService;
import com.tqmall.ucenter.object.result.account.AccountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouheng on 17/3/24.
 */
@Slf4j
@Service
public class OffLineInsuranceBizImpl implements OffLineInsuranceBiz {

    @Autowired
    private ExtInsuranceOffLineService extInsuranceOffLineService;

    @Autowired
    private ExtShopInfoService extShopInfoService;

    @Autowired
    private InsuranceSettleCalculateExtendBiz settleCalculateExtendBiz;

    @Autowired
    private InsuranceBiz insuranceBiz;

    @Autowired
    private ShiroBiz shiroBiz;


    @Override
    public PagingResult<InsuranceOfflineTempInfoBO> getTempInfoPageList(SearchTempInfoParam searchTempInfoParam) {

        SearchTempInfoListParam searchTempInfoListParam = BdUtil.do2bo(searchTempInfoParam,SearchTempInfoListParam.class);

        searchTempInfoListParam.setSource(SourceKey.CRM.getKey());
        String agentMobile = searchTempInfoListParam.getAgentMobile();
        if(agentMobile != null && !ManaUtil.isMobile(agentMobile)){
            throw new BusinessCheckException("001","请填写完整手机号");
        }
        if (!StringUtils.isEmpty(agentMobile)) {
            //根据手机号查询agentId
            AccountDTO account= extShopInfoService.getAccountByMobile(agentMobile);
            if(account != null){
                searchTempInfoListParam.setAgentId(account.getShopId());
            }else{
                List<InsuranceOfflineTempInfoBO> list = Lists.newArrayList();
                return PagingResult.wrapSuccessfulResult(list,0);
            }
        }

        PagingResult<InsuranceOfflineTempInfoDTO> pagingResult = extInsuranceOffLineService.getTempInfoPageList(searchTempInfoListParam);
        if (!pagingResult.isSuccess()) {
            throw new BusinessException(pagingResult.getMessage());
        }
        setAgentMobileForList(pagingResult.getList());

        List<InsuranceOfflineTempInfoBO> tempInfoBOList = BdUtil.do2bo4List(pagingResult.getList(), InsuranceOfflineTempInfoBO.class);
        return PagingResult.wrapSuccessfulResult(tempInfoBOList, pagingResult.getTotal());
    }

    private void setAgentMobileForList(List<InsuranceOfflineTempInfoDTO> dtoList) {

        for (InsuranceOfflineTempInfoDTO tempInfoDTO : dtoList) {
            Integer agentId = tempInfoDTO.getAgentId();
            //根据手机号查询agentId
            log.info("根据门店id调用uc接口获取门店信息,agentId={}", agentId);
            AccountDTO accountDTO = extShopInfoService.getAccountByShopId(agentId);
            if (accountDTO == null) {
                log.error("调用uc获取门店id失败,agentId={}", agentId);
            }else{
                log.info("根据门店id调用uc接口获取门店信息,account={}", accountDTO);
                tempInfoDTO.setAgentMobile(accountDTO.getMobile());
            }
        }
    }

    @Override
    public void modifyEnteringFormAuditStatus(InsuranceOffLineTempInfoVO tempInfoVO) {
        UserBO user = shiroBiz.getCurrentUser();
        tempInfoVO.setAuditor(Integer.valueOf(user.getUserName()));
        tempInfoVO.setAuditName(user.getRealName());
        tempInfoVO.setGmtAudit(new Date());
        InsuranceOffLineTempInfoParam tempInfoParam = BdUtil.do2bo(tempInfoVO,InsuranceOffLineTempInfoParam.class);

        String orderSn = extInsuranceOffLineService.modifyEnteringFormAuditStatus(tempInfoParam);
        Integer auditStatus = tempInfoParam.getAuditStatus();
        if (auditStatus.compareTo(InsuranceOffLineAuditStatusEnum.SHTG.getCode()) == 0) {
            InsuranceSettleBasicBO settleBasicBO = packageSettleData(tempInfoVO);
            settleBasicBO.setInsuranceOrderSn(orderSn);
            settleCalculateExtendBiz.calculateSettleData(settleBasicBO);
        }
    }

    @Override
    public void exportOffLineInsuranceList(HttpServletResponse response, SearchTempInfoParam searchTempInfoParam) {
        PoiUtil poiUtil = new PoiUtil();
        SearchTempInfoListParam searchTempInfoListParam = BdUtil.do2bo(searchTempInfoParam,SearchTempInfoListParam.class);
        int pageSize = 500;
        searchTempInfoListParam.setPageSize(pageSize);
        searchTempInfoListParam.setSource(SourceKey.CRM.getKey());
        PagingResult<InsuranceOfflineTempInfoDTO> pagingResult = extInsuranceOffLineService.getTempInfoPageList(searchTempInfoListParam);
        if(CollectionUtils.isEmpty(pagingResult.getList())){
            List<InsuranceOfflineTempInfoDTO> tempInfoDTOList = Lists.newArrayList();
            pagingResult.setList(tempInfoDTOList);
        }
        List<InsuranceOfflineTempInfoDTO> tempInfoDTOList = pagingResult.getList();
        setAgentMobileForList(tempInfoDTOList);
        List<InsuranceOfflineTempInfoBO> tempInfoBOList =  setCompanyInfo(tempInfoDTOList);
        String[] heads = new String[]{"门店名称", "门店手机号", "保险公司名称", "车牌号", "商业险保单号", "商业险保费", "商业险生效日期",
                "交强险保单号","交强险保费","交强险生效日期","车船税","录入时间","审核状态","审核人"};
        String[] fields = new String[]{"agentName", "agentMobile", "insuranceCompanyName", "vehicleSn", "commercialFormNo",
                "commercialFee", "gmtCommercialStart","forcibleFormNo","forcibleFee","gmtForcibleStart","vesselTaxFee","gmtCreate","auditStatusName","auditName"};

        try {
            poiUtil.exportXlsxToClient(response, "录入保单审核列表", heads, fields, tempInfoBOList);
        } catch (Exception e) {
            log.error("export send log excel error", e);
        }
    }

    private List<InsuranceOfflineTempInfoBO> setCompanyInfo(List<InsuranceOfflineTempInfoDTO> tempInfoDTOList){
        List<InsuranceCompanyBO> companyList = insuranceBiz.getAllCompanyList();
        if(CollectionUtils.isEmpty(companyList)){
            return Lists.newArrayList();
        }
        Map<Integer,InsuranceCompanyBO> companyMap = Maps.uniqueIndex(companyList, new Function<InsuranceCompanyBO, Integer>() {
            @Override
            public Integer apply(InsuranceCompanyBO insuranceCompanyBO) {
                return insuranceCompanyBO.getId();
            }
        });
        List<InsuranceOfflineTempInfoBO> tempInfoBOList = BdUtil.do2bo4List(tempInfoDTOList,InsuranceOfflineTempInfoBO.class);
        for (InsuranceOfflineTempInfoBO tempInfoBO :tempInfoBOList) {
            Integer companyId = tempInfoBO.getInsuranceCompanyId();
            boolean contain = companyMap.containsKey(companyId);
            Integer auditStatus = tempInfoBO.getAuditStatus();
            if(contain && auditStatus.equals(OffLineAuditStatusEnum.SHTG.getCode())){
                String companyName = companyMap.get(companyId).getCompanyName();
                tempInfoBO.setInsuranceCompanyName(companyName);
            }
            tempInfoBO.setAuditStatusName(InsuranceOffLineAuditStatusEnum.codeDescription(auditStatus));
        }

        return tempInfoBOList;
    }

    private InsuranceSettleBasicBO packageSettleData(InsuranceOffLineTempInfoVO tempInfoVO) {

        InsuranceSettleBasicBO settleBasicBO = BdUtil.do2bo(tempInfoVO,InsuranceSettleBasicBO.class);
        settleBasicBO.setCooperationModeId(CooperationModeEnum.GIVE_REWARD.getCode());
        List<InsuranceSettleFormBO> list = Lists.newArrayList();
        String commercialFormNo = tempInfoVO.getCommercialFormNo();
        String forcibleFormNo = tempInfoVO.getForcibleFormNo();
        if(!StringUtils.isEmpty(commercialFormNo)){
            InsuranceSettleFormBO settleFormBO = new InsuranceSettleFormBO();
            settleFormBO.setBillSignTime(tempInfoVO.getGmtPay());
            settleFormBO.setInsuranceTypeId(InsuranceTypeEnum.BIZ_INSURANCE.getCode());
            settleFormBO.setInsuredFee(tempInfoVO.getCommercialFee());
            settleFormBO.setInsuredFormNo(commercialFormNo);
            settleFormBO.setInsuredStartTime(tempInfoVO.getGmtCommercialStart());
            list.add(settleFormBO);
        }

        if(!StringUtils.isEmpty(forcibleFormNo)){
            InsuranceSettleFormBO settleFormBO = new InsuranceSettleFormBO();
            settleFormBO.setBillSignTime(tempInfoVO.getGmtPay());
            settleFormBO.setInsuranceTypeId(InsuranceTypeEnum.FORCE_INSURANCE.getCode());
            BigDecimal totalFee = tempInfoVO.getForcibleFee().add(tempInfoVO.getVesselTaxFee());
            settleFormBO.setInsuredFee(totalFee);
            settleFormBO.setInsuredFormNo(forcibleFormNo);
            settleFormBO.setInsuredStartTime(tempInfoVO.getGmtForcibleStart());
            settleFormBO.setTax(tempInfoVO.getVesselTaxFee());
            list.add(settleFormBO);
        }

        settleBasicBO.setFormBOList(list);
        return settleBasicBO;
    }

}
