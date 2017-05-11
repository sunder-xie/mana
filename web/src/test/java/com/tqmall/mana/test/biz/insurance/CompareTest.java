package com.tqmall.mana.test.biz.insurance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.outInsurance.InsuranceCompareBO;
import com.tqmall.mana.beans.BO.outInsurance.InsuranceItemBO;
import com.tqmall.mana.beans.param.insurance.SearchTempInfoParam;
import com.tqmall.mana.biz.manager.insurance.InsuranceCompareBiz;
import com.tqmall.mana.biz.manager.offline.OffLineInsuranceBiz;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by zxg on 16/12/3.
 * 17:53
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
@Slf4j
public class CompareTest {

    protected HttpServletResponse response;
    @Autowired
    private InsuranceCompareBiz insuranceCompareBiz;
    @Autowired
    private OffLineInsuranceBiz offLineInsuranceBiz;
    
    @Test
    public void testGet() throws ParseException {
        InsuranceCompareBO compareBO  = new InsuranceCompareBO();

        compareBO.setInsuranceBasicId(1);
        compareBO.setProvinceName("浙江省");
        compareBO.setCityName("杭州");
        compareBO.setLicenseNo("浙AYH291");
        compareBO.setVinCode("LGJF1FE09FT398042");
        compareBO.setEngineNo("SMK0506");
        compareBO.setPeopleName("李漠达");
        compareBO.setPeopleCard("432622197602113374");
        compareBO.setCarBrandCode("EQ7160LS1B1");
        compareBO.setCarDisplay("1.6");
        compareBO.setCarGear("CVT");
        compareBO.setCarSeatNum("5");
        compareBO.setCarYear("2016");

        String sDt = "2015-11-01";
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        Date dt2 = sdf.parse(sDt);
        compareBO.setRegisterDate(dt2);


        List<InsuranceItemBO> amountList = new ArrayList<>();
        InsuranceItemBO insuranceItemBO = new InsuranceItemBO();
        insuranceItemBO.setInsuranceName("车损险");
        insuranceItemBO.setDeductible(0);
        insuranceItemBO.setInsuranceFee(new BigDecimal("1388.2"));
        amountList.add(insuranceItemBO);
        InsuranceItemBO insuranceItemBO1 = new InsuranceItemBO();
        insuranceItemBO1.setInsuranceName("车上人员责任险(司机)");
        insuranceItemBO1.setDeductible(0);
        insuranceItemBO1.setInsuranceAmount(new BigDecimal("10000"));
        insuranceItemBO1.setInsuranceFee(new BigDecimal("2388.2"));

        amountList.add(insuranceItemBO1);
        InsuranceItemBO insuranceItemBO2 = new InsuranceItemBO();
        insuranceItemBO2.setInsuranceName("精神损失险");
        insuranceItemBO2.setDeductible(0);
        insuranceItemBO2.setInsuranceAmount(new BigDecimal("20000"));
        insuranceItemBO2.setInsuranceFee(new BigDecimal("3388.2"));

        amountList.add(insuranceItemBO2);
        compareBO.setAmountList(amountList);

        Result result = insuranceCompareBiz.otherInsuranceCompare(compareBO);

        System.out.println(result);
    }


    @Test
    public void OffLineInsuranceBizExport(){

        SearchTempInfoParam searchTempInfoParam = new SearchTempInfoParam();

        offLineInsuranceBiz.exportOffLineInsuranceList(response,searchTempInfoParam);

        log.info("######asdasd%%%%%%%%%%%%");

    }
}
