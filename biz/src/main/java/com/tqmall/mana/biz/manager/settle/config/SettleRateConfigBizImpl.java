package com.tqmall.mana.biz.manager.settle.config;

import com.tqmall.mana.beans.entity.settle.SettleRateConfigDO;
import com.tqmall.mana.beans.param.settle.RateConfigQueryPO;
import com.tqmall.mana.component.enums.insurance.dict.ApplyRangeEnum;
import com.tqmall.mana.dao.mapper.settle.SettleRateConfigDOMapper;
import com.tqmall.mana.dao.mapper.settle.SettleRateRegionConfigDOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by huangzhangting on 17/3/11.
 */
@Slf4j
@Service
public class SettleRateConfigBizImpl implements SettleRateConfigBiz {
    @Autowired
    private SettleRateConfigDOMapper rateConfigDOMapper;
    @Autowired
    private SettleRateRegionConfigDOMapper regionConfigDOMapper;

    @Override
    public List<SettleRateConfigDO> getAll(RateConfigQueryPO queryPO) {
        Assert.notNull(queryPO, "比例配置查询条件不能为空");
        Set<Integer> rateConfigIds = getRateConfigIdsByRegion(queryPO.getCityCode());
        List<SettleRateConfigDO> list = null;
        if(!rateConfigIds.isEmpty()){
            queryPO.setIds(rateConfigIds);
            queryPO.setApplyRange(ApplyRangeEnum.REGION.getCode());
            list = rateConfigDOMapper.selectAll(queryPO);
        }
        if(CollectionUtils.isEmpty(list)){
            RateConfigQueryPO configQueryPO = new RateConfigQueryPO();
            configQueryPO.setInsuranceCompanyId(queryPO.getInsuranceCompanyId());
            configQueryPO.setApplyRange(ApplyRangeEnum.INSURANCE_COMPANY.getCode());
            list = rateConfigDOMapper.selectAll(configQueryPO);
        }
        return list;
    }

    private Set<Integer> getRateConfigIdsByRegion(String cityCode){
        if(StringUtils.isEmpty(cityCode)){
            return new HashSet<>();
        }
        return regionConfigDOMapper.getRateConfigIdsByRegion(cityCode);
    }

}
