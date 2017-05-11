package com.tqmall.mana.biz.manager.outInsurance;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.outInsurance.InsuranceItemBO;
import com.tqmall.mana.beans.BO.outInsurance.RenBaoBO;
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

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zxg on 16/12/7.
 * 16:10
 * no bug,以后改代码的哥们，祝你好运~！！
 * 入口：http://www.epicc.com.cn/wap/views/carProposal/carIndex_app.jsp
 */
@Service
@Slf4j
public class RenBaoInsuranceBizImpl implements RenBaoInsuranceBiz {

    private static final String FIRST_CHOOSE_CITY_URL = "http://www.epicc.com.cn/wap/carProposal/car/carInput1";
    private static final String SECOND_WRITE_PEOPLE_INFO_URL = "http://www.epicc.com.cn/wap/carProposal/car/carInput2";
    private static final String THIRD_CAR_SELECT_URL = "http://www.epicc.com.cn/wap/carProposal/carSelect/vehicleFind";
    private static final String THIRD_CAR_SAVE_URL = "http://www.epicc.com.cn/wap/carProposal/carSelect/vehicleChecked";
    private static final String FOURTH_UNDER_WRITE_URL = "http://www.epicc.com.cn/wap/carProposal/underWrite/underwriteCheckProfitAjax";
    private static final String FIFTH_LIMIT_PRICE_URL = "http://www.epicc.com.cn/wap/carProposal/calculateFee/fee";
    private static final String SIX_CALCULATE_URL = "http://www.epicc.com.cn/wap/carProposal/calculateFee/sy";

    //人保保费的的入参
    private static List<String> renbaoNameList = Lists.newArrayList("select_050200", "select_050600", "select_050500", "select_050701", "select_050702", "select_050310", "select_050231", "select_050270",
            "select_050210", "select_050252", "select_050291", "select_050911", "select_050912", "select_050921", "select_050922", "select_050924", "select_050928", "select_050330", "select_050935",
            "select_050918", "select_050919", "select_050917", "select_050451", "select_050642", "select_050641", "select_050643", "select_050929");

    // 核保重试次数-最多两次
    private Integer underwritingReTryNum = 0;
    private Integer underwritingReTryMaxNum = 1;

    // 已有的车型列表，避免多次重复请求
    List<Map<String, String>> carList = new ArrayList<>();

    @Override
    public Result<Map<ManaInsuranceItemDO, ManaInsuranceItemDO>> getSYData(RenBaoBO renBaoBO) {
        underwritingReTryNum = 0;
        carList = new ArrayList<>();
        // 初始化随机值
        String phone = ManaUtil.getRandomTel();
        String email = phone + "@163.com";
        renBaoBO.setMobilePhone(phone);
        renBaoBO.setEmail(email);
        // 1. 选城市,返回session ID；
        String sessionId = chooseCity(renBaoBO);
        if (sessionId == null) {
            log.info("chooseCity sessionId is null.please see why");
            return ResultUtil.errorResult("0001", "chooseCity to get session is fail");
        }
        renBaoBO.setSessionId(sessionId);
        // 2. 填写个人信息
        if (!writePersonInfo(renBaoBO)) {
            log.info("writePersonInfo fail");
            return ResultUtil.errorResult("0002", "writePersonInfo fail,the url is return not 200");
        }
        // 3. 选择车型
        if (!carSelect(renBaoBO)) {
            log.info("car select fail");
            return ResultUtil.errorResult("0003", "select car fail");
        }
        // 4. 核保
        if (!underwriting(renBaoBO)) {
            log.info("underwriting fail");
            return ResultUtil.errorResult("0004", "underwriting fail");
        }
        // 5. 金额限制
        Map<String, String> limitPriceMap = limitPrice(renBaoBO);
        if (limitPriceMap == null) {
            log.info("limitPrice fail");
            return ResultUtil.errorResult("0005", "limitPrice fail");
        }
        // 6. 计算金额
        Map<ManaInsuranceItemDO, ManaInsuranceItemDO> map = getFinalResultMap(renBaoBO, limitPriceMap);
        if (map == null) {
            log.info("calculate fail");
            return ResultUtil.errorResult("0006", "calculate fail");
        }


        return ResultUtil.successResult(map);
    }

    /*=== START 1. 选城市 获得唯一的sessionId===*/
    private String chooseCity(RenBaoBO renBaoBO) {
        String url = FIRST_CHOOSE_CITY_URL;
        List<NameValuePair> nvpList = consoleChooseCityParams(renBaoBO);
        HttpClientResult saveResult = HttpClientUtil.post(url, nvpList);
        assert saveResult != null;
        if (saveResult.getStatus() != 200) {
            log.info("chooseCity in RenBao wrong,the nvpList:{}", nvpList);
            return null;
        }
        // 不优雅，无法转xpath去做，待优化
        String str = saveResult.getData();
        Integer indexs = str.indexOf("name=\"head.sessionId\"");
        String the_result = str.substring(indexs + 26, indexs + 70);

        String sessionId = the_result.substring(the_result.indexOf("\"") + 1, the_result.lastIndexOf("\""));
        return sessionId;
    }

