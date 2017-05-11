package com.tqmall.mana.biz.manager.coupon;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.cashcoupon.CashCouponRegionConfigBO;
import com.tqmall.mana.beans.BO.cashcoupon.CashCouponRuleConfigBO;
import com.tqmall.mana.beans.param.settle.CashCouponConfigSearchParam;
import com.tqmall.mana.beans.param.settle.CashCouponRuleConfigPO;
import com.tqmall.tqmallstall.domain.result.RegionListDTO;

import java.util.List;

/**
 * Created by zhanghong on 17/4/10.
 */
public interface CashCouponRuleConfigBiz {
    /**
     * 新增现金券规则
     *
     * @param ruleConfigBizParam
     */
    boolean createCashCouponRuleConfig(CashCouponRuleConfigPO ruleConfigBizParam);

    /**
     * 编辑现金券规则
     *
     * @param ruleConfigBizParam
     */
    boolean updateCashCouponRuleConfig(CashCouponRuleConfigPO ruleConfigBizParam);

    /**
     * 分页查询
     *
     * @param searchParam
     * @return
     */
    PagingResult<CashCouponRegionConfigBO> getRegionConfigPageList(CashCouponConfigSearchParam searchParam);


    /**
     * 获取所有开放地区
     *
     * @return
     */
    Result<List<RegionListDTO>> getAllOpenedRegionList();


    /**
     * 根据城市编码 查询规则配置信息
     *
     * @param cityCode
     * @return
     */
    CashCouponRuleConfigBO getConfigInfoByCityCode(String cityCode);

    /**
     * 根据城市站获取对应的配置规则信息
     *
     * @param cityCode
     * @return
     */
    CashCouponRuleConfigBO getCreateRuleConfigInfo(String cityCode);

    /**
     * 通过ID获取配置信息
     *
     * @param id
     * @return
     */
    CashCouponRuleConfigPO getConfigInfoById(Integer id);

    /**
     * 删除
     *
     * @param regionConfigId
     */
    void deleteConfigInfo(Integer regionConfigId);


}
