package com.tqmall.mana.component.util;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhangting on 16/7/30.
 */
public class ImgUtil {
    public static final String HTTP_PROTOCOL = "http://";
    public static final String HTTPS_PROTOCOL = "https://";
    public static final String IMG_DOMAIN = "https://img.tqmall.com/";

    private final static String imageDomain_old = "http://img.tqmall.com/";
    private final static String imageDomain_app = "http://tqmall-vin.b0.upaiyun.com/";
    private final static String imageDomain = "http://tqmall-image.oss-cn-hangzhou.aliyuncs.com/";


    public static String getFullPath(String path) {
        if(StringUtils.isEmpty(path)){
            return path;
        }
        if(path.startsWith(IMG_DOMAIN)){
            return path;
        }
        if(path.startsWith(imageDomain_old)){
            return path.replace(imageDomain_old, IMG_DOMAIN);
        }
        if(path.startsWith(imageDomain_app)){
            return path.replace(imageDomain_app, IMG_DOMAIN);
        }
        if(path.startsWith(imageDomain)){
            return path.replace(imageDomain, IMG_DOMAIN);
        }
        if(path.startsWith(HTTP_PROTOCOL) || path.startsWith(HTTPS_PROTOCOL)){
            return path;
        }

        return IMG_DOMAIN + path;
    }

    public static String httpToHttps(String url){
        if(StringUtils.isEmpty(url)){
            return url;
        }
        if(url.startsWith(HTTP_PROTOCOL)){
            return url.replace(HTTP_PROTOCOL, HTTPS_PROTOCOL);
        }
        return url;
    }

    public static List<String> batchHttpToHttps(List<String> urlList){
        if(CollectionUtils.isEmpty(urlList)){
            return urlList;
        }
        List<String> list = new ArrayList<>();
        for(String url : urlList){
            list.add(httpToHttps(url));
        }
        return list;
    }




    // TODO test
    public static void main(String[] args){
        List<String> list = new ArrayList<>();
        list.add("http://img.tqmall.com/images/car/96197.jpg");
        list.add("http://img.tqmall.com/images/car/96197.jpg");

        System.out.println(list);

        System.out.println(batchHttpToHttps(list));
    }
}
