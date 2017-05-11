package com.tqmall.mana.biz.manager.outInsurance;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.outInsurance.InsuranceItemBO;
import com.tqmall.mana.beans.BO.outInsurance.PingAnBO;
import com.tqmall.mana.beans.entity.insurance.ManaInsuranceItemDO;
import com.tqmall.mana.beans.entity.insurance.OtherCityDO;
import com.tqmall.mana.beans.entity.insurance.OtherInsuranceRelationDO;
import com.tqmall.mana.biz.util.OtherCityUtils;
import com.tqmall.mana.biz.util.OtherRelationUtils;
import com.tqmall.mana.component.bean.HttpClientResult;
import com.tqmall.mana.component.util.DateUtil;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.component.util.http.HttpClientUtil;
import com.tqmall.mana.component.util.mana.ManaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zxg on 16/11/30.
 * 10:19
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Service
@Slf4j
public class PingAnInsuranceBizImpl implements PingAnInsuranceBiz {

    private final String FIRST_POST_URL = "http://www.4008000000.com/ebusiness/auto/newness/super-quick-quote.do";
    private static String SAVE_OTHER_INFO_POST_URL = "http://www.4008000000.com/ebusiness/car/newness/save-other-info.do?flowid=FLOW_ID&fromBase=true";
    private static String PRICE_LIMIT_GET_URL = "http://www.4008000000.com/ebusiness/car/newness/list-packages.do?flowid=FLOW_ID";
    private static String CALCULATE_POST_URL = "http://www.4008000000.com/ebusiness/car/newness/biz-calculate.do?flowid=FLOW_ID";
    private static String CAR_URL = "http://www.pingan.com/rsupport/vehicle/brand?k=KEY_WORD&page=PAGE&range=6&market_date=MARKET_DATA&_=THE_TIME";

    //平安计算保费的的入参
    private static List<String> pingAnNameList = Lists.newArrayList("amount01", "amount02","amount03","amount04","amount05","amount08","amount18","amount17","amount41","amount63",
            "amount27","amount28","amount48","amount49","amount80","amount75","amount77","amount79","amount57");


    @Override
    public Result<Map<ManaInsuranceItemDO,ManaInsuranceItemDO>> getSYData(PingAnBO pingAnBO) {
        //根据品牌型号处理车价和车的数据
        if(getCarByFormat(pingAnBO)) {
            //设置手机号
            pingAnBO.setMobilePhone(ManaUtil.getRandomTel());
            // 1. 获得唯一标识
            String flowId = null;
            try {
                flowId = getFlowId(pingAnBO);
                if(flowId == null){
                    return ResultUtil.errorResult("001","flowid is null");
                }
            } catch (Exception e) {
                log.error("getFlowId error",e);
                return ResultUtil.errorResult("000","getFlowId error,please try to find why");
            }

            // 2. 保存数据到平安服务器
            Boolean saveOtherInfoOK = saveOtherInfo(pingAnBO,flowId);
            if(!saveOtherInfoOK){
                return ResultUtil.errorResult("001","saveOtherInfo error");
            }
            // 3.获得金额限制
            HashMap<String,Integer> priceLimitMap = PriceLimit(flowId);
            // 4.计算金额
            Map<ManaInsuranceItemDO,ManaInsuranceItemDO> resultMap = calculate(priceLimitMap,pingAnBO,flowId);
            if(resultMap == null){
                return ResultUtil.errorResult("001","calculate pingAn fee error");
            }
            return Result.wrapSuccessfulResult(resultMap);
        }
        return ResultUtil.errorResult("000","getCarByFormat error,please try to find why");
    }

    // 1. 获得唯一标识
    private String getFlowId(PingAnBO pingAnBO) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        String getFlowId;
        // 处理参数，成请求的数据
        List<NameValuePair> nvpList = consolePinAnForFirst(pingAnBO);
        HttpClientResult saveResult = HttpClientUtil.post(FIRST_POST_URL, nvpList);
        if(saveResult == null || saveResult.getStatus() != 200){
            log.error("FIRST_POST_URL in PingAn wrong,the nvpList:{}.result:{}", nvpList,saveResult);
            return null;
        }
        // 不优雅，无法转xpath去做，待优化
        String str = saveResult.getData();
        Integer indexs = str.indexOf("name=\"WT.flowId\"");
        String the_result = str.substring(indexs + 24, indexs + 43);

