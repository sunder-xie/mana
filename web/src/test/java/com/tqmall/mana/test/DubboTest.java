package com.tqmall.mana.test;

import com.tqmall.athena.domain.result.carcategory.HotCarBrandDTO;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.result.InsuranceFormDTO;
import com.tqmall.insurance.domain.result.InsuranceUserServicePackageDTO;
import com.tqmall.mana.beans.BO.UcShopBO;
import com.tqmall.mana.beans.BO.coupon.SendCouponBO;
import com.tqmall.mana.beans.BO.shop.SimpleShopBO;
import com.tqmall.mana.beans.param.UcShopRequestParam;
import com.tqmall.mana.biz.manager.UcShop.UcShopBiz;
import com.tqmall.mana.biz.manager.coupon.CouponBiz;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.external.dubbo.athena.ExtCarService;
import com.tqmall.mana.external.dubbo.insurance.ExtInsuranceFormService;
import com.tqmall.mana.external.dubbo.insurance.ExtInsuranceUserService;
import com.tqmall.mana.external.dubbo.uc.ExtShopInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by huangzhangting on 16/12/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class DubboTest {
    @Autowired
    private ExtInsuranceFormService extInsuranceFormService;
    @Autowired
    private CouponBiz couponBiz;

    @Autowired
    private ExtShopInfoService extShopInfoService;
    @Autowired
    private ExtCarService extCarService;

    @Autowired
    private ExtInsuranceUserService extInsuranceUserService;

    @Autowired
    private UcShopBiz ucShopBiz;


    @Test
    public void test(){
        String vehicleSn = "浙RLL202";
        List<InsuranceFormDTO> list = extInsuranceFormService.getInsuranceForms(vehicleSn);
        System.out.println(JsonUtil.objectToStr(list));

    }

    @Test
    public void testCoupon(){
        SendCouponBO couponBO = new SendCouponBO();
        couponBO.setCouponType(1);
        couponBO.setCouponNum(20);
        couponBO.setMobiles("15158036445");

        Result result = couponBiz.sendCoupon(couponBO);
        System.out.println(JsonUtil.objectToStr(result));

    }

    @Test
    public void testShopInfo(){
        Integer shopId = 131;
        SimpleShopBO simpleShopBO = extShopInfoService.getSimpleShopInfo(shopId);
        System.out.println(JsonUtil.objectToStr(simpleShopBO));
    }

    @Test
    public void testCar(){
        List<HotCarBrandDTO> list = extCarService.getHotCarBrandList();
        System.out.println(JsonUtil.objectToStr(list));
    }


    @Test
    public void testUserPackage(){
        Integer id = 90;
        InsuranceUserServicePackageDTO packageDTO = extInsuranceUserService.getPackageById(id);
        System.out.println(JsonUtil.objectToStr(packageDTO));
    }


    @Test
    public void testUcShop(){
        UcShopRequestParam param = new UcShopRequestParam();
        PagingResult<UcShopBO> pagingResult = ucShopBiz.getShopListInfo(param);
        System.out.println(JsonUtil.objectToStr(pagingResult));
    }

}
