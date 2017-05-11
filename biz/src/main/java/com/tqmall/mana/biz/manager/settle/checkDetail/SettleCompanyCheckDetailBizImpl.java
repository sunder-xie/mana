package com.tqmall.mana.biz.manager.settle.checkDetail;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.mana.beans.BO.settle.SettleCompanyAmountSumBO;
import com.tqmall.mana.beans.BO.settle.SettleFormBO;
import com.tqmall.mana.beans.VO.settle.SettleCompanyBizPagingResult;
import com.tqmall.mana.beans.VO.settle.SettleCompanyCheckDetailVO;
import com.tqmall.mana.beans.entity.settle.SettleCompanyCheckDetailDO;
import com.tqmall.mana.beans.entity.settle.SettleShopCheckDetailExtendDO;
import com.tqmall.mana.beans.entity.settle.extend.InsuranceCompanySettleDetailDO;
import com.tqmall.mana.beans.entity.settle.extend.ShopSettleDetailForCashBackDO;
import com.tqmall.mana.beans.param.settle.finance.CashBackPO;
import com.tqmall.mana.beans.param.settle.SettleCompanyCheckDetailBizParam;
import com.tqmall.mana.beans.param.settle.SettleShopCheckDetailQueryPO;
import com.tqmall.mana.component.enums.insurance.dict.*;
import com.tqmall.mana.component.util.BeanUtil;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.dao.mapper.settle.SettleCompanyCheckDetailDOMapper;
import com.tqmall.mana.dao.mapper.settle.SettleFormDOMapper;
import com.tqmall.mana.dao.mapper.settle.SettleShopCheckDetailDOMapper;
import com.tqmall.mana.dao.mapper.settle.SettleShopCheckDetailExtendDOMapper;
import com.tqmall.mana.external.dubbo.finance.ExtFcInsuranceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Created by zengjinju on 17/1/21.
 */
@Service
@Slf4j
public class SettleCompanyCheckDetailBizImpl implements SettleCompanyCheckDetailBiz {

    @Autowired
    private SettleFormDOMapper settleFormDOMapper;
    @Autowired
    private SettleCompanyCheckDetailDOMapper settleCompanyCheckDetailDOMapper;
    @Autowired
    private SettleShopCheckDetailDOMapper settleShopCheckDetailDOMapper;

    @Autowired
    private ExtFcInsuranceService extFcInsuranceService;
    @Autowired
    private SettleShopCheckDetailExtendDOMapper shopCheckDetailExtendDOMapper;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;


    @Override
    public SettleCompanyBizPagingResult insuranceCompanyCheckForERP(SettleCompanyCheckDetailBizParam param) {
        SettleCompanyBizPagingResult pagingResult = new SettleCompanyBizPagingResult();
        Map<String, Object> searchParam = BeanUtil.param2Map(param);
        Integer totalCount = settleFormDOMapper.getCountByCondition(searchParam);
        if (totalCount == 0) {
            log.info("根据查询条件param{}" + JsonUtil.objectToStr(param) + "获取的数据不存在");
            pagingResult.setPagingResult(PagingResult.wrapSuccessfulResult(new ArrayList<SettleCompanyCheckDetailVO>(), totalCount));
            return pagingResult;
        }
        searchParam.put("start", (param.getPage() - 1) * param.getPageSize());
        searchParam.put("offset", param.getPageSize());
        List<SettleCompanyCheckDetailVO> content = getCompanyCheckDetailList(searchParam);
        pagingResult.setPagingResult(PagingResult.wrapSuccessfulResult(content, totalCount));
        //计算保费总和
        SettleCompanyAmountSumBO sumBO = settleFormDOMapper.sumInsuredFee(searchParam);
        pagingResult.setTotalInsuredFee(sumBO.getTotalInsuredFee());
        pagingResult.setTotalInsuredRoyaltyFee(sumBO.getTotalInsuredRoyaltyFee());
        return pagingResult;
    }

