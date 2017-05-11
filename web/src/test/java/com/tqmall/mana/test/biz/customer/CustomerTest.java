package com.tqmall.mana.test.biz.customer;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.result.InsuranceFormDTO;
import com.tqmall.mana.beans.BO.customer.*;
import com.tqmall.mana.beans.BO.outInsurance.InsuranceFormBO;
import com.tqmall.mana.beans.param.CommonPageParam;
import com.tqmall.mana.beans.param.CommonVOPageParam;
import com.tqmall.mana.beans.param.PreferentialVOPageParam;
import com.tqmall.mana.biz.manager.customer.CommunicateRecordBiz;
import com.tqmall.mana.biz.manager.customer.CustomerBiz;
import com.tqmall.mana.biz.manager.customer.PreferentialLogBiz;
import com.tqmall.mana.biz.manager.insurance.InsuranceBiz;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.external.dubbo.insurance.ExtInsuranceFormService;
import com.tqmall.mana.external.dubbo.search.ExtUcShopService;
import com.tqmall.mana.external.dubbo.stall.ExtRegionService;
import com.tqmall.mana.test.BaseTest;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.ucenter.param.UcShopParam;
import com.tqmall.search.dubbo.client.ucenter.result.UcShopDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhangting on 16/12/14.
 */
@Slf4j
public class CustomerTest extends BaseTest {

    @Autowired
    private PreferentialLogBiz preferentialLogBiz;

    @Autowired
    private CustomerBiz customerBiz;

    @Autowired
    private CommunicateRecordBiz communicateRecordBiz;

    @Autowired
    private ExtRegionService extRegionService;

    @Autowired
    private InsuranceBiz insuranceBiz;

    @Autowired
    private ExtInsuranceFormService extInsuranceFormService;

    @Autowired
    private ExtUcShopService extUcShopService;




    //新增客户测试
    @Test
    public void addCustomerTest(){
        AddCustomerBO customer = new AddCustomerBO();
        customer.setCustomerMobile("18042402009");
        customer.setCustomerName("周先生");
        customer.setCustomerSource(0);
        customer.setCustomerProvince(12);
        customer.setCustomerCity(34);
        customer.setCustomerDistrict(56);
        customer.setCustomerAddress("西湖大世界");
        customer.setCertificateType("身份证");
        customer.setCertificateNo("123123123123123");
        customer.setLicencePlate("浙A99999");
        customer.setVehicleType("火箭炮-hzt");
        customer.setInsureIntention(1);
        customer.setInsureCompanyId(222222);
        customer.setInsureStatus(1);
//        customer.setInsureEndDate(new Date());
        customer.setInsureProvince("浙江省");
        customer.setInsureCity("杭州市");
        customer.setInsureProvinceCode(" 浙江--12");
        customer.setInsureCityCode("杭州--34");
        customer.setInsureVehicleType("火箭炮-hzt");
        customer.setEngineNo("宝00001");
//        customer.setVehicleRegDate(new Date());
        customer.setVinNo("VIN00000000001");
//        customer.setCommunicateDate(new Date());
        customer.setCommunicateContent("聊得很开心~~~");

        Result result = customerBiz.addCustomer(customer);
        System.out.println(JsonUtil.objectToStr(result));
    }

    //新增车辆测试
    @Test
    public void addCustomerVehicleTest(){
        AddCustomerBO vehicleBO = new AddCustomerBO();
        vehicleBO.setCustomerId(3);
        vehicleBO.setCustomerMobile("15158036448");
        vehicleBO.setLicencePlate("浙A88886");
        vehicleBO.setVehicleType("火箭炮-hzt");

//        vehicleBO.setCommunicateDate(new Date());
        vehicleBO.setCommunicateContent("聊得很开心~~~");

        Result result = customerBiz.addCustomerVehicle(vehicleBO);
        System.out.println(JsonUtil.objectToStr(result));
    }

    //新增沟通记录测试
    @Test
    public void addCommunicateRecordTest(){
        AddCommunicateRecordBO communicateRecordBO = new AddCommunicateRecordBO();
        communicateRecordBO.setCustomerVehicleId(4);
        communicateRecordBO.setCustomerMobile("15158036448");
        communicateRecordBO.setCommunicateChannel(0);
//        communicateRecordBO.setCommunicateDate(new Date());
        communicateRecordBO.setCommunicateContent("聊得很开心~~~");

        Result result = communicateRecordBiz.addCommunicateRecord(communicateRecordBO);
        System.out.println(JsonUtil.objectToStr(result));
    }

    //通过姓名.手机号或车牌号获取客户信息
    @Test
    public void searchCustomer(){

         PagingResult pagingResult= customerBiz.searchCustomer("浙A99999",2,1);//浙A9999918042402060

        log.info("********* : "+JsonUtil.objectToStr(pagingResult));
    }

    @Test
    public void communicationTest(){

//        PagingResult pagingResult = customerBiz.searchCommunicationRecordList(2,100);
//        log.info("********* : "+JsonUtil.objectToStr(pagingResult));
    }


    @Test
    public void IdTest(){

        Result<SearchCustomerBO> result = customerBiz.searchCustomerDetailById(4);
        log.info("*****123**** : "+JsonUtil.objectToStr(result));
    }

    @Test
    public void testExtRegionService(){

        List<Integer> regionIds = new ArrayList<>();
        regionIds.add(12);
        regionIds.add(34);
        regionIds.add(56);
        Map<Integer, String> map = extRegionService.getRegionNameMap(regionIds);

        log.info("********* : "+JsonUtil.objectToStr(map));
    }

