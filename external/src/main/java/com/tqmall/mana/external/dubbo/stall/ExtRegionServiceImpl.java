package com.tqmall.mana.external.dubbo.stall;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.component.annotation.cache.ManaCache;
import com.tqmall.mana.component.redis.RedisKeyBean;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.tqmallstall.domain.result.RegionDTO;
import com.tqmall.tqmallstall.domain.result.RegionListDTO;
import com.tqmall.tqmallstall.service.common.AppRegionService;
import com.tqmall.tqmallstall.service.region.RpcRegionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhangting on 16/12/14.
 */
@Slf4j
@Service
public class ExtRegionServiceImpl implements ExtRegionService {
    private static final Integer CHINA_ID = 1;

    @Autowired
    private AppRegionService appRegionService;
    @Autowired
    private RpcRegionService rpcRegionService;


    @Override
    public List<RegionDTO> getRegionByPid(Integer pid) {
        if(pid==null || pid<1){
            pid = CHINA_ID;
        }
        Result<List<RegionDTO>> result = appRegionService.subRegion(pid);
        if(result.isSuccess() && !CollectionUtils.isEmpty(result.getData())){
            return result.getData();
        }
        log.info("get sub region failed, pid:{}, result:{}", pid, JsonUtil.objectToStr(result));

        return new ArrayList<>();
    }

    @Override
    public Map<Integer, String> getRegionNameMap(List<Integer> regionIds) {
        if(CollectionUtils.isEmpty(regionIds)){
            return null;
        }

        Result<Map<Integer, String>> result = appRegionService.getRegionNameMap(regionIds);
        if(result.isSuccess() && !CollectionUtils.isEmpty(result.getData())){
            return result.getData();
        }
        log.info("getRegionNameMap failed, regionIds:{}, result:{}", regionIds.toString(), JsonUtil.objectToStr(result));

        return null;
    }

    @Override
    @ManaCache(cacheName = RedisKeyBean.CASHCOUPON_OPENED_REGION_KEY)
    public List<RegionListDTO> getAllOpenedRegionList() {
        log.info("调用stall接口开始,获取区域列表");
        Result<List<RegionListDTO>> regionListResult = rpcRegionService.getAllOpenedRegionList();
        log.info("调用stall接口结束,获取区域列表");
        if(regionListResult.isSuccess() && !CollectionUtils.isEmpty(regionListResult.getData())){
            return regionListResult.getData();
        }
        return null;
    }
}
