package com.tqmall.mana.server.settle;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.entity.settle.SettleInsuranceMaterialAllowanceDO;
import com.tqmall.mana.biz.manager.settle.checkDetail.SettleShopCheckDetailBiz;
import com.tqmall.mana.client.beans.otherInsurance.CheckAllowanceDTO;
import com.tqmall.mana.client.beans.otherInsurance.InsuranceMaterialAllowanceDTO;
import com.tqmall.mana.client.beans.param.CheckAllowanceListRequestParam;
import com.tqmall.mana.client.beans.param.RecordAllowancePaymentInfoParam;
import com.tqmall.mana.client.service.settle.RpcSettleShopCheckDetailService;
import com.tqmall.mana.component.enums.insurance.dict.AgentTagEnum;
import com.tqmall.mana.component.enums.insurance.dict.AllowanceStatusEnum;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.dao.mapper.settle.SettleShopCheckDetailDOMapper;
import com.tqmall.mana.dao.mapper.settle.SettleShopCheckDetailExtendDOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zwb on 17/2/20.
 */
public class RpcSettleShopCheckDetailServiceImpl implements RpcSettleShopCheckDetailService {
    @Autowired
    private SettleShopCheckDetailBiz settleShopCheckDetailBiz;
    @Autowired
    private SettleShopCheckDetailDOMapper settleShopCheckDetailDOMapper;
    @Autowired
    private SettleShopCheckDetailExtendDOMapper settleShopCheckDetailExtendDOMapper;

    @Override
    public Result<Map<Integer, String>> showAllowanceAgentName(String agentName) {
        Assert.notNull(agentName, "参数不能为空");
        return Result.wrapSuccessfulResult(settleShopCheckDetailBiz.showAllowanceAgentName(agentName));
    }

    @Override
    public Result<Map<Integer, String>> showAllowanceStatus() {
        Map<Integer, String> map = Maps.newHashMap();
        map.put(AllowanceStatusEnum.WSQ.getCode(), AllowanceStatusEnum.WSQ.getDesc());
        map.put(AllowanceStatusEnum.YSQ.getCode(), AllowanceStatusEnum.YSQ.getDesc());
        map.put(AllowanceStatusEnum.YSH.getCode(), AllowanceStatusEnum.YSH.getDesc());
        map.put(AllowanceStatusEnum.YFK.getCode(), AllowanceStatusEnum.YFK.getDesc());
        return Result.wrapSuccessfulResult(map);
    }

    @Override
    public Result<Map<String, Integer>> getAllowanceStatusByOrderSn(List<String> orderSn) {
        if (CollectionUtils.isEmpty(orderSn)) {
            return Result.wrapErrorResult("0001", "参数不能为空");
        }
        return Result.wrapSuccessfulResult(settleShopCheckDetailBiz.getAllowanceStatusByOrderSn(orderSn));
    }