        getFlowId = the_result.substring(the_result.indexOf("\"")+1,the_result.lastIndexOf("\""));

        return getFlowId;
    }

    // 2.保存数据到平安服务器
    private Boolean saveOtherInfo(PingAnBO pingAnBO,String flowId){
        String url = SAVE_OTHER_INFO_POST_URL.replaceAll("FLOW_ID",flowId);
        List<NameValuePair> nvpList = consolePinAnOtherInfo(pingAnBO);
        HttpClientResult saveResult = HttpClientUtil.post(url, nvpList);
        if(saveResult == null || saveResult.getStatus() != 200){
            log.error("saveOtherInfo in PingAn wrong,the nvpList:{},saveResult:{}", nvpList,saveResult);
            return false;
        }
        if(saveResult.getData().contains("toCtrl")){
            return true;
        }

        log.info("saveOtherInfo in PingAn wrong,the url:{},nvpList:{},result:{}", url,nvpList,saveResult);
        return false;
    }

    // 3.金额限制
    private HashMap<String,Integer> PriceLimit(String flowId){
        HashMap<String,Integer> resultMap = new HashMap<>();
        String url = PRICE_LIMIT_GET_URL.replaceAll("FLOW_ID", flowId);
        HttpClientResult saveResult = HttpClientUtil.get(url);
        if(saveResult == null || saveResult.getStatus() != 200){
            log.error("PriceLimit in PingAn wrong,the url:{}.saveResult:{}", url, saveResult);
            return resultMap;
        }
        JsonNode jsonNode = JsonUtil.strToObject(saveResult.getData(), JsonNode.class);

        List<String> needGetNameList = Lists.newArrayList("amount01Default", "amount01Max", "amount01Min", "amount03", "amount03Max", "amount03Min", "amount18", "amount18Max", "amount18Min");
        for(String name : needGetNameList) {
            JsonNode amountNode = jsonNode.get(name);
            if (amountNode != null) {
                if(!name.contains("Max") && !name.contains("Min") && !name.contains("Default")){
                    name = name+"Default";
                }
                resultMap.put(name,amountNode.asInt());
            }
        }

        return resultMap;
    }

    //4. 计算金额,返回 anxinName+basicId:ManaInsuranceItemDO
    private Map<ManaInsuranceItemDO,ManaInsuranceItemDO> calculate(HashMap<String,Integer> priceLimitMap,PingAnBO pingAnBO,String flowId){
        String url = CALCULATE_POST_URL.replaceAll("FLOW_ID", flowId);
//        String url = CALCULATE_POST_URL.replaceAll("FLOW_ID", "O0LZIEYRRLNe8wwr");

        // anxinName:ManaInsuranceItemDO
        Map<String,ManaInsuranceItemDO> manaInsuranceItemDOMap = new HashMap<>();
        // 处理参数，成请求的数据
        List<NameValuePair> nvpList = consoleCalculate(priceLimitMap, pingAnBO, manaInsuranceItemDOMap);
        HttpClientResult saveResult = HttpClientUtil.post(url, nvpList);
        if(saveResult == null ||saveResult.getStatus() != 200){
            log.error("CALCULATE_POST_URL in PingAn wrong,the nvpList:{}.saveResult:{}", nvpList,saveResult);
            return null;
        }
        String resultStr = saveResult.getData();

        // 金额叠加
        addPingAnFee(resultStr, manaInsuranceItemDOMap);

        //返回 anxinName+basicId:ManaInsuranceItemDO
        Map<ManaInsuranceItemDO,ManaInsuranceItemDO> anxinBasiMap = new HashMap<>();
        for(Map.Entry<String,ManaInsuranceItemDO> entry:manaInsuranceItemDOMap.entrySet()){
            ManaInsuranceItemDO saveDO = entry.getValue();
            ManaInsuranceItemDO keyDO = new ManaInsuranceItemDO();
            keyDO.setInsuranceName(saveDO.getInsuranceName());
            keyDO.setInsuranceBasicId(saveDO.getInsuranceBasicId());
            anxinBasiMap.put(keyDO,saveDO);
        }

        return anxinBasiMap;
    }

    /*====处理结果到map中=====*/
    public Boolean addPingAnFee(String resultStr, Map<String, ManaInsuranceItemDO> manaInsuranceItemDOMap){
        JsonNode jsonNode = JsonUtil.strToObject(resultStr, JsonNode.class);
        Iterator<String> iterator = jsonNode.fieldNames();
        while (iterator.hasNext()){
            String keyName = iterator.next();
            if(keyName.startsWith("premium")){
                String value = jsonNode.get(keyName).toString();
                // 找对对应的保险名称
                String pingAnKeyName = keyName.replace("premium", "amount");
                OtherInsuranceRelationDO anxinRelation = OtherRelationUtils.getInsuranceByPingAn(pingAnKeyName);
                if(anxinRelation == null){
                    log.info("the insurance not have OtherInsuranceRelationDO.insurance:{}",pingAnKeyName);
                    continue;
                }
                String anxinName = anxinRelation.getAnxinInsuranceName();
                // 叠加值
                ManaInsuranceItemDO manaInsuranceItemDO = manaInsuranceItemDOMap.get(anxinName);
                if(manaInsuranceItemDO == null){
                    continue;
                }
                BigDecimal insuranceFeePingAn = manaInsuranceItemDO.getInsuranceFeePingAn();
                if(insuranceFeePingAn == null) insuranceFeePingAn = BigDecimal.ZERO;
                insuranceFeePingAn = insuranceFeePingAn.add(new BigDecimal(value));
                manaInsuranceItemDO.setInsuranceFeePingAn(insuranceFeePingAn);
            }
        }
        return true;
    }


    /*======参数拼接=====*/
    // 处理参数，成请求的数据--第一次的请求
    private List<NameValuePair> consolePinAnForFirst(PingAnBO pingAnBO){
        List<NameValuePair> resultList = new ArrayList<>();
        // 处理价格为万，保留一位小数,四舍五入
        String carPrice = pingAnBO.getCarPrice();
        BigDecimal old = new BigDecimal(carPrice);
        BigDecimal newPrice = old.divide(new BigDecimal("10000"));
        resultList.add(new BasicNameValuePair("bizQuote.simpleVehiclePrice",newPrice.setScale(1,BigDecimal.ROUND_HALF_UP).toString()));
        // 车牌
        resultList.add(new BasicNameValuePair("vehicle.licenseNo",pingAnBO.getLicenseNo()));
        Date registerDate = pingAnBO.getRegisterDate();
        resultList.add(new BasicNameValuePair("firstRegisterYear",DateUtil.dateToString(registerDate, "yyyy")));
        resultList.add(new BasicNameValuePair("firstRegisterMonth",DateUtil.dateToString(registerDate, "MM")));
        resultList.add(new BasicNameValuePair("vehicle.registerDate",DateUtil.dateToString(registerDate, "yyyy-MM")));
        resultList.add(new BasicNameValuePair("applicant.mobile",pingAnBO.getMobilePhone()));



        // cityCode 和provinceCode 从数据库取
        String provinceCode = OtherCityUtils.getCodeByProvinceName(pingAnBO.getProvinceName().trim(), OtherCityDO.PING_AN_SOURCE);
        String cityCode;
        if(provinceCode != null){
            cityCode = OtherCityUtils.getCodeByCityName(pingAnBO.getCityName().trim(), OtherCityDO.PING_AN_SOURCE);
            if(cityCode == null) {
                cityCode = provinceCode.substring(0, 3) + "100";
            }
        }else {
            // 默认浙江省杭州市
            log.info("get city wrong,not in renbao,used hangzhou default .provinceName:{},cityName:{}",pingAnBO.getProvinceName().trim(),pingAnBO.getCityName().trim());
            provinceCode = "330000";
            cityCode = "330100";
        }
        resultList.add(new BasicNameValuePair("provinceCode",provinceCode));
        resultList.add(new BasicNameValuePair("cityCode",cityCode));

        // 常量
        resultList.add(new BasicNameValuePair("mediaSourse",PingAnBO.SESSION_NUMBER));
        resultList.add(new BasicNameValuePair("WT.mc_id",PingAnBO.SESSION_NUMBER));
        resultList.add(new BasicNameValuePair("vehicle.registered","newVehicle"));
        resultList.add(new BasicNameValuePair("register.name",""));
        resultList.add(new BasicNameValuePair("isAgency","true"));
        resultList.add(new BasicNameValuePair("isOnAgency","false"));
        return resultList;
    }


    private List<NameValuePair> consolePinAnOtherInfo(PingAnBO pingAnBO){
        List<NameValuePair> resultList = new ArrayList<>();
        resultList.add(new BasicNameValuePair("vehicle.frameNo",pingAnBO.getVinCode()));
        resultList.add(new BasicNameValuePair("vehicle.engineNo",pingAnBO.getEngineNo()));
        resultList.add(new BasicNameValuePair("vehicle.registerDate",DateUtil.dateToString(pingAnBO.getRegisterDate(), DateUtil.yyyy_MM_dd)));
        resultList.add(new BasicNameValuePair("register.name",pingAnBO.getPeopleName()));
        resultList.add(new BasicNameValuePair("applicant.mobile",pingAnBO.getMobilePhone()));
        resultList.add(new BasicNameValuePair("applicant.email",pingAnBO.getMobilePhone()+"@163.com"));
        resultList.add(new BasicNameValuePair("vehicle.price",pingAnBO.getCarPrice()));
        // 车的名称
        String carName = pingAnBO.getCarName();
        resultList.add(new BasicNameValuePair("circModel",carName));
        resultList.add(new BasicNameValuePair("modelNameDesc",carName));
        resultList.add(new BasicNameValuePair("vehicle.modelName",carName));

        resultList.add(new BasicNameValuePair("vehicle.parentModelId",pingAnBO.getCarParentModelId()));
        resultList.add(new BasicNameValuePair("vehicle.modelId",pingAnBO.getCarModelId()));


        //根据身份证推出出生年月
        String peopleCard = pingAnBO.getPeopleCard();
        Integer start_index = 6;
        String year = peopleCard.substring(start_index,start_index+4);
        String month = peopleCard.substring(start_index+4,start_index+6);
        String day = peopleCard.substring(start_index + 6, start_index + 8);
        resultList.add(new BasicNameValuePair("register.birthday",String.format("%s-%s-%s",year,month,day)));


        // 常量
        resultList.add(new BasicNameValuePair("register.gender","M"));//默认男
        resultList.add(new BasicNameValuePair("bizQuote.specialCarFlag","0"));
        resultList.add(new BasicNameValuePair("loanCarFlag","0"));
        resultList.add(new BasicNameValuePair("vehicle.importFlag","0"));


        return resultList;
    }

    private List<NameValuePair> consoleCalculate(HashMap<String,Integer> priceLimitMap,PingAnBO pingAnBO, Map<String,ManaInsuranceItemDO> manaInsuranceItemDOMap){
        List<NameValuePair> resultList = new ArrayList<>();
        Map<String,String> pingAnMap = consolePriceForInsurance(priceLimitMap, pingAnBO, manaInsuranceItemDOMap);
        for(Map.Entry<String ,String> entry:pingAnMap.entrySet()){
            resultList.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
        }
        //常量
        resultList.add(new BasicNameValuePair("pkgName","optional"));

        return resultList;
    }
    /*======end 参数拼接=====*/

    /* ======= start 保险价格处理====*/
    private Map<String,String> consolePriceForInsurance(HashMap<String,Integer> priceLimitMap,PingAnBO pingAnBO, Map<String,ManaInsuranceItemDO> manaInsuranceItemDOMap){
        BigDecimal amount01Default = new BigDecimal(priceLimitMap.get("amount01Default"));
        String amountDefaultString = getIntValue(amount01Default);

        Map<String,String> addResultMap = new HashMap<>();

        // 值赋值
        addResultMap.put("inputAmount",amountDefaultString);
        addResultMap.put("inputAmount03",amountDefaultString);
        addResultMap.put("inputAmount18",amountDefaultString);
        /**
         * amountName:保险种类名称
         * amountDeductible：是否不计免赔:0表示不记免赔,1表示记录免赔,2表示没有免赔选项
         * amountPrice:投保金额
         */
        Integer basicId = pingAnBO.getInsuranceBasicId();
        List<InsuranceItemBO> amountList = pingAnBO.getAmountList();

        for (InsuranceItemBO insuranceItemBO : amountList){
            String amountName = insuranceItemBO.getInsuranceName().replace("（", "(").replace("）", ")");
            Integer amountDeductible = insuranceItemBO.getDeductible();
            BigDecimal amountPrice = insuranceItemBO.getInsuranceAmount();

            // 设置之后要保存的内容
            ManaInsuranceItemDO manaInsuranceItemDO = new ManaInsuranceItemDO();
            manaInsuranceItemDO.setInsuranceBasicId(basicId);
            manaInsuranceItemDO.setInsuranceName(amountName);
//            manaInsuranceItemDO.setInsuranceType(ManaInsuranceItemDO.INSURANCE_TYPE_SY);
//            manaInsuranceItemDO.setIsDeductible(amountDeductible);
//            manaInsuranceItemDO.setInsuranceAmount(amountPrice);
            manaInsuranceItemDO.setInsuranceFeePingAn(BigDecimal.ZERO);
            manaInsuranceItemDOMap.put(amountName,manaInsuranceItemDO);

            OtherInsuranceRelationDO insuranceRelationDO = OtherRelationUtils.getInsuranceByAnXinName(amountName);
            if(insuranceRelationDO == null || StringUtils.isEmpty(insuranceRelationDO.getPingAnInsuranceName())){
                log.info("the insurance not have OtherInsuranceRelationDO.insurance:{}",amountName);
                manaInsuranceItemDO.setPingAnRemark(ManaInsuranceItemDO.NO_HAVE_REMARKS);
                continue;
            }

            //不计免赔赋值
            // s是否是不计免赔。true：打勾-不计免赔
            Boolean isAmountDeductible = amountDeductible.equals(0);
            String pinAnDeductible = insuranceRelationDO.getPingAnDeductible();
            if(!StringUtils.isEmpty(pinAnDeductible)){
                if(isAmountDeductible){
                    addResultMap.put(pinAnDeductible,"1");
                }else {
                    addResultMap.put(pinAnDeductible,"0");
                }
            }

            String addResultKey = insuranceRelationDO.getPingAnInsuranceName();
            String pingAnRemarks = ManaInsuranceItemDO.HAVE_REMARKS;
            // 投保金额
            switch (addResultKey) {
                //机动车损失保险
                case "amount01": {
                    if(amountPrice ==null || BigDecimal.ZERO.compareTo(amountPrice) == 0){
                        amountPrice = BigDecimal.ZERO;
                    }
                    BigDecimal amount01Max = new BigDecimal(priceLimitMap.get("amount01Max"));
                    BigDecimal amount01Min = new BigDecimal(priceLimitMap.get("amount01Min"));
                    BigDecimal newPrice = getTruePrice(amountPrice,amount01Min,amount01Max,amount01Default);
                    if(newPrice.compareTo(amountPrice) == 0){
                        // 在范围区间内
                        priceLimitMap.put("amount03Max",amountPrice.intValue());
                        priceLimitMap.put("amount18Max",amountPrice.intValue());
                    }else{//不在范围区间
                        amountPrice = newPrice;
                        pingAnRemarks = "投保金额:" + getIntValue(newPrice);
                    }
                    addResultMap.put("inputAmount", getIntValue(amountPrice));
                    addResultMap.put("inputAmount03", getIntValue(amountPrice));
                    addResultMap.put("inputAmount18", getIntValue(amountPrice));
                    addResultMap.put("amount01",getIntValue(amount01Max));
                    break;
                }
                //全车盗抢保险
                case "amount03": {
                    if(amountPrice !=null && BigDecimal.ZERO.compareTo(amountPrice) != 0) {
                        BigDecimal amount03Max = new BigDecimal(priceLimitMap.get("amount03Max"));
                        BigDecimal amount03Min = new BigDecimal(priceLimitMap.get("amount03Min"));
                        BigDecimal newPrice = getTruePrice(amountPrice, amount03Min, amount03Max,amount01Default);
                        if (newPrice.compareTo(amountPrice) != 0) {
                            pingAnRemarks = "投保金额:" +getIntValue(newPrice);
                            amountPrice = newPrice;
                        }
                        addResultMap.put("amount03", priceLimitMap.get("amount01Max").toString());
                    }
                    addResultMap.put("inputAmount03", getIntValue(amountPrice));

                    break;
                }
                //自燃损失险
                case "amount18": {
                    if (amountPrice == null || BigDecimal.ZERO.compareTo(amountPrice) == 0) {
                        amountPrice = BigDecimal.ZERO;
                    }
                    BigDecimal amount18Max = new BigDecimal(priceLimitMap.get("amount18Max"));
                    BigDecimal amount18Min = new BigDecimal(priceLimitMap.get("amount18Min"));
                    BigDecimal newPrice = getTruePrice(amountPrice, amount18Min, amount18Max,amount01Default);
                    if (newPrice.compareTo(amountPrice) != 0) {
                        pingAnRemarks = "投保金额:" + getIntValue(newPrice);
                        amountPrice = newPrice;
                    }
                    addResultMap.put("inputAmount18", getIntValue(amountPrice));
                    addResultMap.put("amount18", "1");
                    break;
                }
                //发动机涉水损失险
                case "amount41": {
                    if (amountPrice == null || BigDecimal.ZERO.compareTo(amountPrice) == 0) {
                        amountPrice = amount01Default;
                        pingAnRemarks = "投保金额:" + getIntValue(amountPrice);
                    }
                    addResultMap.put("amount41", getIntValue(amountPrice));
                    break;
                }
                default: {
                    if (amountPrice == null || BigDecimal.ZERO.compareTo(amountPrice) == 0) {
                        addResultMap.put(addResultKey, "1");
                    } else {
                        addResultMap.put(addResultKey, getIntValue(amountPrice));
                    }
                }
            }

            manaInsuranceItemDO.setPingAnRemark(pingAnRemarks);
        }

        //没有在里面的 全部默认 0
        for(String pinAnKeyName : pingAnNameList){
            if(!addResultMap.keySet().contains(pinAnKeyName)){
                addResultMap.put(pinAnKeyName,"0");
            }
        }

        return  addResultMap;
    }

    private String getIntValue(BigDecimal amountPrice) {
        return String.valueOf(amountPrice.intValue());
    }


    private BigDecimal getTruePrice(BigDecimal oldPrice,BigDecimal minPrice,BigDecimal maxPrice,BigDecimal defaultPrice){
        if(oldPrice == null || oldPrice.compareTo(BigDecimal.ZERO) == 0){
            return defaultPrice;
        }

        BigDecimal newPrice = oldPrice;
        if (oldPrice.compareTo(minPrice) == -1) {
            // 比最小值小，取最小值
            newPrice = minPrice;
        } else if (oldPrice.compareTo(maxPrice) == 1) {
            //比最大大，取最大值
            newPrice = maxPrice;
        }

        return newPrice;
    }
    /* ======= end 保险价格处理====*/

    /*==获得平安车型的相关信息===*/
    private Boolean getCarByFormat(PingAnBO pingAnBO){
        String VIN_CODE = pingAnBO.getVinCode();
        String KEY_WORD = pingAnBO.getCarBrandCode();

        Date now = new Date();
        String MARKET_DATA = DateUtil.dateToString(now, DateUtil.yyyyMM);
        String THE_TIME = String.valueOf(now.getTime());

        String carUrl = CAR_URL.replace("PAGE","1").replace("KEY_WORD", KEY_WORD).replace("MARKET_DATA", MARKET_DATA).replace("THE_TIME", THE_TIME);
        //请求获得数据
        HttpClientResult result = HttpClientUtil.get(carUrl);
        if(result == null || result.getStatus() != 200){
            log.error("get car in PingAn wrong,the url:{},result:{}", carUrl,result);
            return false;
        }
        JsonNode jsonNode = JsonUtil.strToObject(result.getData(), JsonNode.class);
        JsonNode results = jsonNode.get("results");
        if(results == null){
            log.info("get car in PingAn wrong,url:{},result:{}",carUrl,result.getData());
            return false;
        }
        JsonNode  vehicles = results.get("vehicles");


        List<HashMap<String,Object>> vehicleList = JsonUtil.jsonNodeToCollection(vehicles, List.class, HashMap.class);
        if(CollectionUtils.isEmpty(vehicleList)){
            log.info("get car in PingAn wrong,the list is empty,url:{},result:{}",carUrl,result.getData());
            return false;
        }

        JsonNode  pagination = results.get("pagination");
        Integer pages = pagination.get("pages").intValue();
        Integer nowPage = 1;
        while (nowPage < pages){
            nowPage = nowPage+1;
            carUrl = CAR_URL.replace("PAGE",nowPage.toString()).replace("KEY_WORD", KEY_WORD).replace("MARKET_DATA", MARKET_DATA).replace("THE_TIME", THE_TIME);
            //请求获得数据
            result = HttpClientUtil.get(carUrl);
            List<HashMap<String,Object>> nextList = JsonUtil.jsonNodeToCollection( JsonUtil.strToObject(result.getData(), JsonNode.class).get("results").get("vehicles"), List.class, HashMap.class);
            vehicleList.addAll(nextList);
        }

        Integer right_index = getIndexFromList(vehicleList, pingAnBO);//最合适的车型的下标
        HashMap<String,Object> rightMap = vehicleList.get(right_index);
        //赋值到需要的上面
        Integer price = (Integer) rightMap.get("price");
        if (price != null) {
            pingAnBO.setCarPrice(String.valueOf(price));
        }
        pingAnBO.setCarName((String) rightMap.get("standard_name"));
        pingAnBO.setCarParentModelId((String) rightMap.get("parent_id"));
        pingAnBO.setCarModelId((String) rightMap.get("vehicle_id"));

        return true;
    }

    private Integer getIndexFromList(List<HashMap<String,Object>> vehicleList,PingAnBO pingAnBO){
        Integer right_index = 0;//最合适的车型的下标
        Integer suit_max_num = 0;//跟平安车型对应上的数目
        String carDisplay = pingAnBO.getCarDisplay();
        String carGear = pingAnBO.getCarGear();
        String carType = pingAnBO.getCarType();
        String carSeatNum = pingAnBO.getCarSeatNum();
        String carYear = pingAnBO.getCarYear();

        Integer listSize = vehicleList.size();
        for(int i =0;i< listSize; i++){
            HashMap<String,Object> map = vehicleList.get(i);
            Integer suit_num = 0;
            String mapDisplay = (String) map.get("engine_desc");
            String mapGear = (String) map.get("gearbox_name");
            String mapTypeYear = (String) map.get("parent_veh_name");
            String mapSeatNum = map.get("seat") == null?null:String.valueOf(map.get("seat"));
            // 排量
            if (!StringUtils.isEmpty(carDisplay) && !StringUtils.isEmpty(mapDisplay) && mapDisplay.equals(carDisplay)){
                suit_num ++;
            }
            // 类型
            if (!StringUtils.isEmpty(carGear) && !StringUtils.isEmpty(mapGear) && mapGear.contains(carGear)) {
                suit_num ++;
            }
            // 座位号
            if (!StringUtils.isEmpty(carSeatNum) && !StringUtils.isEmpty(mapSeatNum) && mapSeatNum.contains(carSeatNum)) {
                suit_num ++;
            }
            if(!StringUtils.isEmpty(mapTypeYear)){
                // 类型
                if (!StringUtils.isEmpty(carType) && mapTypeYear.toUpperCase().contains(carType.toUpperCase())) {
                    suit_num ++;
                }
                // 年款
                if (!StringUtils.isEmpty(carYear) && mapTypeYear.contains(carYear)) {
                    suit_num ++;
                }
            }

            if(suit_num > suit_max_num){
                // 这个车型更合适
                suit_max_num = suit_num;
                right_index = i;
            }

        }

        return right_index;
    }
    /*===end 车型处理===*/

}
