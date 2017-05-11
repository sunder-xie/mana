package com.tqmall.mana.component.util.mana;

import com.google.common.collect.Lists;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zxg on 16/12/1.
 * 16:06
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class ManaUtil {
    // 生成随机手机号
    private static List<String> telFirstList = Lists.newArrayList("134","135","136","137","138","139",
            "150","151","152","157","158","159","130","131","132","155","156","133","153","188");


    public static String getRandomTel() {
        Random random = new Random();
        int index=random.nextInt(telFirstList.size());
        StringBuilder stringBuilder= new StringBuilder();
        stringBuilder.append(telFirstList.get(index));
        stringBuilder.append("00012121");
        return stringBuilder.toString();
//        return "13400012121";
    }

    /**
     * 验证手机号
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile){
        if(StringUtils.isEmpty(mobile)){
            return false;
        }
        Pattern pattern = Pattern.compile("^(13|14|15|17|18)\\d{9}$");
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    /**
     * 验证车牌号
     * @param vehicle
     * @return
     */
    public static boolean isVehicle(String vehicle){
        if(StringUtils.isEmpty(vehicle)){
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}");
        Matcher matcher = pattern.matcher(vehicle.toUpperCase());
        return matcher.matches();
    }

    //判断ajax请求
    public static boolean isAjaxRequest(HttpServletRequest request){
        String requestType = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestType);
    }


    public static String bytesToStr(byte[] bytes){
        if(bytes==null){
            return "";
        }
        int len = bytes.length;
        if(len==0){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<len; i++){
            sb.append(",").append(bytes[i]);
        }
        sb.deleteCharAt(0);
        return sb.toString();
    }
    public static byte[] strToBytes(String str){
        if(StringUtils.isEmpty(str)){
            return new byte[0];
        }
        String[] strings = str.split(",");
        int len = strings.length;
        byte[] bytes = new byte[len];
        for(int i=0; i<len; i++){
            bytes[i] = Byte.parseByte(strings[i]);
        }
        return bytes;
    }

    //解密
    public static String aesDecrypt(String text, String key){
        if(text!=null && key!=null){
            AesCipherService aesCipherService = new AesCipherService();
            byte[] keys = ManaUtil.strToBytes(key);
            return new String(aesCipherService.decrypt(Hex.decode(text), keys).getBytes());
        }
        return null;
    }
    //加密
    public static String aesEncrypt(String text, String key){
        if(text!=null && key!=null){
            AesCipherService aesCipherService = new AesCipherService();
            byte[] keys = ManaUtil.strToBytes(key);
            return aesCipherService.encrypt(text.getBytes(), keys).toHex();
        }
        return null;
    }


    //保留两位小数、四舍五入
    public static BigDecimal getDecimalHalfUp(BigDecimal decimal){
        if(decimal==null){
            return null;
        }
        return decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * jeval 公式表达式替换方法
     * 变量只能是字母
     * @param exp 例如：(a - c) * d + c
     * @return jeval可识别的数学公式表达式
     */
    public static String formatExpForJEval(String exp){
        String reg = "([a-zA-Z]+)";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(exp);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()){
            matcher.appendReplacement(sb, "#{" + matcher.group(1) + "}");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