    @Override
    public Result<CheckAllowanceDTO> checkAllowanceList(CheckAllowanceListRequestParam requestParam) {
        if (requestParam == null) {
            return Result.wrapErrorResult("0001", "参数不能为空");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("gmtPackageValidStart", requestParam.getGmtPackageValidStart());
        params.put("gmtPackageValidEnd", requestParam.getGmtPackageValidEnd());
        params.put("shopName", requestParam.getShopName());
        params.put("whsName", requestParam.getWhsName());
        params.put("orderSn", requestParam.getOrderSn());
        params.put("allowanceStatus", requestParam.getAllowanceStatus());
        params.put("agentTag", requestParam.getAgentTag());
        //一页最多500条数据
        if (requestParam.getPageSize() > 500) {
            requestParam.setPageSize(500);
        }
        CheckAllowanceDTO checkAllowanceDTO = new CheckAllowanceDTO();
        Integer count = settleShopCheckDetailDOMapper.checkAllowanceListCount(params);
        List<InsuranceMaterialAllowanceDTO> allowanceDTOs = Lists.newArrayList();
        if (count == 0) {
            checkAllowanceDTO.setPayableAmountCount(BigDecimal.ZERO);
            checkAllowanceDTO.setAllowanceDTOPagingResult(PagingResult.wrapSuccessfulResult(allowanceDTOs, 0));
            return Result.wrapSuccessfulResult(checkAllowanceDTO);
        }
        BigDecimal payableAmountCount = settleShopCheckDetailDOMapper.checkPayableAmountCount(params);
        params.put("startLimit", (requestParam.getPageNum() - 1) * requestParam.getPageSize());
        params.put("endLimit", requestParam.getPageSize());
        List<SettleInsuranceMaterialAllowanceDO> allowanceDOs = settleShopCheckDetailDOMapper.checkAllowanceList(params);
        for(SettleInsuranceMaterialAllowanceDO allowanceDO : allowanceDOs){
            InsuranceMaterialAllowanceDTO allowanceDTO = BdUtil.do2bo(allowanceDO, InsuranceMaterialAllowanceDTO.class);
            allowanceDTO.setAgentTagName(AgentTagEnum.codeDescription(allowanceDO.getAgentTag()));
            allowanceDTOs.add(allowanceDTO);
        }

        checkAllowanceDTO.setPayableAmountCount(payableAmountCount);
        checkAllowanceDTO.setAllowanceDTOPagingResult(PagingResult.wrapSuccessfulResult(allowanceDTOs, count));
        return Result.wrapSuccessfulResult(checkAllowanceDTO);
    }

    @Override
    @Transactional
    public Result<Boolean> updateAllowanceStatus(RecordAllowancePaymentInfoParam param) {
        if (param == null || param.getListOrderSn() == null ||
                param.getListOrderSn().size() <= 0 || param.getAllowanceStatus() == null
                || param.getModifier() == null) {
            return Result.wrapErrorResult("0001", "参数不能为空");
        }
        Boolean result = false;
        result = modify(param);
        return Result.wrapSuccessfulResult(result);
    }

    private boolean modify(RecordAllowancePaymentInfoParam param) {
        List<String> listOrderSn = param.getListOrderSn();
        List<SettleInsuranceMaterialAllowanceDO> list = settleShopCheckDetailDOMapper.selectByOrderSnList(listOrderSn);
        for (SettleInsuranceMaterialAllowanceDO materia : list) {
            if (param.getAllowanceStatus().compareTo(materia.getAllowanceStatus()) == 0) {
                return false;
            }
            SettleInsuranceMaterialAllowanceDO allowanceDO = new SettleInsuranceMaterialAllowanceDO();
            allowanceDO.setId(materia.getId());
            //已审核
            if (param.getAllowanceStatus().compareTo(AllowanceStatusEnum.YSH.getCode()) == 0) {
                allowanceDO.setGmtAudit(new Date());
                allowanceDO.setAuditModifier(param.getModifier());
            }
            //已支付
            if (param.getAllowanceStatus().compareTo(AllowanceStatusEnum.YFK.getCode()) == 0) {
                allowanceDO.setSettleFeeStatus(2);
                allowanceDO.setSettlePeopleName(param.getModifier());
                allowanceDO.setSettleTime(new Date());
            }

            if (param.getAllowanceStatus().compareTo(AllowanceStatusEnum.YSQ.getCode()) == 0) {
                allowanceDO.setGmtApplid(new Date());
                allowanceDO.setApplidModifier(param.getModifier());
            }
            allowanceDO.setGmtModified(new Date());
            allowanceDO.setAllowanceStatus(param.getAllowanceStatus());
            allowanceDO.setId(materia.getId());
            allowanceDO.setGmtModified(new Date());
            settleShopCheckDetailExtendDOMapper.updateDetailExtend(allowanceDO);
            settleShopCheckDetailDOMapper.updateDetail(allowanceDO);
        }


        return true;
    }


}