    // 处理 1 选择城市的参数
    private List<NameValuePair> consoleChooseCityParams(RenBaoBO renBaoBO) {
        List<NameValuePair> resultList = new ArrayList<>();

        //省份，人保无省份概念，因此走 先根据人保的城市去获得省份编码
        String cityName = renBaoBO.getCityName().trim().replace("市", "");

        String provinceCode = OtherCityUtils.getCodeByProvinceName(cityName, OtherCityDO.REN_BAO_SOURCE);
        String cityCode = OtherCityUtils.getCodeByCityName(cityName, OtherCityDO.REN_BAO_SOURCE);
        //若这个名称 人保没有，则使用平安的数据
        if (provinceCode == null) {
            String provinceName = renBaoBO.getProvinceName().trim();
            provinceCode = OtherCityUtils.getCodeByProvinceName(provinceName, OtherCityDO.PING_AN_SOURCE);
            // 如果平安 也没有就默认 杭州
            if (provinceCode != null) {
                provinceCode = provinceCode + "00";
                //判断此平安获得的code 是否存在于人保编码中,不存在，则默认杭州
                if (OtherCityUtils.isExistCode(provinceCode, OtherCityDO.REN_BAO_SOURCE)) {
                    cityCode = provinceCode.substring(0, 3) + "100";
                    log.info("get city wrong,not in renbao but have in pingan.default the provinceCode:{},cityCode:{}", provinceCode, cityCode);
                } else {
                    // 默认浙江省杭州市
                    log.info("get city wrong,not in renbao but have in pingan,the code not in renbao:{},used hangzhou default .provinceName:{},cityName:{}", provinceCode, provinceName, cityName);
                    provinceCode = "33000000";
                    cityCode = "33010000";
                }
            } else {
                // 默认浙江省杭州市
                log.info("get city wrong,not in renbao,used hangzhou default .provinceName:{},cityName:{}", provinceName, cityName);
                provinceCode = "33000000";
                cityCode = "33010000";
            }
        }

        renBaoBO.setProvinceCode(provinceCode);
        renBaoBO.setCityCode(cityCode);
        resultList.add(new BasicNameValuePair("proSelected", provinceCode));
        resultList.add(new BasicNameValuePair("citySelected", cityCode));

        // 固定值
        resultList.add(new BasicNameValuePair("head.requestCode", "20132001"));
        resultList.add(new BasicNameValuePair("head.uuid", "1234"));
        resultList.add(new BasicNameValuePair("head.sessionId", "first"));
        resultList.add(new BasicNameValuePair("head.channelNo", "1"));
        resultList.add(new BasicNameValuePair("carInfo.machineID", ""));
        resultList.add(new BasicNameValuePair("head.requestType", ""));
        resultList.add(new BasicNameValuePair("citySelect", ""));
        resultList.add(new BasicNameValuePair("netAddress", ""));
        resultList.add(new BasicNameValuePair("carInfo.ccaFlag", ""));
        resultList.add(new BasicNameValuePair("carInfo.ccaEntryId", ""));

        return resultList;
    }
    /*=== END 1. 选城市 获得唯一的sessionId===*/

    /*=== START 2. 填写个人信息===*/
    private Boolean writePersonInfo(RenBaoBO renBaoBO) {
        String url = SECOND_WRITE_PEOPLE_INFO_URL;
        List<NameValuePair> nvpList = consoleWriteInfoParams(renBaoBO);
        HttpClientResult saveResult = HttpClientUtil.post(url, nvpList);
        assert saveResult != null;
        if (saveResult.getStatus() != 200) {
            log.info("writePersonInfo in RenBao wrong,the nvpList:{}", nvpList);
            return false;
        }

        return true;
    }

