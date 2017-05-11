package com.tqmall.mana.server.settle;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.settle.SettleShopRuleIntroductionBO;
import com.tqmall.mana.biz.manager.settle.config.SettleShopRuleIntroductionBiz;
import com.tqmall.mana.client.beans.settle.SettleShopRuleIntroductionDTO;
import com.tqmall.mana.client.service.settle.RpcSettleShopRuleIntroductionService;
import com.tqmall.mana.component.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by zxg on 17/2/15.
 * 10:25
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class RpcSettleShopRuleIntroductionServiceImpl implements RpcSettleShopRuleIntroductionService {

    @Autowired
    private SettleShopRuleIntroductionBiz settleShopRuleIntroductionBiz;

    @Override
    public Result<List<SettleShopRuleIntroductionDTO>> getIntroductionList() {
        Result<List<SettleShopRuleIntroductionBO>> ruleIntroductionList = settleShopRuleIntroductionBiz.getRuleIntroductionList();

        return ResultUtil.handleResultList(ruleIntroductionList, SettleShopRuleIntroductionDTO.class);
    }
}