    private List<SettleCompanyCheckDetailVO> getCompanyCheckDetailList(Map<String, Object> searchParam) {
        List<SettleCompanyCheckDetailVO> content = new ArrayList<>();
        List<SettleFormBO> companyCheckDetailDOList = settleFormDOMapper.getSettleFormListForPage(searchParam);
        for (SettleFormBO formBO : companyCheckDetailDOList) {
            SettleCompanyCheckDetailVO companyCheckDetailVO = new SettleCompanyCheckDetailVO();
            BeanUtils.copyProperties(formBO, companyCheckDetailVO);
            companyCheckDetailVO.setAgentTagName(AgentTagEnum.codeDescription(formBO.getAgentTag()));
            companyCheckDetailVO.setScenarioTypeName(ScenarioTypeEnum.codeDescription(formBO.getScenarioTypeId()));
            content.add(companyCheckDetailVO);
        }
        return content;
    }

    @Transactional
    @Override
    public Boolean updateConfirmMoneyStatusByIds(List<Integer> idList, String operator) {
        Assert.hasLength(operator, "操作人不能为空");
        Assert.notEmpty(idList, "数据id集合不能为空");

        List<InsuranceCompanySettleDetailDO> detailDOList = settleCompanyCheckDetailDOMapper.selectByIdsForConfirmMoney(idList);
        Assert.notEmpty(detailDOList, "未查到数据");

        Map<Integer, Set<String>> formSnMap = new HashMap<>();
        Date now = new Date();
        for (InsuranceCompanySettleDetailDO detailDO : detailDOList) {
            Integer id = detailDO.getId();
            Integer confirmMoneyStatus = detailDO.getConfirmMoneyStatus();

            Assert.isTrue(ConfirmMoneyStatusEnum.NOT_CONFIRM.getCode().equals(confirmMoneyStatus),
                    "确认收款失败，非法状态：" + confirmMoneyStatus + "，数据id：" + id);

            SettleCompanyCheckDetailDO updateDetail = new SettleCompanyCheckDetailDO();
            updateDetail.setId(id);
            updateDetail.setGmtModified(now);
            updateDetail.setModifier(operator);
            updateDetail.setConfirmMoneyTime(now);
            updateDetail.setConfirmMoneyPeopleName(operator);
            updateDetail.setConfirmMoneyStatus(ConfirmMoneyStatusEnum.HAS_CONFIRMED.getCode());
            settleCompanyCheckDetailDOMapper.updateByPrimaryKeySelective(updateDetail);

            //奖励金模式
            if (CooperationModeEnum.GIVE_REWARD.getCode().equals(detailDO.getCooperationModeId())) {
                Integer companyId = detailDO.getInsuranceCompanyId();
                Set<String> formSnSet = formSnMap.get(companyId);
                if(formSnSet==null){
                    formSnSet = new HashSet<>();
                    formSnMap.put(companyId, formSnSet);
                }
                formSnSet.add(detailDO.getSettleFormSn());
            }
        }

        /** 处理奖励金模式返现金数据 */
        handleRewardData(formSnMap, operator);

        return true;
    }

    /**
     * 处理奖励金模式返现金数据
     */
    private void handleRewardData(Map<Integer, Set<String>> formSnMap, String operator) {
        if (formSnMap.isEmpty()) {
            return;
        }
        List<ShopSettleDetailForCashBackDO> allDetailDOList = new ArrayList<>();
        SettleShopCheckDetailQueryPO queryPO = new SettleShopCheckDetailQueryPO();
        for(Map.Entry<Integer, Set<String>> entry : formSnMap.entrySet()) {
            queryPO.setInsuranceCompanyId(entry.getKey());
            queryPO.setBizSnList(new ArrayList<>(entry.getValue()));
            List<ShopSettleDetailForCashBackDO> detailDOList = settleShopCheckDetailDOMapper.selectForCashBack(queryPO);
            if (detailDOList.isEmpty()) {
                log.info("handleRewardData failed, no data, companyId:{}, formSnSet:{}", entry.getKey(), entry.getValue());
                continue;
            }
            allDetailDOList.addAll(detailDOList);
        }

        for (ShopSettleDetailForCashBackDO detailDO : allDetailDOList) {
            if (SettleStatusEnum.HAS_PAID.getCode().equals(detailDO.getSettleFeeStatus())) { //排除已支付（已结算）的数据
                continue;
            }
            //奖励金生效
            rewardEffect(detailDO, operator);
        }
    }

