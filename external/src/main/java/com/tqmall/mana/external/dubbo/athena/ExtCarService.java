package com.tqmall.mana.external.dubbo.athena;

import com.tqmall.athena.domain.result.carcategory.CarCategoryDTO;
import com.tqmall.athena.domain.result.carcategory.HotCarBrandDTO;

import java.util.List;

/**
 * Created by huangzhangting on 16/12/14.
 */
public interface ExtCarService {

    List<CarCategoryDTO> getCarListByPid(Integer pid);

    List<HotCarBrandDTO> getHotCarBrandList();

}
