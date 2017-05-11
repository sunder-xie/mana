package com.tqmall.mana.test.biz.settle;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.mana.beans.BO.settle.SettleRateBO;
import com.tqmall.mana.beans.VO.settle.shopRule.*;
import com.tqmall.mana.beans.VO.settle.shopRule.add.AddSettleShopRuleItemVO;
import com.tqmall.mana.beans.VO.settle.shopRule.add.AddSettleShopRuleVO;
import com.tqmall.mana.beans.param.settle.SettleShopRulePageParam;
import com.tqmall.mana.beans.param.settle.ShopSettleRateQueryParam;
import com.tqmall.mana.biz.manager.settle.config.SettleShopRuleBiz;
import com.tqmall.mana.component.enums.insurance.dict.CalculateModeEnum;
import com.tqmall.mana.component.enums.insurance.dict.FundTypeEnum;
import com.tqmall.mana.component.enums.insurance.dict.RebateTypeEnum;
import com.tqmall.mana.component.enums.insurance.dict.SettleConditionEnum;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by huangzhangting on 17/1/16.
 */
public class SettleShopRuleBizTest extends BaseTest {
    @Autowired
    private SettleShopRuleBiz settleShopRuleBiz;


    @Test
    public void testAdd_1(){
        AddSettleShopRuleVO shopRuleVO = new AddSettleShopRuleVO();
        shopRuleVO.setCooperationMode(2);
        shopRuleVO.setInsuranceCompanyId(1);
        shopRuleVO.setInsuranceCompanyName("安心保险");
        shopRuleVO.setApplyRange(1);

        //配置地区
        List<SettleShopRuleRegionConfigVO> regionConfigVOList = new ArrayList<>();
        SettleShopRuleRegionConfigVO regionConfigVO = new SettleShopRuleRegionConfigVO();
        regionConfigVO.setCityCode("1234");
        regionConfigVO.setCityName("杭州");
        regionConfigVO.setProvinceCode("143");
        regionConfigVO.setProvinceName("浙江");
        regionConfigVOList.add(regionConfigVO);

        regionConfigVO = new SettleShopRuleRegionConfigVO();
        regionConfigVO.setCityCode("1235");
        regionConfigVO.setCityName("温州");
        regionConfigVO.setProvinceCode("143");
        regionConfigVO.setProvinceName("浙江");
        regionConfigVOList.add(regionConfigVO);

        shopRuleVO.setRegionConfigVOList(regionConfigVOList);

        //服务包规则
        AddSettleShopRuleItemVO packageRuleItem = new AddSettleShopRuleItemVO();
        packageRuleItem.setSettleCondition(SettleConditionEnum.PACKAGE_PAY_DATE.getCode());
        packageRuleItem.setFundType(FundTypeEnum.CASH.getCode());
        packageRuleItem.setRebateType(RebateTypeEnum.SERVICE_FEE.getCode());

        shopRuleVO.setPackageRuleItem(packageRuleItem);


        settleShopRuleBiz.addSettleShopRule(shopRuleVO);
    }

    @Test
    public void testAdd_2(){
        AddSettleShopRuleVO shopRuleVO = new AddSettleShopRuleVO();
        shopRuleVO.setCooperationMode(2);
        shopRuleVO.setInsuranceCompanyId(1);
        shopRuleVO.setInsuranceCompanyName("安心保险");
        shopRuleVO.setApplyRange(2);

        //配置门店
        SettleShopRuleShopConfigVO shopConfigVO = new SettleShopRuleShopConfigVO();
        shopConfigVO.setShopId(12345);
        shopConfigVO.setShopName("hzt测试门店");
        shopConfigVO.setShopAccount("12931oqiqwe");

        shopRuleVO.setShopConfigVO(shopConfigVO);

        //服务包规则
        AddSettleShopRuleItemVO packageRuleItem = new AddSettleShopRuleItemVO();
        packageRuleItem.setSettleCondition(SettleConditionEnum.PACKAGE_PAY_DATE.getCode());
        packageRuleItem.setFundType(FundTypeEnum.CASH.getCode());
        packageRuleItem.setRebateType(RebateTypeEnum.SERVICE_FEE.getCode());

        shopRuleVO.setPackageRuleItem(packageRuleItem);


        settleShopRuleBiz.addSettleShopRule(shopRuleVO);
    }


