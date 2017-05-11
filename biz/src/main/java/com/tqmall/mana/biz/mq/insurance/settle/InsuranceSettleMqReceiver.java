package com.tqmall.mana.biz.mq.insurance.settle;

import com.tqmall.mana.biz.manager.settle.settleCalculate.InsuranceSettleCalculateBiz;
import com.tqmall.mana.biz.mq.MqReceiver;
import com.tqmall.mana.component.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class InsuranceSettleMqReceiver extends MqReceiver {
    private static final Lock SETTLE_DATA_LOCK = new ReentrantLock(true);

    @Autowired
    private InsuranceSettleCalculateBiz insuranceSettleCalculateBiz;


    @Override
    public void handleMessage(Object obj) {
        if (obj == null || !(obj instanceof String)) {
            log.error("insurance settle mq not process, obj: {}", obj);
            return;
        }
        String str = (String) obj;
        log.info("start process insurance settle message: {}", str);

        try {
            InsuranceSettleMsg settleMsg = JsonUtil.strToObject(str, InsuranceSettleMsg.class);
            if(settleMsg==null){
                return;
            }

            Integer msgType = settleMsg.getMsgType();
            if(SettleMsgTypeEnum.FORM_BASIC.getCode().equals(msgType)) {
                InsuranceSettleBasicMsg basicMsg =
                        JsonUtil.strToObject(settleMsg.getMsgContent(), InsuranceSettleBasicMsg.class);

//                insuranceSettleCalculateBiz.lockCalculateSettleData(basicMsg);

                //暂时先使用这种方式，不然spring事务无效，后期再调整
                SETTLE_DATA_LOCK.lock();
                try {
                    insuranceSettleCalculateBiz.calculateSettleData(basicMsg, true);
                }catch (Exception e){
                    throw e;
                }finally {
                    SETTLE_DATA_LOCK.unlock();
                }

            }else if(SettleMsgTypeEnum.MATERIAL_ALLOWANCE.getCode().equals(msgType)){
                InsuranceSettleMaterialAllowanceMsg materialAllowanceMsg =
                        JsonUtil.strToObject(settleMsg.getMsgContent(), InsuranceSettleMaterialAllowanceMsg.class);

                insuranceSettleCalculateBiz.calculateSettleDataForMaterialAllowance(materialAllowanceMsg);

            }

        } catch (Exception e) {
            log.error("insurance settle mq process error, message: " + str, e);
        }
    }

}