    private List<NameValuePair> consoleWriteInfoParams(RenBaoBO renBaoBO) {
        List<NameValuePair> resultList = new ArrayList<>();
        // sessionId
        resultList.add(new BasicNameValuePair("head.sessionId", renBaoBO.getSessionId()));

        //车牌
        resultList.add(new BasicNameValuePair("carInfo.licenseNo", renBaoBO.getLicenseNo()));

        // 设置省份和城市
        String provinceCode = renBaoBO.getProvinceCode();
        String cityCode = renBaoBO.getCityCode();
        resultList.add(new BasicNameValuePair("carInfo.proSelected", provinceCode));
        resultList.add(new BasicNameValuePair("carInfo.citySelected", cityCode));
        resultList.add(new BasicNameValuePair("carInfo.areaCodeLast", provinceCode));
        resultList.add(new BasicNameValuePair("carInfo.cityCodeLast", cityCode));
        resultList.add(new BasicNameValuePair("proSelected", provinceCode));
        resultList.add(new BasicNameValuePair("citySelected", cityCode));

        //身份证
        String peopleCard = renBaoBO.getPeopleCard();
        Integer start_index = 6;
        String year = peopleCard.substring(start_index, start_index + 4);
        String month = peopleCard.substring(start_index + 4, start_index + 6);
        String day = peopleCard.substring(start_index + 6, start_index + 8);
        String birthday = String.format("%s/%s/%s", year, month, day);
        renBaoBO.setBirthday(birthday);
        resultList.add(new BasicNameValuePair("carInfo.insuredBirthday", birthday));
        resultList.add(new BasicNameValuePair("carInfo.insuredIdentifyType", "01"));
        resultList.add(new BasicNameValuePair("carInfo.insuredIdentifyNumber", peopleCard));
        resultList.add(new BasicNameValuePair("carInfo.appliIdentifyType", "01"));
        resultList.add(new BasicNameValuePair("carInfo.appliIdentifyNumber", peopleCard));
        resultList.add(new BasicNameValuePair("carInfo.carIdentifyType", "01"));
        resultList.add(new BasicNameValuePair("carInfo.carIdentifyNumber", peopleCard));

        //性别
        resultList.add(new BasicNameValuePair("carInfo.insuredIdentifSex", "1"));

        //邮箱和手机
        String email = renBaoBO.getEmail();
        String phone = renBaoBO.getMobilePhone();
        resultList.add(new BasicNameValuePair("carInfo.insuredMobile", phone));
        resultList.add(new BasicNameValuePair("carInfo.appliMobile", phone));
        resultList.add(new BasicNameValuePair("carInfo.appliEmail", email));
        resultList.add(new BasicNameValuePair("carInfo.insuredEmail", email));

        // 固定值
        resultList.add(new BasicNameValuePair("carInfo.lastCityCheck", "1"));
        resultList.add(new BasicNameValuePair("carInfo.invoiceHeadFlag", "0"));
        resultList.add(new BasicNameValuePair("head.requestCode", "20132004"));
        resultList.add(new BasicNameValuePair("head.uuid", "1234"));
        resultList.add(new BasicNameValuePair("head.channelNo", "1"));
        resultList.add(new BasicNameValuePair("carInfo.isRenewal", "0"));
        resultList.add(new BasicNameValuePair("carInfo.isNewCar", "1"));
        resultList.add(new BasicNameValuePair("carInfo.blanclistflag", "true"));
        resultList.add(new BasicNameValuePair("carInfo.isModify_t", "1"));
        resultList.add(new BasicNameValuePair("carInfo.isModify_b", "1"));
        resultList.add(new BasicNameValuePair("skipStep", "1"));
        resultList.add(new BasicNameValuePair("carInfo.switchOn_Off", "0"));
        return resultList;
    }
    /*=== END 2. 填写个人信息===*/

    /*=== START 3. 车型选择===*/
    private Boolean carSelect(RenBaoBO renBaoBO) {
        // 在所有人保车型里面筛选出 合适的车型
        String carParentId = getCarPentId(renBaoBO);
        if (carParentId == null) {
            return false;
        }
        // 保存车型信息到人保服务器
        return saveCarToRenBao(renBaoBO, carParentId);
    }

    private String getCarPentId(RenBaoBO renBaoBO) {
        String parentId;
        if (CollectionUtils.isEmpty(carList)) {
            // 参数拼接
            List<NameValuePair> carParamsList = new ArrayList<>();
            String vinNo = renBaoBO.getVinCode();
            carParamsList.add(new BasicNameValuePair("sessionId", renBaoBO.getSessionId()));
            carParamsList.add(new BasicNameValuePair("proSelected", renBaoBO.getProvinceCode()));
            carParamsList.add(new BasicNameValuePair("citySelected", renBaoBO.getCityCode()));
            carParamsList.add(new BasicNameValuePair("queryCode", renBaoBO.getCarBrandCode()));
            carParamsList.add(new BasicNameValuePair("licenseNo", renBaoBO.getLicenseNo()));
            carParamsList.add(new BasicNameValuePair("frameNo", vinNo));
            carParamsList.add(new BasicNameValuePair("vinNo", vinNo));
            carParamsList.add(new BasicNameValuePair("engineNo", renBaoBO.getEngineNo()));
            carParamsList.add(new BasicNameValuePair("enrollDate", DateUtil.dateToString(renBaoBO.getRegisterDate(), DateUtil.yyyy_MM_dd)));
            carParamsList.add(new BasicNameValuePair("channelNo", "1"));
            carParamsList.add(new BasicNameValuePair("licenseFlag", "1"));
            carParamsList.add(new BasicNameValuePair("isRenewal", "0"));
            HttpClientResult result = HttpClientUtil.post(THIRD_CAR_SELECT_URL, carParamsList);
            assert result != null;
            if (result.getStatus() != 200) {
                log.info("get car list in RenBao wrong not 200,the nvpList:{}", carParamsList);
                return null;
            }
            // 返回值处理,carList 保存进来
            carList = JsonUtil.strToList(result.getData(), Map.class);
            if (CollectionUtils.isEmpty(carList)) {
                log.info("get car list in RenBao is empty,result:{},the nvpList:{}", result.getData(), carParamsList);
                return null;
            }
        }

        Integer right_index = getIndexFromList(carList, renBaoBO);//最合适的车型的下标
        Map<String, String> rightMap = carList.get(right_index);
        // 获得parentId
        parentId = rightMap.get("parentId");

        String seat = rightMap.get("seat");
        if (seat != null && !renBaoBO.getCarSeatNum().equals(seat)) {
            log.info("seat changed.old:{},new:{}", renBaoBO.getCarSeatNum(), seat);
            renBaoBO.setCarSeatNum(seat);
        }

        return parentId;
    }

