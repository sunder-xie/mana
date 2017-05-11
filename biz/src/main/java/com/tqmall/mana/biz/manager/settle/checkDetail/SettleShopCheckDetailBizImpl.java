package com.tqmall.mana.biz.manager.settle.checkDetail;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Maps;
import com.tqmall.mana.beans.entity.settle.SettleInsuranceMaterialAllowanceDO;
import com.tqmall.mana.beans.entity.settle.SettleShopCheckDetailDO;
import com.tqmall.mana.dao.mapper.settle.SettleShopCheckDetailDOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zwb on 17/2/20.
 */
@Service
public class SettleShopCheckDetailBizImpl implements SettleShopCheckDetailBiz {
    @Autowired
    private SettleShopCheckDetailDOMapper settleShopCheckDetailDOMapper;

    @Override
    public Map<Integer, String> showAllowanceAgentName(String shopName) {
        Map<Integer, String> map = Maps.newLinkedHashMap();
        List<SettleShopCheckDetailDO> allowanceDOs = settleShopCheckDetailDOMapper.selectAgentNameList(shopName);
        if (!CollectionUtils.isEmpty(allowanceDOs)) {
            for (SettleShopCheckDetailDO materialAllowanceDO : allowanceDOs) {
                map.put(materialAllowanceDO.getAgentId(), materialAllowanceDO.getAgentName());
            }
        }
        return map;
    }

    @Override
    public Map<String, Integer> getAllowanceStatusByOrderSn(List<String> orderSn) {
        Map<String, Integer> map = Maps.newLinkedHashMap();
        List<SettleInsuranceMaterialAllowanceDO> allowanceDOs = settleShopCheckDetailDOMapper.selectByOrderSnList(orderSn);
        if (!CollectionUtils.isEmpty(allowanceDOs)) {
            for (SettleInsuranceMaterialAllowanceDO materialAllowanceDO : allowanceDOs) {
                map.put(materialAllowanceDO.getOrderSn(), materialAllowanceDO.getAllowanceStatus());
            }
        }
        return map;
    }


}
