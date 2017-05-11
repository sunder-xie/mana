package com.tqmall.mana.client.service.settle;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.client.beans.settle.SettleShopRuleIntroductionDTO;

import java.util.List;

/**
 * Created by zxg on 17/2/15.
 * 09:58
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public interface RpcSettleShopRuleIntroductionService {

    // 获得所有规则说明数据
    Result<List<SettleShopRuleIntroductionDTO>> getIntroductionList();
}
