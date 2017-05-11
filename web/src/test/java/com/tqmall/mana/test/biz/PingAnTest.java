package com.tqmall.mana.test.biz;

import com.tqmall.mana.beans.BO.outInsurance.InsuranceItemBO;
import com.tqmall.mana.beans.BO.outInsurance.PingAnBO;
import com.tqmall.mana.biz.manager.outInsurance.PingAnInsuranceBiz;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zxg on 16/12/1.
 * 20:32
 * no bug,以后改代码的哥们，祝你好运~！！
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class PingAnTest {

    @Autowired
    private PingAnInsuranceBiz pingAnInsuranceBiz;

    @Test
    public void testGet() throws ParseException {
        PingAnBO pingAnBO = new PingAnBO();

        pingAnBO.setProvinceName("浙江省");
        pingAnBO.setCityName("杭州");
        pingAnBO.setLicenseNo("浙AYH291");
        pingAnBO.setVinCode("LGJF1FE09FT398042");
        pingAnBO.setEngineNo("SMK0506");
        pingAnBO.setPeopleName("李漠达");
        pingAnBO.setPeopleCard("432622197602113374");
        pingAnBO.setCarBrandCode("EQ7160LS1B1");
        pingAnBO.setCarDisplay("1.6");
        pingAnBO.setCarGear("CVT");
        pingAnBO.setCarSeatNum("5");
        pingAnBO.setCarYear("2016");

        String sDt = "2015-11-01";
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        Date dt2 = sdf.parse(sDt);
        pingAnBO.setRegisterDate(dt2);


        List<InsuranceItemBO> amountList = new ArrayList<>();
        InsuranceItemBO insuranceItemBO = new InsuranceItemBO();
        insuranceItemBO.setInsuranceName("车损险");
        insuranceItemBO.setDeductible(0);
        amountList.add(insuranceItemBO);
        InsuranceItemBO insuranceItemBO1 = new InsuranceItemBO();
        insuranceItemBO1.setInsuranceName("车上人员责任险(司机)");
        insuranceItemBO1.setDeductible(0);
        insuranceItemBO1.setInsuranceAmount(new BigDecimal("10000"));
        amountList.add(insuranceItemBO1);
        pingAnBO.setAmountList(amountList);

        pingAnInsuranceBiz.getSYData(pingAnBO);

    }

}
