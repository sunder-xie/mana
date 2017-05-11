package com.tqmall.mana.external.dubbo.stall;

import com.tqmall.tqmallstall.domain.result.RegionDTO;
import com.tqmall.tqmallstall.domain.result.RegionListDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by huangzhangting on 16/12/14.
 */
public interface ExtRegionService {

    List<RegionDTO> getRegionByPid(Integer pid);

    Map<Integer, String> getRegionNameMap(List<Integer> regionIds);

    List<RegionListDTO> getAllOpenedRegionList();

}
