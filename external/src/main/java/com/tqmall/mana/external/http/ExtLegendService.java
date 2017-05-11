//package com.tqmall.mana.external.http;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.tqmall.mana.beans.BO.LicenseProvinceBO;
//import com.tqmall.mana.component.bean.HttpClientResult;
//import com.tqmall.mana.component.util.JsonUtil;
//import com.tqmall.mana.component.util.http.HttpClientUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.HttpStatus;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by huangzhangting on 16/12/14.
// */
//@Slf4j
//@Service
//public class ExtLegendService {
//    @Value("${legend.host}")
//    private String legendHost;
//
//
//    public List<LicenseProvinceBO> getLicensePreList(Integer cityId){
//        String url;
//        if(cityId==null || cityId<1){
//            url = legendHost + "legend/pub/license/city/get_province";
//        }else{
//            url = legendHost + "legend/pub/license/city/cur_province/"+cityId;
//        }
//
//        HttpClientResult clientResult = HttpClientUtil.get(url);
//        if(clientResult!=null && clientResult.getStatus()== HttpStatus.SC_OK){
//            String dataStr = clientResult.getData();
//            if(!StringUtils.isEmpty(dataStr)){
//                JsonNode jsonNode = JsonUtil.getJsonNode(dataStr);
//                if(jsonNode==null){
//                    return new ArrayList<>();
//                }
//
//                JsonNode data = jsonNode.findValue("data");
//                if(data==null){
//                    return new ArrayList<>();
//                }
//
//                List<LicenseProvinceBO> licenseProvinceBOList =
//                        JsonUtil.jsonNodeToCollection(data, List.class, LicenseProvinceBO.class);
//
//                if(licenseProvinceBOList==null){
//                    return new ArrayList<>();
//                }
//                return licenseProvinceBOList;
//            }
//        }
//
//        return new ArrayList<>();
//    }
//}
