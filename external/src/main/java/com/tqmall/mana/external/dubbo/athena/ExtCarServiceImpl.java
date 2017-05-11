package com.tqmall.mana.external.dubbo.athena;

import com.tqmall.athena.client.car.CarCategoryService;
import com.tqmall.athena.domain.result.carcategory.CarCategoryDTO;
import com.tqmall.athena.domain.result.carcategory.HotCarBrandDTO;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.component.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhangting on 16/12/14.
 */
@Slf4j
@Service
public class ExtCarServiceImpl implements ExtCarService {
    @Value("${default.city.id}")
    private Integer defaultCityId;
    @Autowired
    private CarCategoryService carCategoryService;


    @Override
    public List<CarCategoryDTO> getCarListByPid(Integer pid) {
        if(pid==null || pid<0){
            pid = 0;
        }

        Result<List<CarCategoryDTO>> result = carCategoryService.getCarCategoryByPid(pid);
        if(result.isSuccess() && !CollectionUtils.isEmpty(result.getData())){
            return result.getData();
        }
        log.info("getCarCategoryByPid failed, pid:{}, result:{}", pid, JsonUtil.objectToStr(result));

        return new ArrayList<>();
    }

    @Override
    public List<HotCarBrandDTO> getHotCarBrandList() {
        Result<List<HotCarBrandDTO>> result = carCategoryService.getHotCarBrandList(defaultCityId);
        if(result.isSuccess() && !CollectionUtils.isEmpty(result.getData())){
            return result.getData();
        }
        log.info("getHotCarBrandList failed, defaultCityId:{}, result:{}", defaultCityId, JsonUtil.objectToStr(result));
        return new ArrayList<>();
    }
}