    @Test
    public void testExtInsuranceFormService1(){

        List<InsuranceFormDTO> list = extInsuranceFormService.getInsuranceForms("粤BB481E");
        log.info("********* : "+JsonUtil.objectToStr(list));

    }

    @Test
    public void testExtInsuranceFormService2(){

        InsuranceFormDTO insuranceFormDetail= extInsuranceFormService.getInsuranceFormDetail(89,60520);

        log.info("********* : "+JsonUtil.objectToStr(insuranceFormDetail));

    }

    @Test
    public void testInsuranceBiz(){

        Result<InsuranceFormBO> result = insuranceBiz.getInsuranceFormInfo(89,60520);

        log.info("********* : "+JsonUtil.objectToStr(result));

    }

    @Test
    public void TestPage(){

        CommonVOPageParam commonPageParam = new CommonVOPageParam();
        commonPageParam.setCustomerVehicleId(4);
        commonPageParam.setCustomerMobile("18042402060");
        commonPageParam.setPageSize(1);

        PagingResult<SearchCommunicateRecordBO> pagingResult = communicateRecordBiz.searchCommunicationRecordPagingResult(commonPageParam);
        log.info("********* : "+JsonUtil.objectToStr(pagingResult));
    }

    @Test
    public void TestCustomerPage(){

        CommonPageParam commonPageParam = new CommonPageParam();
        commonPageParam.setCustomerSource(1);


//        PagingResult<CustomerPageBO> pagingResult = customerBiz.searchCustomerFormPagingResult(commonPageParam);

//        log.info("**asdasd******* : "+JsonUtil.objectToStr(pagingResult));

    }

    @Test
    public void addPreferential(){
        AddPreferentialLogBO addPreferentialLogBO = new AddPreferentialLogBO();
        addPreferentialLogBO.setCustomerVehicleId(2);
        addPreferentialLogBO.setCustomerMobile("18042402060");
        addPreferentialLogBO.setPreferentialContent("有优惠了 ####");
        addPreferentialLogBO.setPreferentialType(2);
        addPreferentialLogBO.setPreferentialNum(5);
        addPreferentialLogBO.setSendDate(new Date());

        Result result = preferentialLogBiz.addPreferentialLog(addPreferentialLogBO);
    }

    @Test
    public void updateCustomerInfo(){
        UpdateCustomerInfoBO updateCustomerInfoBO = new UpdateCustomerInfoBO();
        updateCustomerInfoBO.setId(2);
        updateCustomerInfoBO.setVehicleId(2);
        updateCustomerInfoBO.setCustomerMobile("18042402060");
        updateCustomerInfoBO.setCustomerName("恒");
        updateCustomerInfoBO.setCustomerSource(1);
        updateCustomerInfoBO.setCertificateType("军官证");
        updateCustomerInfoBO.setCertificateNo("9999999990000");
        updateCustomerInfoBO.setInsureEndDate(new Date());
        updateCustomerInfoBO.setEngineNo("Q111111");
        updateCustomerInfoBO.setInsureIntention(1);
        updateCustomerInfoBO.setInsureStatus(1);
        updateCustomerInfoBO.setLicencePlate("豫SSSSSS");
        updateCustomerInfoBO.setInsureVehicleType("TTT");
        updateCustomerInfoBO.setVehicleRegDate(new Date());
        updateCustomerInfoBO.setVinNo("VIN0000000000");
        Result result = customerBiz.updateCustomerAndVehicle(updateCustomerInfoBO);
        log.info("########"+result);
    }

    @Test
    public void testExtUcShop(){
        UcShopParam ucShopParam = new UcShopParam();
        ucShopParam.setAddrType(0);
        ucShopParam.setShopStatus(0);
        ucShopParam.setProvinceId(31);
//        ucShopParam.setCityId(383);
//        ucShopParam.setDistrictId(3231);
//        ucShopParam.setCompanyName("小马");
//        ucShopParam.setCompanyName("临安");

        PageableRequest pageableRequest = new PageableRequest(1,10);

        Page<UcShopDTO> page = extUcShopService.getUcShops(ucShopParam,pageableRequest);

        log.info("######"+JsonUtil.objectToStr(page));

    }

    @Test
    public void testPreferential(){

        PreferentialVOPageParam preferentialVOPageParam = new PreferentialVOPageParam();
        preferentialVOPageParam.setCustomerVehicleId(2);

        PagingResult<AddPreferentialLogBO> pagingResult= preferentialLogBiz.getPreferentialBOList(preferentialVOPageParam);

        log.info("######"+JsonUtil.objectToStr(pagingResult));

    }

    @Test
    public void testLatest(){

        Result result = communicateRecordBiz.searchLatestCommunicationRecord(2);
        log.info("######"+JsonUtil.objectToStr(result));

    }

    @Test
    public void test(){

        AddPreferentialVO addPreferentialVO = new AddPreferentialVO();
        List<Integer> list = new ArrayList<>();
        list.add(4);
        list.add(6);
        addPreferentialVO.setVehicleIdList(list);
        addPreferentialVO.setPreferentialNum(4);
        addPreferentialVO.setPreferentialType(1);

        Result result = preferentialLogBiz.addPreferentialLogList(addPreferentialVO);
        log.info("######"+JsonUtil.objectToStr(result));
    }


}