    private Integer getIndexFromList(List<Map<String, String>> carList, RenBaoBO renBaoBO) {
        Integer right_index = 0;//最合适的车型的下标
        Integer suit_max_num = 0;//跟平安车型对应上的数目
        String carType = renBaoBO.getCarType();
        String carSeatNum = renBaoBO.getCarSeatNum();
        String carYear = renBaoBO.getCarYear();

        Integer listSize = carList.size();
        for (int i = 0; i < listSize; i++) {
            Map<String, String> map = carList.get(i);
            Integer suit_num = 0;
            String mapSeatNum = map.get("seat") == null ? null : String.valueOf(map.get("seat"));
            String parentVehName = map.get("parentVehName");
            // 座位号
            if (!StringUtils.isEmpty(carSeatNum) && !StringUtils.isEmpty(mapSeatNum) && mapSeatNum.contains(carSeatNum)) {
                suit_num++;
            }
            if (!StringUtils.isEmpty(parentVehName)) {
                // 类型
                if (!StringUtils.isEmpty(carType) && parentVehName.toUpperCase().contains(carType.toUpperCase())) {
                    suit_num++;
                }
                // 年款
                if (!StringUtils.isEmpty(carYear) && parentVehName.contains(carYear)) {
                    suit_num++;
                }
            }

            if (suit_num > suit_max_num) {
                // 这个车型更合适
                suit_max_num = suit_num;
                right_index = i;
            }

        }

        return right_index;
    }

    private Boolean saveCarToRenBao(RenBaoBO renBaoBO, String carParentId) {
        // 参数拼接
        List<NameValuePair> carParamsList = new ArrayList<>();
        String carBrandCode = renBaoBO.getCarBrandCode();
        String vinNo = renBaoBO.getVinCode();
        carParamsList.add(new BasicNameValuePair("parentId", carParentId));
        carParamsList.add(new BasicNameValuePair("sessionId", renBaoBO.getSessionId()));
        carParamsList.add(new BasicNameValuePair("proSelected", renBaoBO.getProvinceCode()));
        carParamsList.add(new BasicNameValuePair("citySelected", renBaoBO.getCityCode()));
        carParamsList.add(new BasicNameValuePair("queryCode", carBrandCode));
        carParamsList.add(new BasicNameValuePair("carModel", carBrandCode));
        carParamsList.add(new BasicNameValuePair("seatCount", renBaoBO.getCarSeatNum()));
        carParamsList.add(new BasicNameValuePair("enrollDate", DateUtil.dateToString(renBaoBO.getRegisterDate(), DateUtil.yyyy_MM_dd)));
        carParamsList.add(new BasicNameValuePair("frameNo", vinNo));
        carParamsList.add(new BasicNameValuePair("vinNo", vinNo));
        carParamsList.add(new BasicNameValuePair("engineNo", renBaoBO.getEngineNo()));
        carParamsList.add(new BasicNameValuePair("channelNo", "1"));
        carParamsList.add(new BasicNameValuePair("carRequestType", "03"));
        carParamsList.add(new BasicNameValuePair("licenseFlag", "1"));
        HttpClientResult result = HttpClientUtil.post(THIRD_CAR_SAVE_URL, carParamsList);
        assert result != null;
        if (result.getStatus() != 200) {
            log.info("save car in RenBao wrong. not 200,the nvpList:{}", carParamsList);
            return false;
        }
        JsonNode jsonNode = JsonUtil.strToObject(result.getData(), JsonNode.class);
        JsonNode common = jsonNode.get("common");
        if (common == null) {
            log.info("save car in renbao wrong,result:{}", result.getData());
            return false;
        }
        String commonStr = common.toString().replace("\"", "").replace("\\", "\"");
        Map<String, String> resultMap = JsonUtil.strToObject(commonStr, HashMap.class);
        if (resultMap.containsKey("resultCode") && resultMap.get("resultCode").equals("1")) {
            return true;
        }
        log.info("save car in renbao wrong. resultCode not exist or not equals 1,result:{}", result.getData());
        return false;
    }
    /*=== END 3. 车型选择===*/

    /*=== START 4. 核保===*/
    private Boolean underwriting(RenBaoBO renBaoBO) {
        List<NameValuePair> paramsList = consoleUnderwritingParams(renBaoBO);
        HttpClientResult result = HttpClientUtil.post(FOURTH_UNDER_WRITE_URL, paramsList);
        assert result != null;
        if (result.getStatus() != 200) {
            log.info("underwriting in RenBao wrong. not 200,the nvpList:{}", paramsList);
            return false;
        }
        JsonNode jsonNode = JsonUtil.strToObject(result.getData(), JsonNode.class);
        JsonNode common = jsonNode.get("common");
        if (common == null) {
            log.info("underwriting in renbao wrong,result:{}", result.getData());
            return false;
        }
        String commonStr = common.toString().replace("\"", "").replace("\\", "\"");
        Map<String, String> resultMap = JsonUtil.strToObject(commonStr, HashMap.class);
        if (resultMap.containsKey("resultCode")) {
            String resultCode = resultMap.get("resultCode");
            if (resultCode.equals("1")) {
                return true;
            }
            String message = resultMap.get("resultMsg");
            if (resultCode.equals("3") || (message != null && message.contains("车型不一致"))) {
                log.info("car is not equal.retry.[{}]", result.getData());
                //车型不一致,重新赋值，重试
                return retryUnderwriting(jsonNode, renBaoBO);
            }
        }

        log.info("underwriting in renbao wrong,result:{}", result.getData());
        return false;
    }

