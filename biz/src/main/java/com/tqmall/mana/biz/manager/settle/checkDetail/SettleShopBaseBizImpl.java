package com.tqmall.mana.biz.manager.settle.checkDetail;

import com.tqmall.core.common.exception.BusinessCheckFailException;
import com.tqmall.mana.beans.BO.settle.SettleShopBO;
import com.tqmall.mana.beans.entity.settle.SettleShopBaseDO;
import com.tqmall.mana.component.annotation.lock.ManaLock;
import com.tqmall.mana.component.annotation.lock.ManaLockKey;
import com.tqmall.mana.component.redis.RedisKeyBean;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.dao.mapper.settle.SettleShopBaseDOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zxg on 17/2/4.
 * 15:31
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Service
@Slf4j
public class SettleShopBaseBizImpl implements SettleShopBaseBiz {
/*
    @Autowired
    private SettleShopBaseDOMapper settleShopBaseDOMapper;

    @Override
    @ManaLock(lockKeyName = RedisKeyBean.LOCK_SETTLE_SHOP_BASE_BY_SHOP_ID)
    public Boolean addPayableCashAmount(BigDecimal payableCashAmount, @ManaLockKey Integer shopId) {
        if (payableCashAmount == null || payableCashAmount.compareTo(BigDecimal.ZERO) <= 0) {
            log.info("add payableCash fail,amount is <= 0.it is :{}", payableCashAmount);
            return false;
        }
        addDataToSql(shopId,payableCashAmount,null,null);
        return true;
    }

    @Override
    @ManaLock(lockKeyName = RedisKeyBean.LOCK_SETTLE_SHOP_BASE_BY_SHOP_ID)
    public Boolean changeCashFromPayableToSettled(BigDecimal payableToSettledAmount, @ManaLockKey Integer shopId) {
        if (payableToSettledAmount == null || payableToSettledAmount.compareTo(BigDecimal.ZERO) <= 0) {
            log.info("payableToSettledAmount fail,amount is <= 0.it is :{}", payableToSettledAmount);
            return false;
        }
        SettleShopBaseDO settleShopBaseDO = getShopBaseDOByShopId(shopId);
        if (settleShopBaseDO == null) {
            log.info("mysql no data.shopId:{}", shopId);
            return false;
        }
        BigDecimal sqlPayableCashAmount = settleShopBaseDO.getPayableCashAmount();
        BigDecimal sqlSettledCashAmount = settleShopBaseDO.getSettledCashAmount();
        if (sqlPayableCashAmount.compareTo(payableToSettledAmount) < 0) {
            // 未结算的现金 < 准备从未结算到已结算的值
            log.error("sqlPayableCashAmount < payableToSettledAmount.the shopId:{},payableToSettledAmount:{},sqlPayableCashAmount:{}", shopId, payableToSettledAmount, sqlPayableCashAmount);
            return false;
        }

        BigDecimal upPayableAmount = sqlPayableCashAmount.subtract(payableToSettledAmount);
        BigDecimal upSettledAmount = payableToSettledAmount.add(sqlSettledCashAmount == null ? BigDecimal.ZERO : sqlSettledCashAmount);

        //更新数据
        SettleShopBaseDO upDO = new SettleShopBaseDO();
        upDO.setId(settleShopBaseDO.getId());
        upDO.setPayableCashAmount(upPayableAmount);
        upDO.setSettledCashAmount(upSettledAmount);
        settleShopBaseDOMapper.updateByPrimaryKeySelective(upDO);
        return true;
    }

    @Override
    @ManaLock(lockKeyName = RedisKeyBean.LOCK_SETTLE_SHOP_BASE_BY_SHOP_ID)
    public Boolean addPayableBonusAmount(BigDecimal payableBonusAmount, @ManaLockKey Integer shopId) {
        if (payableBonusAmount == null || payableBonusAmount.compareTo(BigDecimal.ZERO) <= 0) {
            log.info("add payableBonusAmount fail,amount is <= 0.it is :{}", payableBonusAmount);
            return false;
        }
        addDataToSql(shopId,null,payableBonusAmount,null);
        return true;
    }

    @Override
    @ManaLock(lockKeyName = RedisKeyBean.LOCK_SETTLE_SHOP_BASE_BY_SHOP_ID)
    public Boolean changeBonusFromPayableToSettled(BigDecimal payableToSettledAmount, @ManaLockKey Integer shopId) {
        if (payableToSettledAmount == null || payableToSettledAmount.compareTo(BigDecimal.ZERO) <= 0) {
            log.info("payableToSettledAmount fail,amount is <= 0.it is :{}", payableToSettledAmount);
            return false;
        }
        SettleShopBaseDO settleShopBaseDO = getShopBaseDOByShopId(shopId);
        if (settleShopBaseDO == null) {
            log.info("mysql no data.shopId:{}", shopId);
            return false;
        }
        BigDecimal sqlPayableBonusAmount = settleShopBaseDO.getPayableBonusAmount();
        BigDecimal sqlSettledBonusAmount = settleShopBaseDO.getSettledBonusAmount();
        if (sqlPayableBonusAmount.compareTo(payableToSettledAmount) < 0) {
            // 未结算的奖励金 < 准备从未结算到已结算的值
            log.error("sqlPayableBonusAmount < payableToSettledAmount.the shopId:{},payableToSettledAmount:{},sqlPayableBonusAmount:{}", shopId, payableToSettledAmount, sqlPayableBonusAmount);
            return false;
        }

        BigDecimal upPayableAmount = sqlPayableBonusAmount.subtract(payableToSettledAmount);
        BigDecimal upSettledAmount = payableToSettledAmount.add(sqlSettledBonusAmount == null ? BigDecimal.ZERO : sqlSettledBonusAmount);

        //更新数据
        SettleShopBaseDO upDO = new SettleShopBaseDO();
        upDO.setId(settleShopBaseDO.getId());
        upDO.setPayableBonusAmount(upPayableAmount);
        upDO.setSettledBonusAmount(upSettledAmount);
        settleShopBaseDOMapper.updateByPrimaryKeySelective(upDO);
        return true;
    }

    @Override
    @ManaLock(lockKeyName = RedisKeyBean.LOCK_SETTLE_SHOP_BASE_BY_SHOP_ID)
    public Boolean addWaitEffectPackageNum(Integer waitEffectPackageNum, @ManaLockKey Integer shopId) {
        if (waitEffectPackageNum == null || waitEffectPackageNum <= 0) {
            log.info("add waitEffectPackageNum fail,waitEffectPackageNum is <= 0.it is :{}", waitEffectPackageNum);
            return false;
        }
        addDataToSql(shopId,null,null,waitEffectPackageNum);
        return true;
    }

    @Override
    @ManaLock(lockKeyName = RedisKeyBean.LOCK_SETTLE_SHOP_BASE_BY_SHOP_ID)
    public Boolean changePackageWaitEffectToWait(Integer waitEffectToWaitPackageNum, @ManaLockKey Integer shopId) {

        if (waitEffectToWaitPackageNum == null || waitEffectToWaitPackageNum <= 0) {
            log.info("changePackageWaitEffectToWait fail,waitEffectToWaitPackageNum is <= 0.it is :{}", waitEffectToWaitPackageNum);
            return false;
        }
        SettleShopBaseDO settleShopBaseDO = getShopBaseDOByShopId(shopId);
        if (settleShopBaseDO == null) {
            log.info("mysql no data.shopId:{}", shopId);
            return false;
        }
        Integer sqlWaitEffectNum = settleShopBaseDO.getWaitEffectPackageNum();
        if(sqlWaitEffectNum < waitEffectToWaitPackageNum){
            log.info("sqlWaitEffectNum:{} < waitEffectToWaitPackageNum:{}",sqlWaitEffectNum,waitEffectToWaitPackageNum);
            return false;
        }

        Integer saveWaitEffectNum = sqlWaitEffectNum - waitEffectToWaitPackageNum;
        Integer saveWaitNum = (settleShopBaseDO.getWaitPackageNum() == null?0:settleShopBaseDO.getWaitPackageNum()) + waitEffectToWaitPackageNum;

        //更新数据
        SettleShopBaseDO upDO = new SettleShopBaseDO();
        upDO.setId(settleShopBaseDO.getId());
        upDO.setWaitEffectPackageNum(saveWaitEffectNum);
        upDO.setWaitPackageNum(saveWaitNum);
        settleShopBaseDOMapper.updateByPrimaryKeySelective(upDO);
        return true;
    }

    @Override
    @ManaLock(lockKeyName = RedisKeyBean.LOCK_SETTLE_SHOP_BASE_BY_SHOP_ID)
    public Boolean changePackageWaitToSend(Integer waitToSendPackageNum, @ManaLockKey Integer shopId) {
        if (waitToSendPackageNum == null || waitToSendPackageNum <= 0) {
            log.info("changePackageWaitToSend fail,waitToSendPackageNum is <= 0.it is :{}", waitToSendPackageNum);
            return false;
        }
        SettleShopBaseDO settleShopBaseDO = getShopBaseDOByShopId(shopId);
        if (settleShopBaseDO == null) {
            log.info("mysql no data.shopId:{}", shopId);
            return false;
        }
        Integer sqlWaitNum = settleShopBaseDO.getWaitPackageNum();
        if(sqlWaitNum < waitToSendPackageNum){
            log.info("sqlWaitNum:{} < waitToSendPackageNum:{}",sqlWaitNum,waitToSendPackageNum);
            return false;
        }

        Integer saveWaitNum = sqlWaitNum - waitToSendPackageNum;
        Integer saveSendNum = (settleShopBaseDO.getSendPackageNum() == null?0:settleShopBaseDO.getSendPackageNum()) + waitToSendPackageNum;

        //更新数据
        SettleShopBaseDO upDO = new SettleShopBaseDO();
        upDO.setId(settleShopBaseDO.getId());
        upDO.setWaitPackageNum(saveWaitNum);
        upDO.setSendPackageNum(saveSendNum);
        settleShopBaseDOMapper.updateByPrimaryKeySelective(upDO);
        return true;
    }

    @Override
    @ManaLock(lockKeyName = RedisKeyBean.LOCK_SETTLE_SHOP_BASE_BY_SHOP_ID)
    public Boolean changePackageSendToReceive(Integer sendToReceivePackageNum, @ManaLockKey Integer shopId) {
        if (sendToReceivePackageNum == null || sendToReceivePackageNum <= 0) {
            log.info("changePackageSendToReceive fail,sendToReceivePackageNum is <= 0.it is :{}", sendToReceivePackageNum);
            return false;
        }
        SettleShopBaseDO settleShopBaseDO = getShopBaseDOByShopId(shopId);
        if (settleShopBaseDO == null) {
            log.info("mysql no data.shopId:{}", shopId);
            return false;
        }
        Integer sqlSendNum = settleShopBaseDO.getSendPackageNum();
        if(sqlSendNum < sendToReceivePackageNum){
            log.info("sqlSendNum:{} < sendToReceivePackageNum:{}",sqlSendNum,sendToReceivePackageNum);
            return false;
        }

        Integer saveSendNum = sqlSendNum - sendToReceivePackageNum;
        Integer saveReceiveNum = (settleShopBaseDO.getReceivePackageNum() == null?0:settleShopBaseDO.getReceivePackageNum()) + sendToReceivePackageNum;

        //更新数据
        SettleShopBaseDO upDO = new SettleShopBaseDO();
        upDO.setId(settleShopBaseDO.getId());
        upDO.setSendPackageNum(saveSendNum);
        upDO.setReceivePackageNum(saveReceiveNum);
        settleShopBaseDOMapper.updateByPrimaryKeySelective(upDO);

        return true;
    }

    @Override
    @ManaLock(lockKeyName = RedisKeyBean.LOCK_SETTLE_SHOP_BASE_BY_SHOP_ID)
    public SettleShopBO getShopByShopId(@ManaLockKey Integer shopId) {
        if (shopId == null || shopId < 1) {
            throw new BusinessCheckFailException("", "shop id is empty or < 1");
        }

        return BdUtil.do2bo(getShopBaseDOByShopId(shopId), SettleShopBO.class);
    }


    *//*==========private============*//*

    private SettleShopBaseDO getShopBaseDOByShopId(Integer shopId) {
        if (shopId == null || shopId < 1) {
            throw new BusinessCheckFailException("", "shop id is empty or < 1");
        }
        SettleShopBaseDO searchDO = new SettleShopBaseDO();
        searchDO.setAgentId(shopId);
        List<SettleShopBaseDO> resultList = settleShopBaseDOMapper.selectByDO(searchDO);
        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }

        return resultList.get(0);
    }

    //新增数据到数据库，若数据库不存在数据，则新建数据
    private void addDataToSql(Integer shopId, BigDecimal payableCashAmount, BigDecimal payableBonusAmount, Integer waitffectPackageNum) {
        if (payableCashAmount == null) payableCashAmount = BigDecimal.ZERO;
        if (payableBonusAmount == null) payableBonusAmount = BigDecimal.ZERO;
        if (waitffectPackageNum == null) waitffectPackageNum = 0;

        SettleShopBaseDO settleShopBaseDO = getShopBaseDOByShopId(shopId);
        if (settleShopBaseDO == null) {
            // 新增数据
            settleShopBaseDO = new SettleShopBaseDO();
            settleShopBaseDO.setAgentId(shopId);
            settleShopBaseDO.setPayableCashAmount(payableCashAmount);
            settleShopBaseDO.setPayableBonusAmount(payableBonusAmount);
            settleShopBaseDO.setWaitEffectPackageNum(waitffectPackageNum);
            // 保存数据
            settleShopBaseDOMapper.insertSelective(settleShopBaseDO);
            return;
        }

        // 数据库存在数据，更新数据

        SettleShopBaseDO upDO = new SettleShopBaseDO();
        upDO.setId(settleShopBaseDO.getId());

        // 未结算现金
        BigDecimal sqlPayableCashAmount = settleShopBaseDO.getPayableCashAmount() == null ? BigDecimal.ZERO : settleShopBaseDO.getPayableCashAmount();
        BigDecimal savePayableCashAmount = payableCashAmount.add(sqlPayableCashAmount);
        if (savePayableCashAmount.compareTo(sqlPayableCashAmount) != 0) {
            upDO.setPayableCashAmount(savePayableCashAmount);
        }

        //未结算奖励金
        BigDecimal sqlPayableBonusAmount = settleShopBaseDO.getPayableBonusAmount() == null ? BigDecimal.ZERO : settleShopBaseDO.getPayableBonusAmount();
        BigDecimal savePayableBonusAmount = payableBonusAmount.add(sqlPayableBonusAmount);
        if (savePayableBonusAmount.compareTo(sqlPayableBonusAmount) != 0) {
            upDO.setPayableBonusAmount(savePayableBonusAmount);
        }

        //未生效的服务包
        Integer sqlWaitEffectNum = settleShopBaseDO.getWaitEffectPackageNum() == null ? 0 : settleShopBaseDO.getWaitEffectPackageNum();
        Integer saveWaitEffectPackageNum = waitffectPackageNum + sqlWaitEffectNum;
        if (!saveWaitEffectPackageNum.equals(sqlWaitEffectNum)) {
            upDO.setWaitEffectPackageNum(saveWaitEffectPackageNum);
        }


        settleShopBaseDOMapper.updateByPrimaryKeySelective(upDO);
    }
    */
}