    /**
     * 奖励金生效
     */
    private void rewardEffect(final ShopSettleDetailForCashBackDO detailDO, String operator) {
        //修改奖励金状态
        Integer shopCheckDetailId = detailDO.getId();
        SettleShopCheckDetailExtendDO detailExtendDO = shopCheckDetailExtendDOMapper.selectByShopCheckDetailId(shopCheckDetailId);
        if(detailExtendDO==null){
            log.error("reward effect failed, data not exist, shopCheckDetailId="+shopCheckDetailId);
        }else{
            if(RewardStatusEnum.NOT_EFFECT.getCode().equals(detailExtendDO.getRewardStatus())) {
                SettleShopCheckDetailExtendDO updateDO = new SettleShopCheckDetailExtendDO();
                updateDO.setId(detailExtendDO.getId());
                updateDO.setGmtModified(new Date());
                updateDO.setModifier(operator);
                updateDO.setRewardEffectTime(updateDO.getGmtModified());
                updateDO.setRewardStatus(RewardStatusEnum.HAS_EFFECTED.getCode());
                shopCheckDetailExtendDOMapper.updateByPrimaryKeySelective(updateDO);
            }else{
                log.error("reward effect failed, reward status is error, shopCheckDetailId="+shopCheckDetailId);
            }
        }

        //确认收款后，通知finance奖励金生效
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                cashBack(detailDO);
            }
        });
    }

    /**
     * 确认收款后，通知finance奖励金生效
     */
    private void cashBack(ShopSettleDetailForCashBackDO detailDO) {
        CashBackPO cashBackPO = new CashBackPO();
        cashBackPO.setShopId(detailDO.getAgentId());
        cashBackPO.setCooperationMode(CooperationModeEnum.GIVE_REWARD.getCode());
        cashBackPO.setReward(detailDO.getSettleFee());
        cashBackPO.setInsuranceNo(detailDO.getInsuredFormNo());
        cashBackPO.setInsuranceType(detailDO.getInsuranceTypeId());

        List<String> formNoList = new ArrayList<>();
        formNoList.add(detailDO.getInsuredFormNo());
        List<SettleCompanyCheckDetailDO> companyCheckDetailDOList =
                settleCompanyCheckDetailDOMapper.selectBySettleFormSnList(formNoList, detailDO.getInsuranceCompanyId());
        if(!companyCheckDetailDOList.isEmpty()){
            SettleCompanyCheckDetailDO companyCheckDetailDO = companyCheckDetailDOList.get(0);
            cashBackPO.setCharges(companyCheckDetailDO.getInsuredRoyaltyFee());
        }

        extFcInsuranceService.cashBack(cashBackPO);
    }


    @Override
    public List<SettleCompanyCheckDetailVO> exportCompanyCheckDetailList(SettleCompanyCheckDetailBizParam param) {
        Assert.notNull(param, "SettleCompanyCheckDetailBizParam不能为空");
        Map<String, Object> searchParam = BeanUtil.param2Map(param);
        Integer totalCount = settleFormDOMapper.getCountByCondition(searchParam);
        List<SettleCompanyCheckDetailVO> content = new ArrayList<>();
        if (totalCount == 0) {
            log.info("根据查询条件param{}" + JsonUtil.objectToStr(param) + "获取的数据不存在");
            return content;
        }
        int totalPages = (int) Math.ceil(totalCount.doubleValue() / (double) param.getPageSize());
        for (int i = 1; i <= totalPages; i++) {
            searchParam.put("start", (i - 1) * param.getPageSize());
            searchParam.put("offset", param.getPageSize());
            List<SettleCompanyCheckDetailVO> list = getCompanyCheckDetailList(searchParam);
            content.addAll(list);
        }
        return content;
    }

}