    private List<NameValuePair> consoleUnderwritingParams(RenBaoBO renBaoBO) {
        List<NameValuePair> resultList = new ArrayList<>();
        resultList.add(new BasicNameValuePair("sessionId", renBaoBO.getSessionId()));

        //城市站
        String provinceCode = renBaoBO.getProvinceCode();
        String cityCode = renBaoBO.getCityCode();
        resultList.add(new BasicNameValuePair("proSelected", provinceCode));
        resultList.add(new BasicNameValuePair("citySelected", cityCode));
        resultList.add(new BasicNameValuePair("areaCodeLast", provinceCode));
        resultList.add(new BasicNameValuePair("cityCodeLast", cityCode));

        //时间
        Date now = new Date();
        Date startDate = DateUtil.getDaysBeforeOrAfter(now, null, null, 1);
        String startDateStr = DateUtil.dateToString(startDate, DateUtil.yyyy_MM_dd);
        resultList.add(new BasicNameValuePair("startdate", startDateStr));
        resultList.add(new BasicNameValuePair("starthour", "0"));
        Date nextYearDay = DateUtil.getDaysBeforeOrAfter(startDate, 1, null, -1);
        String endDateStr = DateUtil.dateToString(nextYearDay, DateUtil.yyyy_MM_dd);
        resultList.add(new BasicNameValuePair("enddate", endDateStr));
        resultList.add(new BasicNameValuePair("endhour", "24"));
        resultList.add(new BasicNameValuePair("transferdate", DateUtil.dateToString(now, "yyyy/MM/dd")));
        renBaoBO.setStartDateStr(startDateStr);
        renBaoBO.setEndDateStr(endDateStr);

        //
        String vinCode = renBaoBO.getVinCode();
        resultList.add(new BasicNameValuePair("licenseno", renBaoBO.getLicenseNo()));
        resultList.add(new BasicNameValuePair("engineno", renBaoBO.getEngineNo()));
        resultList.add(new BasicNameValuePair("vinno", vinCode));
        resultList.add(new BasicNameValuePair("frameno", vinCode));
        resultList.add(new BasicNameValuePair("seatcount", renBaoBO.getCarSeatNum()));
        resultList.add(new BasicNameValuePair("carOwner", renBaoBO.getPeopleName()));
        resultList.add(new BasicNameValuePair("enrolldate", DateUtil.dateToString(renBaoBO.getRegisterDate(), DateUtil.yyyy_MM_dd)));

        resultList.add(new BasicNameValuePair("channelNo", "1"));
        resultList.add(new BasicNameValuePair("isRenewal", "0"));
        resultList.add(new BasicNameValuePair("guohuselect", "0"));
        resultList.add(new BasicNameValuePair("licenseflag", "1"));
        resultList.add(new BasicNameValuePair("isOutRenewal", "0"));
        resultList.add(new BasicNameValuePair("lastHas050200", "0"));
        resultList.add(new BasicNameValuePair("lastHas050210", "0"));
        resultList.add(new BasicNameValuePair("lastHas050500", "0"));
        resultList.add(new BasicNameValuePair("seatflag", "1"));
        resultList.add(new BasicNameValuePair("guohuflag", "0"));
        resultList.add(new BasicNameValuePair("assignDriver", "2"));
        resultList.add(new BasicNameValuePair("haveLoan", "2"));
        String theNumber = renBaoBO.getPeopleCard();
        resultList.add(new BasicNameValuePair("insuredIdentifyNumber", theNumber));
        resultList.add(new BasicNameValuePair("appliIdentifyNumber", theNumber));
        resultList.add(new BasicNameValuePair("carIdentifyNumber", theNumber));
        resultList.add(new BasicNameValuePair("ccaID", ""));
        resultList.add(new BasicNameValuePair("ccaEntryId", ""));
        resultList.add(new BasicNameValuePair("ccaFlag", ""));
        resultList.add(new BasicNameValuePair("lastdamagedbi", ""));
        resultList.add(new BasicNameValuePair("runAreaCodeName", ""));
        resultList.add(new BasicNameValuePair("LoanName", ""));
        resultList.add(new BasicNameValuePair("weiFaName", ""));
        resultList.add(new BasicNameValuePair("carDrivers", ""));
        resultList.add(new BasicNameValuePair("oldPolicyNo", ""));
        resultList.add(new BasicNameValuePair("certificatedateSH", ""));
        resultList.add(new BasicNameValuePair("lastcarownername", ""));

        return resultList;
    }

    private Boolean retryUnderwriting(JsonNode result, RenBaoBO renBaoBO) {
        if (underwritingReTryNum > underwritingReTryMaxNum) {
            log.info("underwriting in renbao wrong,underwritingReTryNum:{} > maxNum:{} .result:{}", underwritingReTryNum, underwritingReTryMaxNum, result.toString());

            return false;
        }
        underwritingReTryNum += 1;
        JsonNode carModelAndNameNode = result.get("carModelAndName");
        if (carModelAndNameNode == null) {
            log.info("carModelAndNameNode is null");
            return false;
        }
        String carModelAndName = carModelAndNameNode.toString();
        String carBrandCode = renBaoBO.getCarBrandCode();
        Integer index = carModelAndName.indexOf(carBrandCode);
        String the_result = carModelAndName.substring(index + carBrandCode.length());
        if (!StringUtils.isEmpty(the_result)) {
            renBaoBO.setCarType(the_result.replace("\"", ""));
        }
        // 3. 选择车型
        carSelect(renBaoBO);
        // 4. 核保
        return underwriting(renBaoBO);
    }
    /*=== END 4. 核保===*/

