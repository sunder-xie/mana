package com.tqmall.mana.biz.manager.common;

import com.tqmall.athena.domain.result.carcategory.CarCategoryDTO;
import com.tqmall.athena.domain.result.carcategory.HotCarBrandDTO;
import com.tqmall.mana.beans.VO.CarBrandSelectVO;
import com.tqmall.mana.beans.VO.CarBrandVO;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.ImgUtil;
import com.tqmall.mana.external.dubbo.athena.ExtCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by huangzhangting on 16/12/23.
 */
@Service
public class CommonBizImpl implements CommonBiz {
    @Autowired
    private ExtCarService extCarService;

    @Override
    public CarBrandSelectVO getCarBrandSelectVO() {
        Map<String, List<CarBrandVO>> dataMap = new HashMap<>();
        List<String> firstWordList = new ArrayList<>();

        List<CarCategoryDTO> carBrandList = extCarService.getCarListByPid(0);
        for (CarCategoryDTO car : carBrandList) {
            String firstWord = car.getFirstWord();
            List<CarBrandVO> list = dataMap.get(firstWord);
            if (list == null) {
                list = new ArrayList<>();
                dataMap.put(firstWord, list);
            }
            CarBrandVO carBrandVO = BdUtil.do2bo(car, CarBrandVO.class);
            carBrandVO.setLogo(ImgUtil.getFullPath(carBrandVO.getLogo()));
            list.add(carBrandVO);
        }

        firstWordList.addAll(dataMap.keySet());
        Collections.sort(firstWordList);

        List<HotCarBrandDTO> hotCarBrandDTOs = extCarService.getHotCarBrandList();
        List<CarBrandVO> hotCarBrands = new ArrayList<>();
        for (HotCarBrandDTO hotCarBrandDTO : hotCarBrandDTOs) {
            CarBrandVO carBrandVO = new CarBrandVO();
            carBrandVO.setId(Integer.valueOf(hotCarBrandDTO.getId()));
            carBrandVO.setName(hotCarBrandDTO.getName());
            carBrandVO.setLogo(ImgUtil.getFullPath(hotCarBrandDTO.getLogo()));

            hotCarBrands.add(carBrandVO);
        }

        dataMap.put("常用", hotCarBrands);
        firstWordList.add(0, "常用");

        CarBrandSelectVO carBrandSelectVO = new CarBrandSelectVO();
        carBrandSelectVO.setFirstWordList(firstWordList);
        carBrandSelectVO.setDataMap(dataMap);

        return carBrandSelectVO;
    }
}
