package com.tqmall.mana.test.biz.settle;

import com.tqmall.mana.biz.manager.insurance.InsuranceSettleSyncBiz;
import com.tqmall.mana.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by huangzhangting on 17/3/15.
 */
public class InsuranceSettleSyncBizTest extends BaseTest {
    @Autowired
    private InsuranceSettleSyncBiz insuranceSettleSyncBiz;

    @Test
    public void test(){
        String vehicleSn = "粤031473";
        vehicleSn = "粤Y78097";
        vehicleSn = "粤L98710"; //未投保虚拟保单
//        vehicleSn = "粤0313106"; //有机滤补贴

        insuranceSettleSyncBiz.syncDataByVehicleSn(vehicleSn);
    }

}