    /*=== Start 5. 金额限制===*/
    private Map<String, String> limitPrice(RenBaoBO renBaoBO) {
        List<NameValuePair> paramsList = getBasePriceParams(renBaoBO);
        HttpClientResult result = HttpClientUtil.post(FIFTH_LIMIT_PRICE_URL, paramsList);
        assert result != null;
        if (result.getStatus() != 200) {
            log.info("limitPrice in RenBao wrong. not 200,the nvpList:{}", paramsList);
            return null;
        }
        JsonNode jsonNode = JsonUtil.strToObject(result.getData(), JsonNode.class);
        JsonNode common = jsonNode.get("common");
        if (common == null) {
            log.info("limitPrice in renbao wrong,result:{}", result.getData());
            return null;
        }
        String commonStr = common.toString().replace("\"", "").replace("\\", "\"");
        Map<String, String> commonMap = JsonUtil.strToObject(commonStr, HashMap.class);
        if (!commonMap.containsKey("resultCode") || !commonMap.get("resultCode").equals("1")) {
            log.info("limitPrice in renbao wrong,not have resultCode == 1,result:{}", result.getData());
            return null;
        }

        JsonNode priceConfigNode = jsonNode.get("priceConfig");
        Map<String, String> priceConfig = JsonUtil.strToObject(priceConfigNode.toString(), HashMap.class);

        Map<String, String> limitPriceMap = new HashMap<>();
        limitPriceMap.put("defaultPrice", priceConfig.get("purchasePrice"));
        limitPriceMap.put("maxPrice", priceConfig.get("purchasePriceMax"));
        limitPriceMap.put("minPrice", priceConfig.get("purchasePriceMin"));

        return limitPriceMap;
    }

    // 获得 基础的金额查询的参数--给 5 和6用
    private List<NameValuePair> getBasePriceParams(RenBaoBO renBaoBO) {
        List<NameValuePair> resultList = new ArrayList<>();

        resultList.add(new BasicNameValuePair("sessionId", renBaoBO.getSessionId()));

        //城市站
        String provinceCode = renBaoBO.getProvinceCode();
        String cityCode = renBaoBO.getCityCode();
        resultList.add(new BasicNameValuePair("proSelected", provinceCode));
        resultList.add(new BasicNameValuePair("citySelected", cityCode));
        resultList.add(new BasicNameValuePair("areaCodeLast", provinceCode));
        resultList.add(new BasicNameValuePair("cityCodeLast", cityCode));

        resultList.add(new BasicNameValuePair("mobile", renBaoBO.getMobilePhone()));
        resultList.add(new BasicNameValuePair("email", renBaoBO.getEmail()));
        resultList.add(new BasicNameValuePair("identifytype", "01"));
        resultList.add(new BasicNameValuePair("identifynumber", renBaoBO.getPeopleCard()));
        resultList.add(new BasicNameValuePair("birthday", renBaoBO.getBirthday()));

        resultList.add(new BasicNameValuePair("startdate", renBaoBO.getStartDateStr()));
        resultList.add(new BasicNameValuePair("starthour", "0"));
        resultList.add(new BasicNameValuePair("enddate", renBaoBO.getEndDateStr()));
        resultList.add(new BasicNameValuePair("endhour", "24"));

        resultList.add(new BasicNameValuePair("licenseno", renBaoBO.getLicenseNo()));
        resultList.add(new BasicNameValuePair("engineno", renBaoBO.getEngineNo()));
        String vinCode = renBaoBO.getVinCode();
        resultList.add(new BasicNameValuePair("vinno", vinCode));
        resultList.add(new BasicNameValuePair("frameno", vinCode));
        resultList.add(new BasicNameValuePair("seatCount", renBaoBO.getCarSeatNum()));
        resultList.add(new BasicNameValuePair("enrolldate", DateUtil.dateToString(renBaoBO.getRegisterDate(), DateUtil.yyyy_MM_dd)));
        resultList.add(new BasicNameValuePair("insuredname", renBaoBO.getPeopleName()));

        resultList.add(new BasicNameValuePair("sex", "1"));
        resultList.add(new BasicNameValuePair("channelNo", "1"));
        resultList.add(new BasicNameValuePair("isRenewal", "0"));
        resultList.add(new BasicNameValuePair("nonlocalflag", "01"));
        resultList.add(new BasicNameValuePair("licenseflag", "1"));
        resultList.add(new BasicNameValuePair("newcarflag", "0"));
        resultList.add(new BasicNameValuePair("isOutRenewal", "0"));
        resultList.add(new BasicNameValuePair("lastHas050200", "0"));
        resultList.add(new BasicNameValuePair("lastHas050210", "0"));
        resultList.add(new BasicNameValuePair("lastHas050500", "0"));
        resultList.add(new BasicNameValuePair("seatflag", "1"));
        resultList.add(new BasicNameValuePair("transfervehicleflag", "0"));
        resultList.add(new BasicNameValuePair("guohuselect", "0"));
        resultList.add(new BasicNameValuePair("assignDriver", "2"));
        resultList.add(new BasicNameValuePair("haveLoan", "2"));

        return resultList;
    }
    /*=== End 5. 金额限制===*/

