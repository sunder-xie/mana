package com.tqmall.mana.biz.manager.common;

import com.tqmall.mana.beans.VO.CarBrandSelectVO;

/**
 * Created by huangzhangting on 16/12/23.
 */
public interface CommonBiz {

    /**
     * 车品牌选择，包含热门品牌
     *
     * @return
     */
    CarBrandSelectVO getCarBrandSelectVO();
}
