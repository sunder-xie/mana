package com.tqmall.mana.biz.manager.settle.checkDetail;

import com.tqmall.core.common.exception.BusinessCheckFailException;
import com.tqmall.core.common.exception.BusinessProcessFailException;
import com.tqmall.mana.beans.entity.settle.SettleServiceCheckDetailDO;
import com.tqmall.mana.beans.entity.settle.SettleShopCheckDetailDO;
import com.tqmall.mana.beans.param.settle.ServicePackageModifyPO;
import com.tqmall.mana.beans.param.settle.SettleShopCheckDetailQueryPO;
import com.tqmall.mana.component.enums.insurance.dict.ServicePackageStatusEnum;
import com.tqmall.mana.component.enums.insurance.dict.SettleProjectEnum;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.dao.mapper.settle.SettleServiceCheckDetailDOMapper;
import com.tqmall.mana.dao.mapper.settle.SettleShopCheckDetailDOMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by zxg on 17/2/4.
 * 10:33
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Slf4j
@Service
public class SettleServiceCheckDetailBizImpl implements SettleServiceCheckDetailBiz {

    @Autowired
    private SettleServiceCheckDetailDOMapper settleServiceCheckDetailDOMapper;
//    @Autowired
//    private SettleShopBaseBiz settleShopBaseBiz;
    @Autowired
    private SettleShopCheckDetailDOMapper settleShopCheckDetailDOMapper;


    @Override
    public SettleServiceCheckDetailDO getByInsuranceOrderSn(String insuranceOrderSn) {
        if (StringUtils.isEmpty(insuranceOrderSn)) {
            throw new BusinessCheckFailException("", "insuranceOrderSn id is empty ");
        }

        SettleServiceCheckDetailDO searchDO = new SettleServiceCheckDetailDO();
        searchDO.setInsuranceOrderSn(insuranceOrderSn);

        List<SettleServiceCheckDetailDO> resultList = settleServiceCheckDetailDOMapper.selectBySearchDO(searchDO);
        if(CollectionUtils.isEmpty(resultList)){
            throw new BusinessProcessFailException("mysql no data","");
        }
        return resultList.get(0);
    }

    @Transactional
    @Override
    public boolean modifyServicePackage(ServicePackageModifyPO param) {
        log.info("modifyServicePackage param:{}", JsonUtil.objectToStr(param));

        Assert.notNull(param, "参数不能为空");
        Assert.hasLength(param.getOperator(), "操作人员不能为空");
        Assert.notNull(param.getPackageStatus(), "服务包状态不能为空");
        Assert.notEmpty(param.getOrderSnList(), "物料订单编号集合不能为空");

        List<SettleServiceCheckDetailDO> serviceCheckDetailDOList =
                settleServiceCheckDetailDOMapper.selectByOrderSnList(param.getOrderSnList());
        Assert.notEmpty(serviceCheckDetailDOList, "未查到数据");

        //物料订单编号集合
//        Set<String> orderSnSet = new HashSet<>();
        for(SettleServiceCheckDetailDO checkDetailDO : serviceCheckDetailDOList){
            SettleServiceCheckDetailDO detailDO = new SettleServiceCheckDetailDO();
            detailDO.setId(checkDetailDO.getId());
            detailDO.setModifier(param.getOperator());
            detailDO.setGmtModified(new Date());
            detailDO.setSettlePackageStatus(param.getPackageStatus());
            settleServiceCheckDetailDOMapper.updateByPrimaryKeySelective(detailDO);

//            orderSnSet.add(checkDetailDO.getSettlePackageOrderSn());
        }

        /** 重新统计服务包数量 */
//        recountPackageNum(param.getPackageStatus(), orderSnSet);

        return true;
    }

    /** 重新统计服务包数量 */
    /*
    private void recountPackageNum(Integer packageStatus, Set<String> orderSnSet){
        if(orderSnSet.isEmpty()){
            return;
        }
        SettleShopCheckDetailQueryPO queryPO = new SettleShopCheckDetailQueryPO();
        queryPO.setBizSnList(new ArrayList<>(orderSnSet));
        queryPO.setSettleProjectId(SettleProjectEnum.PACKAGE_REBATE.getCode());
        List<SettleShopCheckDetailDO> checkDetailDOList = settleShopCheckDetailDOMapper.selectByCondition(queryPO);
        if(checkDetailDOList.isEmpty()){
            return;
        }
        Map<Integer, Set<String>> shopIdMap = new HashMap<>();
        for(SettleShopCheckDetailDO detailDO : checkDetailDOList){
            Integer shopId = detailDO.getAgentId();
            Set<String> set = shopIdMap.get(shopId);
            if(set==null){
                set = new HashSet<>();
                shopIdMap.put(shopId, set);
            }
            set.add(detailDO.getBizSn());
        }

        if(ServicePackageStatusEnum.DISPATCHING.getCode().equals(packageStatus)){
            for(Map.Entry<Integer, Set<String>> entry : shopIdMap.entrySet()){
                settleShopBaseBiz.changePackageWaitToSend(entry.getValue().size(), entry.getKey());
            }
        }else if(ServicePackageStatusEnum.HAS_SIGNED.getCode().equals(packageStatus)){
            for(Map.Entry<Integer, Set<String>> entry : shopIdMap.entrySet()){
                settleShopBaseBiz.changePackageSendToReceive(entry.getValue().size(), entry.getKey());
            }
        }
    }
*/
}