    /*=== start 6. 计算金额===*/
    private Map<ManaInsuranceItemDO, ManaInsuranceItemDO> getFinalResultMap(RenBaoBO renBaoBO, Map<String, String> limitPriceMap) {
        // 获得基本参数
        List<NameValuePair> paramsList = getBasePriceParams(renBaoBO);
        // 获得要计算的参数
        // anxinName:保存的安心的DO
        Map<String, ManaInsuranceItemDO> insuranceItemDOMap = new HashMap<>();

        List<NameValuePair> jsList = consoleSYParams(renBaoBO, limitPriceMap, insuranceItemDOMap);
        paramsList.addAll(jsList);

        HttpClientResult result = HttpClientUtil.post(SIX_CALCULATE_URL, paramsList);
        assert result != null;
        if (result.getStatus() != 200) {
            log.info("getFinalResultMap in RenBao wrong. not 200,the nvpList:{}", paramsList);
            return null;
        }
        JsonNode jsonNode = JsonUtil.strToObject(result.getData(), JsonNode.class);
        JsonNode common = jsonNode.get("common");
        if (common == null) {
            log.info("getFinalResultMap in renbao wrong,result:{}", result.getData());
            return null;
        }
        String commonStr = common.toString().replace("\"", "").replace("\\", "\"");
        Map<String, String> commonMap = JsonUtil.strToObject(commonStr, HashMap.class);
        if (!commonMap.containsKey("resultCode") || !commonMap.get("resultCode").equals("1")) {
            log.info("getFinalResultMap renbao wrong,not have resultCode == 1,result:{}", result.getData());
            return null;
        }

        JsonNode commonPackage = jsonNode.get("commonPackage");
        Map<String, Object> commonPackageMap = JsonUtil.strToObject(commonPackage.toString(), HashMap.class);
        List<Map<String, String>> itemsList = (List<Map<String, String>>) commonPackageMap.get("items");

        // 金额叠加
        addRenBaoFee(itemsList, insuranceItemDOMap);

        //返回 anxinName+basicId:ManaInsuranceItemDO
        Map<ManaInsuranceItemDO, ManaInsuranceItemDO> anxinBasiMap = new HashMap<>();
        for (Map.Entry<String, ManaInsuranceItemDO> entry : insuranceItemDOMap.entrySet()) {
            ManaInsuranceItemDO saveDO = entry.getValue();
            ManaInsuranceItemDO keyDO = new ManaInsuranceItemDO();
            keyDO.setInsuranceName(saveDO.getInsuranceName());
            keyDO.setInsuranceBasicId(saveDO.getInsuranceBasicId());
            anxinBasiMap.put(keyDO, saveDO);
        }

        return anxinBasiMap;
    }

