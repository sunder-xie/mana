package com.tqmall.mana.external.dubbo.insurance;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.insurance.domain.param.insurance.QueryInsuranceShopParam;
import com.tqmall.insurance.domain.result.InsuranceShopConfigDTO;
import com.tqmall.insurance.service.insurance.RpcInsuranceShopConfigService;
import com.tqmall.mana.component.redis.RedisClientTemplate;
import com.tqmall.mana.component.redis.RedisKeyBean;
import com.tqmall.mana.component.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * 有保险资格的门店
 *
 * Created by huangzhangting on 17/1/6.
 */
@Service
public class ExtInsuranceShopConfigService {
    @Autowired
    private RpcInsuranceShopConfigService rpcInsuranceShopConfigService;
    @Autowired
    private RedisClientTemplate redisClient;


    /** 查询全部的门店id */
    public Set<Integer> getAllShopIds(){
        String key = RedisKeyBean.INSURANCE_SHOP_IDS_KEY;
        String redisStr = redisClient.get(key);
        if(redisStr != null){
            Set<Integer> ids = JsonUtil.strToCollection(redisStr, Set.class, Integer.class);
            if(ids != null) {
                return ids;
            }
        }

        Set<Integer> shopIds = new HashSet<>();
        QueryInsuranceShopParam param = new QueryInsuranceShopParam();
        int pageSize = 1000;
        param.setPageNum(1);
        param.setPageSize(pageSize);
        PagingResult<InsuranceShopConfigDTO> pagingResult = rpcInsuranceShopConfigService.queryShopListByCondition(param);
        if(pagingResult.isSuccess() && !CollectionUtils.isEmpty(pagingResult.getList())){
            addShopIds(shopIds, pagingResult.getList());
            int total = pagingResult.getTotal();
            if(total > pageSize){
                int pages = total%pageSize==0?total/pageSize:(total/pageSize+1);
                while (pages > 1){
                    param.setPageNum(pages);
                    pagingResult = rpcInsuranceShopConfigService.queryShopListByCondition(param);
                    if(pagingResult.isSuccess()){
                        addShopIds(shopIds, pagingResult.getList());
                    }
                    pages--;
                }
            }
        }

        if(!shopIds.isEmpty()){
            redisClient.lazySet(key, shopIds, RedisKeyBean.RREDIS_EXP_MINUTES);
        }

        return shopIds;
    }
    private void addShopIds(Set<Integer> shopIds, List<InsuranceShopConfigDTO> list){
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        for(InsuranceShopConfigDTO configDTO : list){
            shopIds.add(configDTO.getShopId());
        }
    }

}
