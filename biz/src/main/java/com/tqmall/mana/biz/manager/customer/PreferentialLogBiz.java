package com.tqmall.mana.biz.manager.customer;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.customer.AddPreferentialLogBO;
import com.tqmall.mana.beans.BO.customer.AddPreferentialVO;
import com.tqmall.mana.beans.param.PreferentialVOPageParam;

/**
 * 优惠记录biz
 * <p>
 * Created by huangzhangting on 17/1/3.
 */
public interface PreferentialLogBiz {

    /**
     * 通过车辆id获取车辆优惠信息列表
     *
     * @param preferentialVOPageParam
     * @return
     */
    PagingResult<AddPreferentialLogBO> getPreferentialBOList(PreferentialVOPageParam preferentialVOPageParam);

    /**
     * 新增优惠记录
     *
     * @param addPreferentialLogBO
     * @return
     */
    Result addPreferentialLog(AddPreferentialLogBO addPreferentialLogBO);

    /**
     * 新增客户优惠记录lie
     *
     * @param addPreferentialVO
     * @return
     */
    Result addPreferentialLogList(AddPreferentialVO addPreferentialVO);

}