    private List<NameValuePair> consoleSYParams(RenBaoBO renBaoBO, Map<String, String> limitPriceMap, Map<String, ManaInsuranceItemDO> insuranceItemDOMap) {
        List<NameValuePair> resultList = new ArrayList<>();
        Map<String, String> pingAnMap = consolePriceForInsurance(renBaoBO, limitPriceMap, insuranceItemDOMap);
        for (Map.Entry<String, String> entry : pingAnMap.entrySet()) {
            resultList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        //常量
        resultList.add(new BasicNameValuePair("transferdate", DateUtil.dateToString(new Date(), "yyyy/MM/dd")));
        resultList.add(new BasicNameValuePair("BZ_selected", "2"));

        return resultList;
    }

    private Map<String, String> consolePriceForInsurance(RenBaoBO renBaoBO, Map<String, String> limitPriceMap, Map<String, ManaInsuranceItemDO> insuranceItemDOMap) {
        Map<String, String> resultMap = new HashMap<>();

        BigDecimal defaultPrice = limitPriceMap.get("defaultPrice") == null ? BigDecimal.ZERO : new BigDecimal(limitPriceMap.get("defaultPrice"));
        BigDecimal maxPrice = limitPriceMap.get("maxPrice") == null ? BigDecimal.ZERO : new BigDecimal(limitPriceMap.get("maxPrice"));
        BigDecimal minPrice = limitPriceMap.get("minPrice") == null ? BigDecimal.ZERO : new BigDecimal(limitPriceMap.get("minPrice"));

        Integer basicId = renBaoBO.getInsuranceBasicId();
        List<InsuranceItemBO> amountList = renBaoBO.getAmountList();

        for (InsuranceItemBO insuranceItemBO : amountList) {
            String amountName = insuranceItemBO.getInsuranceName().replace("（", "(").replace("）", ")");
            Integer amountDeductible = insuranceItemBO.getDeductible();
            BigDecimal amountPrice = insuranceItemBO.getInsuranceAmount();

            // 设置之后要保存的内容
            ManaInsuranceItemDO manaInsuranceItemDO = new ManaInsuranceItemDO();
            manaInsuranceItemDO.setInsuranceBasicId(basicId);
            manaInsuranceItemDO.setInsuranceName(amountName);
            manaInsuranceItemDO.setInsuranceFeeRenBao(BigDecimal.ZERO);
            insuranceItemDOMap.put(amountName, manaInsuranceItemDO);

            OtherInsuranceRelationDO insuranceRelationDO = OtherRelationUtils.getInsuranceByAnXinName(amountName);
            if (insuranceRelationDO == null || StringUtils.isEmpty(insuranceRelationDO.getRenInsuranceName())) {
                log.info("the insurance not have OtherInsuranceRelationDO.insurance:{}", amountName);
                manaInsuranceItemDO.setRenBaoRemark(ManaInsuranceItemDO.NO_HAVE_REMARKS);
                continue;
            }

            //不计免赔赋值
            // s是否是不计免赔。true：打勾-不计免赔
            Boolean isAmountDeductible = amountDeductible.equals(0);
            String renDeductible = insuranceRelationDO.getRenDeductible();
            if (!StringUtils.isEmpty(renDeductible)) {
                if (isAmountDeductible) {
                    resultMap.put(renDeductible, "1");
                } else {
                    resultMap.put(renDeductible, "-1");
                }
            }


            String addResultKey = insuranceRelationDO.getRenInsuranceName();
            String renRemarks = ManaInsuranceItemDO.HAVE_REMARKS;
            // 车损险
            if (addResultKey.equals("select_050200")) {
                if (amountPrice == null || BigDecimal.ZERO.compareTo(amountPrice) == 0) {
                    amountPrice = BigDecimal.ZERO;
                }
                BigDecimal newPrice = getTruePrice(amountPrice, minPrice, maxPrice, defaultPrice);
                if (newPrice.compareTo(amountPrice) == 0) {
                    // 在范围区间内
                    defaultPrice = newPrice;// 设置默认值
                } else {//不在范围区间
                    amountPrice = newPrice;
                    renRemarks = "投保金额:" + getDoubleValue(newPrice);
                }
                resultMap.put(addResultKey, getDoubleValue(amountPrice));
            } else // 盗抢险和 自然损失险
                if (addResultKey.equals("select_050500") || addResultKey.equals("select_050310")) {

                    if (amountPrice == null || BigDecimal.ZERO.compareTo(amountPrice) == 0) {
                        amountPrice = BigDecimal.ZERO;
                    }
                    if (amountPrice.compareTo(defaultPrice) != 0) {
                        // 不等于默认值
                        amountPrice = defaultPrice;
                        renRemarks = "投保金额:" + getDoubleValue(defaultPrice);
                    }
                    resultMap.put(addResultKey, getDoubleValue(amountPrice));
                } else //玻璃破碎险
                    if (addResultKey.equals("select_050231")) {
                        resultMap.put(addResultKey, "10");
                    } else //第三者责任险 车上人员责任险-司机+乘客 精神损害抚慰金责任险 车身划痕损失险 允许金额
                        if (addResultKey.equals("select_050600") || addResultKey.equals("select_050701") || addResultKey.equals("select_050702") || addResultKey.equals("select_050643") || addResultKey.equals("select_050210")) {
                            resultMap.put(addResultKey, getIntValue(amountPrice));
                        } else { // 不允许金额
                            if (amountPrice == null || BigDecimal.ZERO.compareTo(amountPrice) == 0) {
                                renRemarks = "不支持金额，勾选获得";
                            }
                            resultMap.put(addResultKey, "1");
                        }

            manaInsuranceItemDO.setRenBaoRemark(renRemarks);
        }
        // 固定值
        resultMap.put("select_050270", "");
        resultMap.put("select_050330", "");
        resultMap.put("select_050918", "");
        resultMap.put("select_050919", "");
        resultMap.put("select_050642", "");
        resultMap.put("select_050641", "");
        //没有在里面的 全部默认 -1
        for (String renKeyName : renbaoNameList) {
            if (!resultMap.keySet().contains(renKeyName)) {
                resultMap.put(renKeyName, "-1");
            }
        }

        return resultMap;

    }


    // 叠加金额
    private void addRenBaoFee(List<Map<String, String>> itemsList, Map<String, ManaInsuranceItemDO> insuranceItemDOMap) {
        for (Map<String, String> itemMap : itemsList) {
            String code = itemMap.get("kindCode");
            String price = itemMap.get("premium");
            code = "select_" + code;
            OtherInsuranceRelationDO insuranceByRenBao = OtherRelationUtils.getInsuranceByRenBao(code);
            if (insuranceByRenBao == null) {
                log.info("the insurance not have OtherInsuranceRelationDO.insurance:{}", code);
                continue;
            }
            ManaInsuranceItemDO manaInsuranceItemDO = insuranceItemDOMap.get(insuranceByRenBao.getAnxinInsuranceName());
            // 叠加值
            if (manaInsuranceItemDO == null) {
                continue;
            }


            BigDecimal insuranceFeeRenBao = manaInsuranceItemDO.getInsuranceFeeRenBao();
            if (insuranceFeeRenBao == null) insuranceFeeRenBao = BigDecimal.ZERO;
            insuranceFeeRenBao = insuranceFeeRenBao.add(price == null ? BigDecimal.ZERO : new BigDecimal(price));
            manaInsuranceItemDO.setInsuranceFeeRenBao(insuranceFeeRenBao);
        }
    }
    /*=== end 6. 计算金额===*/

    private String getIntValue(BigDecimal amountPrice) {
        return String.valueOf(amountPrice.intValue());
    }

    private String getDoubleValue(BigDecimal amountPrice) {
        //保留2位小数
        return amountPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    private BigDecimal getTruePrice(BigDecimal oldPrice, BigDecimal minPrice, BigDecimal maxPrice, BigDecimal defaultPrice) {
        if (oldPrice == null || oldPrice.compareTo(BigDecimal.ZERO) == 0) {
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

}