    @Test
    public void testAdd_3(){
        AddSettleShopRuleVO shopRuleVO = new AddSettleShopRuleVO();
        shopRuleVO.setCooperationMode(1);
        shopRuleVO.setInsuranceCompanyId(1);
        shopRuleVO.setInsuranceCompanyName("安心保险");
        shopRuleVO.setApplyRange(1);
        shopRuleVO.setSettleCondition(SettleConditionEnum.INSURED_DATE.getCode());

        //配置地区
        List<SettleShopRuleRegionConfigVO> regionConfigVOList = new ArrayList<>();
        SettleShopRuleRegionConfigVO regionConfigVO = new SettleShopRuleRegionConfigVO();
        regionConfigVO.setCityCode("2341589");
        regionConfigVO.setCityName("杭州");
        regionConfigVO.setProvinceCode("143");
        regionConfigVO.setProvinceName("浙江");
        regionConfigVOList.add(regionConfigVO);

        regionConfigVO = new SettleShopRuleRegionConfigVO();
        regionConfigVO.setCityCode("2351589");
        regionConfigVO.setCityName("温州");
        regionConfigVO.setProvinceCode("143");
        regionConfigVO.setProvinceName("浙江");
        regionConfigVOList.add(regionConfigVO);

        shopRuleVO.setRegionConfigVOList(regionConfigVOList);

        //商业险规则
        Date date = new Date();
        List<AddSettleShopRuleItemVO> bizRuleItemList = new ArrayList<>();
        AddSettleShopRuleItemVO ruleItemVO = new AddSettleShopRuleItemVO();
        ruleItemVO.setCalculateMode(CalculateModeEnum.SINGLE.getCode());
        ruleItemVO.setSettleConfigBasicId(36);
        ruleItemVO.setEndTime(date);
        bizRuleItemList.add(ruleItemVO);

        ruleItemVO = new AddSettleShopRuleItemVO();
        ruleItemVO.setCalculateMode(CalculateModeEnum.SINGLE.getCode());
        ruleItemVO.setSettleConfigBasicId(36);
        ruleItemVO.setEndTime(null);
        bizRuleItemList.add(ruleItemVO);

        ruleItemVO = new AddSettleShopRuleItemVO();
        ruleItemVO.setCalculateMode(CalculateModeEnum.SINGLE.getCode());
        ruleItemVO.setSettleConfigBasicId(36);
        ruleItemVO.setEndTime(addDays(date, 5));
        bizRuleItemList.add(ruleItemVO);

        ruleItemVO = new AddSettleShopRuleItemVO();
        ruleItemVO.setCalculateMode(CalculateModeEnum.SINGLE.getCode());
        ruleItemVO.setSettleConfigBasicId(36);
        ruleItemVO.setEndTime(addDays(date, 5));
        bizRuleItemList.add(ruleItemVO);

        shopRuleVO.setBizRuleItemList(bizRuleItemList);


        settleShopRuleBiz.addSettleShopRuleForReward(shopRuleVO);
    }


