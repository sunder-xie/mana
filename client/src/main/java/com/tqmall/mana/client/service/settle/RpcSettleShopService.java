package com.tqmall.mana.client.service.settle;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.client.beans.param.*;
import com.tqmall.mana.client.beans.settle.SettleShopDTO;
import com.tqmall.mana.client.beans.settle.ShopSettleDetailPackageModeDTO;
import com.tqmall.mana.client.beans.settle.ShopSettleDetailRewardModeDTO;

/**
 * Created by zxg on 17/1/17.
 * 19:41
 * no bug,以后改代码的哥们，祝你好运~！！
 * 保险对账门店interface
 */
public interface RpcSettleShopService {

    /**
     * 获得门店对账的基础数据
     * @param shopId 门店的唯一标识
     * @return 对账的基础数据
     */
    Result<SettleShopDTO> getSettleShopDetailByShopId(Integer shopId);

    /**
     * 门店对账明细查询，奖励金模式
     * @param param
     * @return
     */
    PagingResult<ShopSettleDetailRewardModeDTO> shopSettleDetailRewardModePage(ShopSettleDetailRewardModeQueryParam param);

    /**
     * 门店对账明细查询，服务包模式
     * @param param
     * @return
     */
    PagingResult<ShopSettleDetailPackageModeDTO> shopSettleDetailPackageModePage(ShopSettleDetailPackageModeQueryParam param);

    /**
     * 修改对账状态
     * @param param
     * @return
     */
    Result modifyBalanceStatus(BalanceStatusModifyParam param);

}
