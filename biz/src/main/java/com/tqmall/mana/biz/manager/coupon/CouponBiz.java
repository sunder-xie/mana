package com.tqmall.mana.biz.manager.coupon;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.coupon.SearchCouponSendLogBO;
import com.tqmall.mana.beans.BO.coupon.SendCouponBO;
import com.tqmall.mana.beans.entity.coupon.ManaCouponSendLogDO;
import com.tqmall.mana.beans.entity.coupon.ManaCouponTypeDO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * Created by huangzhangting on 16/12/6.
 */
public interface CouponBiz {

    Result sendCoupon(SendCouponBO sendCouponBO);

    List<ManaCouponTypeDO> getAllTypes();

    PagingResult<ManaCouponSendLogDO> getSendLogPage(SearchCouponSendLogBO searchCouponSendLogBO);

    void exportSendLog(HttpServletResponse response, SearchCouponSendLogBO searchCouponSendLogBO);
}
