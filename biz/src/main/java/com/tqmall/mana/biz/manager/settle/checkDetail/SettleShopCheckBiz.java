package com.tqmall.mana.biz.manager.settle.checkDetail;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.mana.beans.BO.settle.ShopSettleDetailPackageModeBO;
import com.tqmall.mana.beans.BO.settle.ShopSettleDetailRewardModeBO;
import com.tqmall.mana.beans.param.settle.BalanceStatusModifyPO;
import com.tqmall.mana.beans.param.settle.SettleFeeModifyPO;
import com.tqmall.mana.beans.param.settle.ShopSettleDetailQueryPO;
import com.tqmall.mana.beans.param.settle.UseCashCouponPO;

/**
 * Created by zxg on 17/1/17.
 * 19:50
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public interface SettleShopCheckBiz {

    /**
     * 门店对账明细查询，奖励金模式
     * @param param
     * @return
     */
    PagingResult<ShopSettleDetailRewardModeBO> shopSettleDetailRewardModePage(ShopSettleDetailQueryPO param);

    /**
     * 门店对账明细查询，服务包模式
     * @param param
     * @return
     */
    PagingResult<ShopSettleDetailPackageModeBO> shopSettleDetailPackageModePage(ShopSettleDetailQueryPO param);

    /**
     * 修改对账状态
     * @param modifyPO
     * @return
     */
    boolean modifyBalanceStatus(BalanceStatusModifyPO modifyPO);

    /**
     * 撤销使用现金券
     * @param modifyPO
     * @return
     */
    boolean unUseCashCoupon(SettleFeeModifyPO modifyPO);

    /**
     * 使用现金券
     * @param useCashCouponPO
     * @return
     */
    boolean useCashCoupon(UseCashCouponPO useCashCouponPO);

}
