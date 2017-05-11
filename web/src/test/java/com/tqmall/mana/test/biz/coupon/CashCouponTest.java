package com.tqmall.mana.test.biz.coupon;

import com.google.common.collect.Lists;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.cashcoupon.CashCouponRegionConfigBO;
import com.tqmall.mana.beans.BO.cashcoupon.CashCouponRuleConfigBO;
import com.tqmall.mana.beans.param.settle.CashCouponConfigSearchParam;
import com.tqmall.mana.beans.param.settle.CashCouponGoodsConfigPO;
import com.tqmall.mana.beans.param.settle.CashCouponRegionConfigPO;
import com.tqmall.mana.beans.param.settle.CashCouponRuleConfigPO;
import com.tqmall.mana.biz.manager.coupon.CashCouponRuleConfigBiz;
import com.tqmall.mana.client.beans.cashcoupon.CreateRuleConfigResultDTO;
import com.tqmall.mana.client.service.InsuranceCompareService;
import com.tqmall.mana.client.service.cashcoupon.RpcCashCouponRuleConfigService;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.test.BaseTest;
import com.tqmall.tqmallstall.domain.result.RegionListDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhouheng on 17/4/11.
 */
@Slf4j
public class CashCouponTest extends BaseTest {

    @Autowired
    private CashCouponRuleConfigBiz configBiz;
    @Autowired
    private RpcCashCouponRuleConfigService ruleConfigService;

    @Test
    public void test(){

        CashCouponConfigSearchParam searchParam = new CashCouponConfigSearchParam();

        PagingResult<CashCouponRegionConfigBO> pageList = configBiz.getRegionConfigPageList(searchParam);

        log.info("*******getRegionConfigPageList******"+JsonUtil.objectToStr(pageList));

    }

    @Test
    public void getConfigByIdTest(){

        CashCouponRuleConfigPO configPO = configBiz.getConfigInfoById(12);

        log.info("*******getConfigById******"+JsonUtil.objectToStr(configPO));

    }

    @Test
    public void deletedConfigTest() {

        configBiz.deleteConfigInfo(1);
        log.info("*******getConfigById******");
    }

    @Test
    public void getCreateRuleConfigInfoTest(){

        Result<CreateRuleConfigResultDTO> result = ruleConfigService.getCreateRuleConfigInfo("76");
        log.info("*******getCreateRuleConfigInfo******"+JsonUtil.objectToStr(result));
    }



    @Test
    @Rollback(false)
    public void createCashCouponRuleConfig(){
        /**规则配置信息**/
        CashCouponRuleConfigPO ruleConfigBizParam=new CashCouponRuleConfigPO();
        /**区域配置信息**/
        List<CashCouponRegionConfigPO> regionConfigBizParams= Lists.newArrayList();
        /**商品配置信息**/
        List<CashCouponGoodsConfigPO> goodsConfigBizParams=Lists.newArrayList();
        ruleConfigBizParam.setOnlineCommercialMinfee(BigDecimal.ONE);
        ruleConfigBizParam.setOnlineCooperationMode(1);
        ruleConfigBizParam.setOnlineForcibleMinfee(BigDecimal.TEN);
        ruleConfigBizParam.setOnlineFormValidateDays(10);
        ruleConfigBizParam.setOfflineForcibleMinfee(BigDecimal.ONE);
        ruleConfigBizParam.setOfflineCooperationMode(1);
        ruleConfigBizParam.setOfflineCommercialMinfee(BigDecimal.TEN);
        ruleConfigBizParam.setOfflineFormValidateDays(15);
        ruleConfigBizParam.setFaceAmount(new BigDecimal(20));
        ruleConfigBizParam.setCashCouponValidateDays(10);
        ruleConfigBizParam.setCommercialRebateRatio(new BigDecimal(0.30));
        ruleConfigBizParam.setForcibleRebateRatio(new BigDecimal(0.08));
        ruleConfigBizParam.setRuleStatus(1);

        CashCouponRegionConfigPO regionConfigPO1=new CashCouponRegionConfigPO();
        regionConfigPO1.setProvinceName("浙江");
        regionConfigPO1.setProvinceCode("31");
        regionConfigPO1.setCityCode("383");
        regionConfigPO1.setCityName("杭州");
        regionConfigBizParams.add(regionConfigPO1);
        CashCouponRegionConfigPO regionConfigPO2=new CashCouponRegionConfigPO();
        regionConfigPO2.setProvinceName("上海");
        regionConfigPO2.setProvinceCode("25");
        regionConfigPO2.setCityCode("321");
        regionConfigPO2.setCityName("上海");
        regionConfigBizParams.add(regionConfigPO2);
        ruleConfigBizParam.setRegionConfigBizParams(regionConfigBizParams);

        CashCouponGoodsConfigPO goodsConfigPO1=new CashCouponGoodsConfigPO();
        goodsConfigPO1.setGoodsCoefficient(new BigDecimal(2));
        goodsConfigPO1.setGoodsType(1);
        goodsConfigBizParams.add(goodsConfigPO1);
        CashCouponGoodsConfigPO goodsConfigPO2=new CashCouponGoodsConfigPO();
        goodsConfigPO2.setGoodsCoefficient(new BigDecimal(2));
        goodsConfigPO2.setGoodsType(2);
        goodsConfigBizParams.add(goodsConfigPO2);
        ruleConfigBizParam.setGoodsConfigBizParams(goodsConfigBizParams);
        boolean ruleConfig = configBiz.createCashCouponRuleConfig(ruleConfigBizParam);
        Assert.assertTrue(ruleConfig);
    }

    @Test
    public void getAllOpenedRegionList(){
        Result<List<RegionListDTO>> result=configBiz.getAllOpenedRegionList();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void getCreateRuleConfigInfo(){
        String cityCode="383";
        CashCouponRuleConfigBO couponRuleConfigBO=configBiz.getCreateRuleConfigInfo(cityCode);
    }

}