    // 测试数据修改接口
    @Test
    public void testModify_1(){
        ModifyShopRuleVO modifyShopRuleVO = new ModifyShopRuleVO();
        modifyShopRuleVO.setId(14);
        modifyShopRuleVO.setSettleCondition(SettleConditionEnum.SIGN_DATE.getCode());

        //配置地区
        List<SettleShopRuleRegionConfigVO> regionConfigVOList = new ArrayList<>();
        SettleShopRuleRegionConfigVO regionConfigVO = new SettleShopRuleRegionConfigVO();
        regionConfigVO.setCityCode("2341589");
        regionConfigVO.setCityName("杭州");
        regionConfigVO.setProvinceCode("143");
        regionConfigVO.setProvinceName("浙江");
        regionConfigVOList.add(regionConfigVO);

        regionConfigVO = new SettleShopRuleRegionConfigVO();
        regionConfigVO.setCityCode("2351589");
        regionConfigVO.setCityName("温州");
        regionConfigVO.setProvinceCode("143");
        regionConfigVO.setProvinceName("浙江");
        regionConfigVOList.add(regionConfigVO);

        modifyShopRuleVO.setRegionConfigVOList(regionConfigVOList);

        //商业险规则
        Date date = new Date();
        List<AddSettleShopRuleItemVO> bizRuleItemList = new ArrayList<>();
        AddSettleShopRuleItemVO ruleItemVO = new AddSettleShopRuleItemVO();
        ruleItemVO.setCalculateMode(CalculateModeEnum.SINGLE.getCode());
        ruleItemVO.setSettleConfigBasicId(36);
        ruleItemVO.setEndTime(date);
        bizRuleItemList.add(ruleItemVO);

        ruleItemVO = new AddSettleShopRuleItemVO();
        ruleItemVO.setCalculateMode(CalculateModeEnum.SINGLE.getCode());
        ruleItemVO.setSettleConfigBasicId(36);
        ruleItemVO.setEndTime(null);
        bizRuleItemList.add(ruleItemVO);

        ruleItemVO = new AddSettleShopRuleItemVO();
        ruleItemVO.setCalculateMode(CalculateModeEnum.SINGLE.getCode());
        ruleItemVO.setSettleConfigBasicId(36);
        ruleItemVO.setEndTime(addDays(date, 5));
        bizRuleItemList.add(ruleItemVO);

        ruleItemVO = new AddSettleShopRuleItemVO();
        ruleItemVO.setCalculateMode(CalculateModeEnum.SINGLE.getCode());
        ruleItemVO.setSettleConfigBasicId(36);
        ruleItemVO.setEndTime(addDays(date, 7));
        bizRuleItemList.add(ruleItemVO);


        modifyShopRuleVO.setBizRuleItemList(bizRuleItemList);


        settleShopRuleBiz.modifyShopRuleForReward(modifyShopRuleVO);
    }

    @Test
    public void testModify_2(){
        ModifyShopRuleVO modifyShopRuleVO = new ModifyShopRuleVO();
        modifyShopRuleVO.setId(6);

        //服务包规则
        AddSettleShopRuleItemVO packageRuleItem = new AddSettleShopRuleItemVO();
        packageRuleItem.setSettleCondition(SettleConditionEnum.PACKAGE_PAY_DATE.getCode());
        packageRuleItem.setFundType(FundTypeEnum.CASH.getCode());
        packageRuleItem.setRebateType(RebateTypeEnum.SERVICE_FEE.getCode());

        modifyShopRuleVO.setPackageRuleItem(packageRuleItem);


        settleShopRuleBiz.modifyShopRule(modifyShopRuleVO);
    }


    private Date addDays(Date date, int days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    private Date handleStartDate(Date date){
        if(date==null){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    private Date handleEndDate(Date date){
        if(date==null){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    @Test
    public void testDate(){
        Date date = new Date();
        Date endDate = handleEndDate(date);
        System.out.println(endDate.toString());
        Date startDate = handleStartDate(date);
        System.out.println(startDate.toString());
    }


    @Test
    public void testGetSettleRate(){
        // 门店配置了比例
        ShopSettleRateQueryParam param = new ShopSettleRateQueryParam();
        param.setInsuranceCompanyId(1);
        param.setCooperationMode(2);
        param.setShopId(1234);
        param.setRebateStandard(1);
        param.setInsuredFee(new BigDecimal(6000));

        // 区域配置了比例
        param = new ShopSettleRateQueryParam();
        param.setInsuranceCompanyId(1);
        param.setCooperationMode(1);
        param.setCityCode("2351589");
        param.setRebateStandard(1);
        param.setInsuredFee(new BigDecimal(6000));


        System.out.println("\n========== 开始测试，获取结算比例 ==========\n");

        SettleRateBO settleRateBO = settleShopRuleBiz.getSettleRate(param);
        System.out.println(settleRateBO);

        System.out.println("\n========== 获取结算比例，结束 ==========\n");

    }


    @Test
    public void testGetById(){
        Integer id = 10;
        SettleShopRuleVO shopRuleVO = settleShopRuleBiz.getSettleShopRule(id);
        System.out.println(JsonUtil.objectToStr(shopRuleVO));
    }

    @Test
    public void testQuery(){
        SettleShopRulePageParam param = new SettleShopRulePageParam();
        List<Integer> list = new ArrayList<>();
        list.add(1);

        param.setCooperationModes(list);
        PagingResult<SettleShopRuleVO> result = settleShopRuleBiz.queryShopRulePage(param);
        System.out.println(JsonUtil.objectToStr(result));
    }

}
