package com.tqmall.mana.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.mana.beans.BO.insurance.InsuranceDicBO;
import com.tqmall.mana.component.bean.HttpClientResult;
import com.tqmall.mana.component.util.DateUtil;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.component.util.http.HttpClientUtil;
import com.tqmall.mana.component.util.mana.ManaUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by zxg on 16/12/1.
 * 14:47
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class PingAnJustTest {

    @Test
    public void testTime() {
        String CAR_URL = "http://www.4008000000.com/rsupport/vehicle/model-vincode/VIN_CODE?k=KEY_WORD&page=1&range=6&market_date=MARKET_DATA&_=THE_TIME";
        String VIN_CODE = "LGJF1FE09FT398042";
        String KEY_WORD = "EQ";

        Date now = new Date();
        String MARKET_DATA = DateUtil.dateToString(now, DateUtil.yyyyMM);
        String THE_TIME = String.valueOf(now.getTime());

        String carUrl = CAR_URL.replace("VIN_CODE", VIN_CODE).replace("KEY_WORD", KEY_WORD).replace("MARKET_DATA", MARKET_DATA).replace("THE_TIME", THE_TIME);
        //请求获得数据

        HttpClientResult result = HttpClientUtil.get(carUrl);
        if (result.getStatus() != 200) {
            System.out.println("get car in PingAn wrong,the url:" + carUrl);
        }
        JsonNode jsonNode = JsonUtil.strToObject(result.getData(), JsonNode.class);
        JsonNode results = jsonNode.get("results");
        JsonNode vehicles = results.get("vehicles");

        List<HashMap<String, String>> vehicleList = JsonUtil.jsonNodeToCollection(vehicles, List.class, HashMap.class);

        System.out.println("hh");
    }


    @Test
    public void testBigDECIMAL() {
        String carPrice = "74400";
        BigDecimal old = new BigDecimal(carPrice);
        BigDecimal newPrice = old.divide(new BigDecimal("10000"));
        System.out.println(newPrice.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
    }

    @Test
    public void testRandomPhone() {
        for (int i = 0; i < 100; i++) {
            System.out.println(ManaUtil.getRandomTel());
        }
    }


    @Test
    public void testXMl()throws Exception{
        String str = readString3("/Users/zxg/Desktop/renbao.html");

        Integer indexs = str.indexOf("name=\"head.sessionId\"");
        String the_result = str.substring(indexs + 26, indexs + 70);
        System.out.println(the_result);
        System.out.println(the_result.substring(the_result.indexOf("\"") + 1, the_result.lastIndexOf("\"")));
//
//        StringReader sr = new StringReader(str);
//        InputSource is = new InputSource(sr);
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder= factory.newDocumentBuilder();
//        org.w3c.dom.Document doc = builder.parse(is);
//        String productsXpath = "//input[@id='sessionId']/@value/text()"; //xpath语句
//        org.w3c.dom.Node products = XPathAPI.selectSingleNode(doc, productsXpath);

        System.out.println("111");

    }

    private static String readString3(String FILE_IN) {
        String str = "";
        File file = new File(FILE_IN);
        try {
            FileInputStream in = new FileInputStream(file);
            // size  为字串的长度 ，这里一次性读完
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            str = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return str;

    }

    @Test
    public void testCardToBirthDay(){
        String str = "432622197602113374";
        Integer start_index = 6;
        String year = str.substring(start_index,start_index+4);
        String month = str.substring(start_index+4,start_index+6);
        String day = str.substring(start_index+6,start_index+8);

        System.out.println(year+"-"+month+"-"+day);
    }

    @Test
    public void testJson(){
        String str = "{\"resultCode\":0,\"amount01Default\":73668,\"amount01Max\":88402,\"amount01Min\":73668,\"amount03\":73668,\"amount03Max\":73668,\"amount03Min\":14734,\"amount18\":73668,\"amount18Max\":73668,\"amount18Min\":14734,\"seatsNum05\":4,\"economical\":{\"amount01\":73668,\"amount02\":500000,\"amount03\":0,\"amount04\":10000,\"amount05\":10000,\"amount08\":0,\"amount17\":0,\"amount18\":0,\"amount27\":1,\"amount28\":1,\"amount48\":0,\"amount49\":1,\"inputAmount\":73668,\"amount41\":0,\"amount57\":0,\"amount63\":1,\"amount75\":0,\"amount77\":0,\"amount79\":0,\"amount80\":1},\"recommend\":{\"amount01\":73668,\"amount02\":500000,\"amount03\":0,\"amount04\":10000,\"amount05\":10000,\"amount08\":0,\"amount17\":0,\"amount18\":73668,\"amount27\":1,\"amount28\":1,\"amount48\":0,\"amount49\":1,\"inputAmount\":73668,\"amount41\":73668,\"amount57\":0,\"amount63\":1,\"amount75\":0,\"amount77\":1,\"amount79\":1,\"amount80\":1},\"luxury\":{\"amount01\":73668,\"amount02\":1000000,\"amount03\":73668,\"amount04\":50000,\"amount05\":50000,\"amount08\":1,\"amount17\":0,\"amount18\":73668,\"amount27\":1,\"amount28\":1,\"amount48\":1,\"amount49\":1,\"inputAmount\":73668,\"amount41\":73668,\"amount57\":0,\"amount63\":1,\"amount75\":0,\"amount77\":1,\"amount79\":1,\"amount80\":1},\"importFlag\":0,\"selected\":\"recommend\"}";
        JsonNode jsonNode = JsonUtil.strToObject(str, JsonNode.class);
        Integer amount01Default = jsonNode.get("amount01Default").asInt();
        System.out.println(amount01Default);
    }

    @Test
    public void testJsonResult(){
        String str = "{\"pkgName\":\"optional\",\"premiumTotal\":2243.34,\"originalPremium\":4297.71,\"savedPremium\":2054.37,\"premium01\":971.61,\"premium02\":0,\"premium03\":0,\"premium04\":0,\"premium05\":958,\"premium08\":0,\"premium17\":0,\"premium18\":0,\"premium27\":145.74,\"premium28\":0,\"premium48\":0,\"premium49\":0,\"premium41\":0,\"premium57\":0,\"premium63\":24.29,\"premium10\":0,\"premium29\":0,\"premium75\":0,\"premium77\":0,\"premium79\":0,\"premium80\":143.7,\"lowPriceFlag\":false,\"resultCode\":0}";
        JsonNode jsonNode = JsonUtil.strToObject(str, JsonNode.class);
        Iterator<String> iterator = jsonNode.fieldNames();
        while (iterator.hasNext()){
            String keyName = iterator.next();
            if(keyName.contains("premium")){

            }
        }
        System.out.println("666");
    }

    @Test
    public void testCode(){
        String provinceCode = "330000";
        System.out.println(provinceCode.substring(0, 3) + "100");

        String str = "123123，,,,，，12312321，123123，123123，12312，";
        str = str.replace("，", ",");
        System.out.println(str);
        String[] strs = str.split(",");
        System.out.println(strs.length);
        System.out.println(JsonUtil.objectToStr(strs));

    }

    @Test
    public void testBigDecimal(){
        BigDecimal a = BigDecimal.ZERO;
        BigDecimal b = new BigDecimal("0.00");
        System.out.println("=======start====");

        System.out.println(a.equals(b));
        System.out.println(a.compareTo(b));
        System.out.println("=======end====");
    }

    @Test
    public void testRenbaoCarSelect(){
        String str = "[{\"familyName\":\"景逸S50\",\"parentId\":\"4028b2b6482124f10148340143d20c79\",\"parentVehName\":\"2014款 旗舰型\",\"picPath\":\"/home/ecar/jy/pics/group/DFC9AB01/small/small_DFC9AB01_2.jpg\",\"price\":\"102900.0\",\"seat\":\"5\",\"vehicleFgwCode\":\"EQ7160LS1B1\"},{\"familyName\":\"景逸S50\",\"parentId\":\"4028b2b651b4996e015239fd779151fb\",\"parentVehName\":\"2016款 旗舰型\",\"picPath\":\"/home/ecar/jy/pics/group/DFC9AB01/small/small_DFC9AB01_2.jpg\",\"price\":\"102900.0\",\"seat\":\"5\",\"vehicleFgwCode\":\"EQ7160LS1B1\"},{\"familyName\":\"景逸S50\",\"parentId\":\"4028b2b651b4996e015239f9d35151f2\",\"parentVehName\":\"2016款 尊享型\",\"picPath\":\"/home/ecar/jy/pics/group/DFC9AB01/small/small_DFC9AB01_2.jpg\",\"price\":\"87900.0\",\"seat\":\"5\",\"vehicleFgwCode\":\"EQ7160LS1B1\"},{\"familyName\":\"景逸S50\",\"parentId\":\"4028b2b651b4996e015239fc861d51f7\",\"parentVehName\":\"2016款 豪华型\",\"picPath\":\"/home/ecar/jy/pics/group/DFC9AB01/small/small_DFC9AB01_2.jpg\",\"price\":\"79900.0\",\"seat\":\"5\",\"vehicleFgwCode\":\"EQ7160LS1B1\"},{\"familyName\":\"景逸S50\",\"parentId\":\"4028b2b6482124f101483401ac9e0c7e\",\"parentVehName\":\"2014款 尊享型\",\"picPath\":\"/home/ecar/jy/pics/group/DFC9AB01/small/small_DFC9AB01_2.jpg\",\"price\":\"87900.0\",\"seat\":\"5\",\"vehicleFgwCode\":\"EQ7160LS1B1\"}]";
        List<Map<String,String>> carList = JsonUtil.strToList(str, HashMap.class);
        System.out.println(carList.size());
    }
    @Test
    public void testRenbaoCarSave(){
        String str = "{\"aliasName\":\"\",\"aliasNameViewFlag\":\"0\",\"assignDriverFlag\":\"\",\"carKindName\":\"--\",\"common\":\"{\\\"resultCode\\\":\\\"1\\\",\\\"resultMsg\\\":\\\"成功\\\"}\",\"countryNature\":\"01\",\"insuredIdentifyNumber\":\"--\",\"insuredName\":\"--\",\"len\":0,\"maxAmount\":0,\"riskcName\":\"--\",\"runAreaCodeFlag\":\"\",\"seatFlag\":\"1\",\"standardName\":\"景逸S50\"}";
        JsonNode jsonNode = JsonUtil.strToObject(str, JsonNode.class);
        JsonNode common = jsonNode.get("common");
        String commonStr = common.toString().replace("\"", "").replace("\\", "\"");
        System.out.println(commonStr);
        Map<String ,String> resultDict = JsonUtil.strToObject(commonStr, HashMap.class);
        String resultCode = resultDict.get("resultCode").toString();
        System.out.println(resultCode);
    }


    @Test
    public void testFeeRenBao(){
        String str = "{\"basicPackage\":[{\"amountList\":\"\",\"benchMarkPremium\":\"3973.05\",\"discountPremium\":\"4674.18\",\"isModify\":\"false\",\"isTouBao\":\"false\",\"kindCode\":\"EconomyPackage\",\"premium\":\"2439.86\"}],\"calcBIFlag\":\"1\",\"carKindName\":\"--\",\"common\":\"{\\\"resultCode\\\":\\\"1\\\",\\\"resultMsg\\\":\\\"成功\\\"}\",\"commonPackage\":{\"adjustAll\":\"false\",\"items\":[{\"amount\":\"74147.20\",\"amountList\":\"0|74147.20\",\"benchMarkPremium\":\"1380.55\",\"discountPremium\":\"1380.55\",\"isModify\":\"false\",\"isTouBao\":\"true\",\"kindCode\":\"050200\",\"kindName\":\"机动车辆损失险\",\"premium\":\"847.80\"},{\"amount\":\"500000.00\",\"amountList\":\"0|50000|100000|150000|200000|300000|500000|1000000|1500000|2000000|3000000|4000000|5000000|6000000\",\"benchMarkPremium\":\"1546.00\",\"discountPremium\":\"1546.00\",\"isModify\":\"false\",\"isTouBao\":\"true\",\"kindCode\":\"050600\",\"kindName\":\"第三者责任险\",\"premium\":\"949.40\"},{\"amount\":\"74147.20\",\"amountList\":\"0|74147.20\",\"benchMarkPremium\":\"483.32\",\"discountPremium\":\"483.32\",\"isModify\":\"false\",\"isTouBao\":\"true\",\"kindCode\":\"050500\",\"kindName\":\"盗抢险\",\"premium\":\"296.81\"},{\"amount\":\"0\",\"amountList\":\"0|10000|20000|30000|40000|50000|80000|100000|150000|200000|250000|300000|350000|400000|450000|500000|1000000|2000000\",\"isModify\":\"false\",\"isTouBao\":\"false\",\"kindCode\":\"050701\",\"kindName\":\"车上人员责任险-司机\"},{\"amount\":\"40000.00\",\"amountList\":\"0|10000|20000|30000|40000|50000|80000|100000|150000|200000|250000|300000|350000|400000|450000|500000|1000000|2000000\",\"benchMarkPremium\":\"108.00\",\"discountPremium\":\"108.00\",\"isModify\":\"false\",\"isTouBao\":\"true\",\"kindCode\":\"050702\",\"kindName\":\"车上人员责任险-乘客\",\"premium\":\"66.32\"},{\"amount\":\"0\",\"amountList\":\"0|74147.20\",\"isModify\":\"false\",\"isTouBao\":\"false\",\"kindCode\":\"050310\",\"kindName\":\"自燃损失险\"},{\"amount\":\"0\",\"amountList\":\"0|10|20\",\"isModify\":\"false\",\"isTouBao\":\"false\",\"kindCode\":\"050231\",\"kindName\":\"玻璃单独破碎险\"},{\"amount\":\"0\",\"amountList\":\"0|2000|5000\",\"isModify\":\"false\",\"isTouBao\":\"false\",\"kindCode\":\"050210\",\"kindName\":\"车身划痕损失险\"},{\"amount\":\"0\",\"amountList\":\"\",\"isModify\":\"false\",\"isTouBao\":\"false\",\"kindCode\":\"050900\",\"kindName\":\"不计免赔率特约条款\"},{\"amount\":\"0\",\"amountList\":\"0|1\",\"isModify\":\"false\",\"isTouBao\":\"false\",\"kindCode\":\"050252\",\"kindName\":\"指定修理厂险\"},{\"amount\":\"0\",\"amountList\":\"0|1\",\"isModify\":\"false\",\"isTouBao\":\"false\",\"kindCode\":\"050291\",\"kindName\":\"发动机涉水损失险\"},{\"amount\":\"0\",\"amountList\":\"0|1\",\"isModify\":\"false\",\"isTouBao\":\"false\",\"kindCode\":\"050451\",\"kindName\":\"机动车损失保险无法找到第三方特约险\"},{\"amount\":\"0\",\"amountList\":\"0|10000|20000|30000|40000|50000\",\"isModify\":\"false\",\"isTouBao\":\"false\",\"kindCode\":\"050643\",\"kindName\":\"精神损害抚慰金责任险\"},{\"amount\":\"1\",\"amountList\":\"0|1\",\"benchMarkPremium\":\"207.08\",\"discountPremium\":\"207.08\",\"isModify\":\"false\",\"isTouBao\":\"true\",\"kindCode\":\"050911\",\"kindName\":\"机动车辆损失险\",\"premium\":\"127.17\"},{\"amount\":\"1\",\"amountList\":\"0|1\",\"benchMarkPremium\":\"231.90\",\"discountPremium\":\"231.90\",\"isModify\":\"false\",\"isTouBao\":\"true\",\"kindCode\":\"050912\",\"kindName\":\"第三者责任险\",\"premium\":\"142.41\"},{\"amount\":\"0\",\"amountList\":\"0|1\",\"isModify\":\"false\",\"isTouBao\":\"false\",\"kindCode\":\"050921\",\"kindName\":\"盗抢险\"},{\"amount\":\"0\",\"amountList\":\"0|1\",\"isModify\":\"false\",\"isTouBao\":\"false\",\"kindCode\":\"050928\",\"kindName\":\"车上人员责任险-司机\"},{\"amount\":\"1\",\"amountList\":\"0|1\",\"benchMarkPremium\":\"16.20\",\"discountPremium\":\"16.20\",\"isModify\":\"false\",\"isTouBao\":\"true\",\"kindCode\":\"050929\",\"kindName\":\"车上人员责任险-乘客\",\"premium\":\"9.95\"},{\"amount\":\"0\",\"amountList\":\"0|1\",\"isModify\":\"false\",\"isTouBao\":\"false\",\"kindCode\":\"050935\",\"kindName\":\"自燃损失险\"},{\"amount\":\"0\",\"amountList\":\"0|1\",\"isModify\":\"false\",\"isTouBao\":\"false\",\"kindCode\":\"050922\",\"kindName\":\"车身划痕损失险\"},{\"amount\":\"0\",\"amountList\":\"0|1\",\"isModify\":\"false\",\"isTouBao\":\"false\",\"kindCode\":\"050924\",\"kindName\":\"发动机涉水损失险\"},{\"amount\":\"0\",\"amountList\":\"0|1\",\"isModify\":\"false\",\"isTouBao\":\"false\",\"kindCode\":\"050917\",\"kindName\":\"精神损害抚慰金责任险\"}],\"packageName\":\"EconomyPackage\"},\"insuredIdentifyNumber\":\"--\",\"insuredName\":\"--\",\"len\":0,\"maxAmount\":0,\"noticeTip1\":\"\",\"noticeTip2\":\"\",\"noticeTip3\":\"\",\"noticeTip4\":\"\",\"priceConfig\":{\"actualPrice\":\"79900\",\"bmflag\":\"0\",\"carReduceFlag\":\"1\",\"purchasePrice\":\"74147.2\",\"purchasePriceByDeclinature\":\"0\",\"purchasePriceMax\":\"96391.36\",\"purchasePriceMin\":\"51903.04\",\"userPriceConf\":\"1\"},\"riskcName\":\"--\",\"theftProtMax\":\"68808.60\",\"theftProtMin\":\"14829.44\",\"trafficViolation\":\"\",\"trafficViolationTwo\":\"\"}";
        JsonNode jsonNode = JsonUtil.strToObject(str, JsonNode.class);
        JsonNode common = jsonNode.get("common");
        String commonStr = common.toString().replace("\"", "").replace("\\","\"");
        System.out.println(commonStr);
        Map<String ,String> commonMap = JsonUtil.strToObject(commonStr, HashMap.class);


        JsonNode priceConfigNode = jsonNode.get("priceConfig");
        Map<String,String> priceConfig = JsonUtil.strToObject(priceConfigNode.toString(),HashMap.class);

        Map<String,String> limitPriceMap = new HashMap<>();
        limitPriceMap.put("defaultPrice",priceConfig.get("purchasePrice"));
        limitPriceMap.put("maxPrice",priceConfig.get("purchasePriceMax"));
        limitPriceMap.put("minPrice",priceConfig.get("purchasePriceMin"));

        System.out.println(limitPriceMap.toString());
    }

    @Test
    public void testjsRenBao(){
        String str ="{\"basicPackage\":[{\"amountList\":\"\",\"benchMarkPremium\":\"4085.24\",\"discountPremium\":\"4806.16\",\"isModify\":\"false\",\"isTouBao\":\"false\",\"kindCode\":\"EconomyPackage\",\"premium\":\"4085.24\"}],\"calcBIFlag\":\"\",\"carKindName\":\"--\",\"common\":\"{\\\"resultCode\\\":\\\"1\\\",\\\"resultMsg\\\":\\\"成功\\\"}\",\"commonPackage\":{\"adjustAll\":\"true\",\"items\":[{\"amount\":\"79900.00\",\"amountList\":\"0|79900.00\",\"benchMarkPremium\":\"1380.55\",\"discountPremium\":\"1380.55\",\"isModify\":\"true\",\"isTouBao\":\"true\",\"kindCode\":\"050200\",\"kindName\":\"机动车辆损失险\",\"premium\":\"1380.55\"},{\"amount\":\"500000.00\",\"amountList\":\"0|50000|100000|150000|200000|300000|500000|1000000|1500000|2000000|3000000|4000000|5000000|6000000\",\"benchMarkPremium\":\"1546.00\",\"discountPremium\":\"1546.00\",\"isModify\":\"true\",\"isTouBao\":\"true\",\"kindCode\":\"050600\",\"kindName\":\"第三者责任险\",\"premium\":\"1546.00\"},{\"amount\":\"79900.00\",\"amountList\":\"0|79900.00\",\"benchMarkPremium\":\"511.51\",\"discountPremium\":\"511.51\",\"isModify\":\"true\",\"isTouBao\":\"true\",\"kindCode\":\"050500\",\"kindName\":\"盗抢险\",\"premium\":\"511.51\"},{\"amount\":\"20000.00\",\"amountList\":\"0|10000|20000|30000|40000|50000|80000|100000|150000|200000|250000|300000|350000|400000|450000|500000|1000000|2000000\",\"benchMarkPremium\":\"84.00\",\"discountPremium\":\"84.00\",\"isModify\":\"true\",\"isTouBao\":\"true\",\"kindCode\":\"050701\",\"kindName\":\"车上人员责任险-司机\",\"premium\":\"84.00\"},{\"amount\":\"40000.00\",\"amountList\":\"0|10000|20000|30000|40000|50000|80000|100000|150000|200000|250000|300000|350000|400000|450000|500000|1000000|2000000\",\"benchMarkPremium\":\"108.00\",\"discountPremium\":\"108.00\",\"isModify\":\"true\",\"isTouBao\":\"true\",\"kindCode\":\"050702\",\"kindName\":\"车上人员责任险-乘客\",\"premium\":\"108.00\"},{\"amount\":\"0\",\"amountList\":\"0|79900.00\",\"isModify\":\"true\",\"isTouBao\":\"false\",\"kindCode\":\"050310\",\"kindName\":\"自燃损失险\"},{\"amount\":\"0\",\"amountList\":\"0|10|20\",\"isModify\":\"true\",\"isTouBao\":\"false\",\"kindCode\":\"050231\",\"kindName\":\"玻璃单独破碎险\"},{\"amount\":\"0\",\"amountList\":\"0|2000|5000\",\"isModify\":\"true\",\"isTouBao\":\"false\",\"kindCode\":\"050210\",\"kindName\":\"车身划痕损失险\"},{\"amount\":\"0\",\"amountList\":\"\",\"isModify\":\"true\",\"isTouBao\":\"false\",\"kindCode\":\"050900\",\"kindName\":\"不计免赔率特约条款\"},{\"amount\":\"0\",\"amountList\":\"0|1\",\"isModify\":\"true\",\"isTouBao\":\"false\",\"kindCode\":\"050252\",\"kindName\":\"指定修理厂险\"},{\"amount\":\"0\",\"amountList\":\"0|1\",\"isModify\":\"true\",\"isTouBao\":\"false\",\"kindCode\":\"050291\",\"kindName\":\"发动机涉水损失险\"},{\"amount\":\"0\",\"amountList\":\"0|1\",\"isModify\":\"true\",\"isTouBao\":\"false\",\"kindCode\":\"050451\",\"kindName\":\"机动车损失保险无法找到第三方特约险\"},{\"amount\":\"0\",\"amountList\":\"0|10000|20000|30000|40000|50000\",\"isModify\":\"true\",\"isTouBao\":\"false\",\"kindCode\":\"050643\",\"kindName\":\"精神损害抚慰金责任险\"},{\"amount\":\"1\",\"amountList\":\"0|1\",\"benchMarkPremium\":\"207.08\",\"discountPremium\":\"207.08\",\"isModify\":\"true\",\"isTouBao\":\"true\",\"kindCode\":\"050911\",\"kindName\":\"机动车辆损失险\",\"premium\":\"207.08\"},{\"amount\":\"1\",\"amountList\":\"0|1\",\"benchMarkPremium\":\"231.90\",\"discountPremium\":\"231.90\",\"isModify\":\"true\",\"isTouBao\":\"true\",\"kindCode\":\"050912\",\"kindName\":\"第三者责任险\",\"premium\":\"231.90\"},{\"amount\":\"0\",\"amountList\":\"0|1\",\"isModify\":\"true\",\"isTouBao\":\"false\",\"kindCode\":\"050921\",\"kindName\":\"盗抢险\"},{\"amount\":\"0\",\"amountList\":\"0|1\",\"isModify\":\"true\",\"isTouBao\":\"false\",\"kindCode\":\"050928\",\"kindName\":\"车上人员责任险-司机\"},{\"amount\":\"1\",\"amountList\":\"0|1\",\"benchMarkPremium\":\"16.20\",\"discountPremium\":\"16.20\",\"isModify\":\"true\",\"isTouBao\":\"true\",\"kindCode\":\"050929\",\"kindName\":\"车上人员责任险-乘客\",\"premium\":\"16.20\"},{\"amount\":\"0\",\"amountList\":\"0|1\",\"isModify\":\"true\",\"isTouBao\":\"false\",\"kindCode\":\"050935\",\"kindName\":\"自燃损失险\"},{\"amount\":\"0\",\"amountList\":\"0|1\",\"isModify\":\"true\",\"isTouBao\":\"false\",\"kindCode\":\"050922\",\"kindName\":\"车身划痕损失险\"},{\"amount\":\"0\",\"amountList\":\"0|1\",\"isModify\":\"true\",\"isTouBao\":\"false\",\"kindCode\":\"050924\",\"kindName\":\"发动机涉水损失险\"},{\"amount\":\"0\",\"amountList\":\"0|1\",\"isModify\":\"true\",\"isTouBao\":\"false\",\"kindCode\":\"050917\",\"kindName\":\"精神损害抚慰金责任险\"}],\"packageName\":\"EconomyPackage\"},\"insuredIdentifyNumber\":\"--\",\"insuredName\":\"--\",\"len\":0,\"maxAmount\":0,\"noticeTip1\":\"\",\"noticeTip2\":\"已经承保盗抢险的新车，注册牌照非“粤A”号牌，不承担盗抢险保险责任\",\"noticeTip3\":\"\",\"noticeTip4\":\"\",\"priceConfig\":{\"actualPrice\":\"79900\",\"bmflag\":\"0\",\"carReduceFlag\":\"1\",\"purchasePrice\":\"74147.2\",\"purchasePriceByDeclinature\":\"0\",\"purchasePriceMax\":\"96391.36\",\"purchasePriceMin\":\"51903.04\",\"userPriceConf\":\"1\"},\"riskcName\":\"--\",\"theftProtMax\":\"79900.00\",\"theftProtMin\":\"15980.000\",\"trafficViolation\":\"\",\"trafficViolationTwo\":\"\"}";

        JsonNode jsonNode = JsonUtil.strToObject(str, JsonNode.class);
        JsonNode common = jsonNode.get("common");
        String commonStr = common.toString().replace("\"", "").replace("\\","\"");
        System.out.println(commonStr);
        Map<String ,String> commonMap = JsonUtil.strToObject(commonStr, HashMap.class);


        JsonNode commonPackage = jsonNode.get("commonPackage");
        Map<String,Object> commonPackageMap = JsonUtil.strToObject(commonPackage.toString(), HashMap.class);
        List<Map<String,String>> itemsList = (List<Map<String, String>>) commonPackageMap.get("items");

        System.out.println(itemsList.size());
    }

    @Test
    public void testList(){
        List<String> renbaoNameList = Lists.newArrayList("select_050200", "select_050600", "select_050500", "select_050701", "select_050702", "select_050310", "select_050231", "select_050270",
                "select_050210", "select_050252", "select_050291", "select_050911", "select_050912", "select_050921", "select_050922", "select_050924", "select_050928", "select_050330", "select_050935",
                "select_050918", "select_050919", "select_050917", "select_050451", "select_050642", "select_050641", "select_050643", "select_050929");

        System.out.println("===== size:"+renbaoNameList.size());
    }

    @Test
    public void testSub(){
        String str = "carModelBFXCJZUB0003CarName东风EQ7160LS1B1豪华型";
        String key = "EQ7160LS1B1";
        Integer index = str.indexOf(key);
        String the_result = str.substring(index + key.length());
        System.out.println(the_result);

        String a = "2016款\" 豪华\"型\"";
        System.out.printf(a.replaceAll("\"+", ""));
    }


    @Test
    public void testLong(){
        Long test = 10*24*3600*1000L;
        System.out.println(test);
    }

    @Test
    public void testTime11(){
        Date date = DateUtil.stringToDate("2016-12-22 11:00:00", DateUtil.yyyy_MM_dd_HH_mm_ss);
        long i = (System.currentTimeMillis()-date.getTime())/60000;
        System.out.println(i>10);


        Map<String, String> treeMap = new TreeMap<>();
        treeMap.put("常用", "changyong");
        treeMap.put("A", "AAAA");
        treeMap.put("B", "b");
        treeMap.put("C", "ccc");
        System.out.println(treeMap);

    }

    @Test
    public  void testJavaType(){
        JavaType javaType = JsonUtil.getMapType(Map.class, String.class, String.class);


        List<String> list = Lists.newArrayList("asda", "Asda");
        Map<String,List<String>> map = Maps.newHashMap();
        map.put("a", list);

        String jsonStr = JsonUtil.objectToStr(map);
        JavaType valueType = JsonUtil.getCollectionType(List.class, String.class);
        JavaType javaType1 = JsonUtil.getMapType(HashMap.class, JsonUtil.getType(String.class), valueType);
        Map<String,List<String>> map2 = JsonUtil.strToObject(jsonStr,javaType1);
        if(javaType1 instanceof MapLikeType){
        }
        System.out.println("111");
    }

    @Test
    public void testJsonToBean(){
        String json = " [{\"dicId\":1,\"dicValue\":\"单笔\"},{\"dicId\":2,\"dicValue\":\"月累计\"}]";
        JavaType classType = JsonUtil.getType(InsuranceDicBO.class);
        JavaType listType = JsonUtil.getCollectionType(List.class, classType);

        JavaType listType1 = JsonUtil.getCollectionType(List.class, InsuranceDicBO.class);
        List<InsuranceDicBO> insuranceDicBOs1 = JsonUtil.strToObject(json, listType1);
        List<InsuranceDicBO> insuranceDicBOs = JsonUtil.strToObject(json,listType);
        System.out.println("111");


    }


    @Test
    public void testRegx(){
        String str = "0000010";
        String pattern = "0([0-1]*)0([0-1]{1})|0([0-1]*)([1]+)([0-1]*)1([0-1]{1})|0([0-1]*)11";

        Pattern r = Pattern.compile(pattern);


//        Matcher m = r.matcher(str);
        System.out.println(r.matcher("00000010").matches());
        System.out.println(r.matcher("00000011").matches());
        System.out.println(r.matcher("00001010").matches());
    }
}
