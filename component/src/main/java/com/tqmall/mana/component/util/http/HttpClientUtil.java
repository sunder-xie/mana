package com.tqmall.mana.component.util.http;

import com.tqmall.mana.component.bean.HttpClientResult;
import com.tqmall.mana.component.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by huangzhangting on 15/9/5.
 */
@Slf4j
public class HttpClientUtil {

    private static final int CONNECT_TIME_OUT = 60000;//60s
    private static final int READ_TIME_OUT = 30000;//30s

    private static final String defaultCharset = "UTF-8";

    // 重试次数
    private static Integer RETRY_POST_NUM = 0;
    private static Integer RETRY_POST_MAX_NUM = 3;
    // 重试次数
    private static Integer RETRY_GET_NUM = 0;
    private static Integer RETRY_GET_MAX_NUM = 3;

    private static CloseableHttpClient httpclient;

    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.setDefaultMaxPerRoute(10);

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIME_OUT)
                .setSocketTimeout(READ_TIME_OUT).build();

        httpclient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .setConnectionManager(cm)
                .build();
    }

    public static HttpClientResult post(String url) {
        return post(url, null);
    }

    public static HttpClientResult post(String url, List<NameValuePair> nvpList) {
        CloseableHttpResponse response = null;
        try {
            HttpPost request = new HttpPost(url);
            if (!CollectionUtils.isEmpty(nvpList)) {
                request.setEntity(new UrlEncodedFormEntity(nvpList, defaultCharset));
            }
            request.setHeader("Content-type", "application/x-www-form-urlencoded");
            request.setHeader("User-Agent", UserAgent.getRandomUserAgent());

            response = httpclient.execute(request);
            HttpClientResult hcResult = new HttpClientResult();
            try {
                hcResult.setStatus(response.getStatusLine().getStatusCode());

                log.info("http post url:" + url + ", status:" + hcResult.getStatus());

                if (hcResult.getStatus() != HttpStatus.SC_OK) {
                    return hcResult;
                }

                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    hcResult.setData(EntityUtils.toString(resEntity, defaultCharset));
                }

                if(RETRY_POST_NUM > 0) {RETRY_POST_NUM  = 0;}

                return hcResult;

            } catch (Exception e) {
                log.error("http post url:" + url + ".response:" + JsonUtil.objectToStr(response), e);
                return reTryPost(url,nvpList);
            }finally {
                response.close();
            }

        } catch (Exception e) {
            log.error("http post url:" + url + ".nvpList:" + JsonUtil.objectToStr(nvpList), e);
            return reTryPost(url,nvpList);
        }finally {
            if(response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 重试
    private static HttpClientResult reTryPost(String url, List<NameValuePair> nvpList){
        if(RETRY_POST_NUM < RETRY_POST_MAX_NUM){
            RETRY_POST_NUM += 1;
            log.info("re try again. the retry num:{}",RETRY_POST_NUM);
            return post( url, nvpList);
        }
        return null;
    }

    public static HttpClientResult postJson(String url, String jsonString) {
        try {
            HttpPost request = new HttpPost(url);
            if (!StringUtils.isEmpty(jsonString)) {
                request.setEntity(new StringEntity(jsonString));
            }
            request.addHeader("Content-Type", "application/json;charset=UTF-8");
            request.addHeader("accept", "application/json");
            request.addHeader("connection", "Keep-Alive");

            CloseableHttpResponse response = httpclient.execute(request);
            HttpClientResult hcResult = new HttpClientResult();
            try {

                hcResult.setStatus(response.getStatusLine().getStatusCode());

                log.info("http postJson url:" + url + ", params:" + jsonString + ", status:" + hcResult.getStatus());

                if (hcResult.getStatus() != HttpStatus.SC_OK) {
                    return hcResult;
                }

                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    hcResult.setData(EntityUtils.toString(resEntity, defaultCharset));
                }
                return hcResult;

            } finally {
                response.close();
            }

        } catch (Exception e) {
            log.error("http postJson url:" + url + ", params:" + jsonString, e);
        }

        return null;
    }


    public static HttpClientResult get(String url) {
        return get(url, null);
    }

    public static HttpClientResult get(String url, List<NameValuePair> nvpList) {
        try {
            if (!CollectionUtils.isEmpty(nvpList)) {
                String params = (URLEncodedUtils.format(nvpList, defaultCharset));
                url = url + "?" + params;
            }

            HttpGet request = new HttpGet(url);

            CloseableHttpResponse response = httpclient.execute(request);
            HttpClientResult hcResult = new HttpClientResult();
            try {
                hcResult.setStatus(response.getStatusLine().getStatusCode());

                log.info("http get url:" + url + ", status:" + hcResult.getStatus());

                if (hcResult.getStatus() != HttpStatus.SC_OK) {
                    return hcResult;
                }

                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    hcResult.setData(EntityUtils.toString(resEntity, defaultCharset));
                }

                if(RETRY_GET_NUM > 0){RETRY_GET_NUM = 0;}
                return hcResult;

            }catch (Exception e){
                log.error("error:http get url:" + url, e);
                reTryGet(url,nvpList);
            } finally {
                response.close();
            }

        } catch (Exception e) {
            log.error("error:http get url:" + url, e);
            reTryGet(url,nvpList);
        }

        return null;
    }


    // 重试
    private static HttpClientResult reTryGet(String url, List<NameValuePair> nvpList){
        if(RETRY_GET_NUM < RETRY_GET_MAX_NUM){
            RETRY_GET_NUM += 1;
            log.info("retry again. the retry num:{}",RETRY_GET_NUM);
            return get(url, nvpList);
        }
        return null;
    }

    public static HttpClientResult postXml(String url, String xml) {
        try {
            HttpPost request = new HttpPost(url);
            if (!StringUtils.isEmpty(xml)) {
                request.setEntity(new StringEntity(xml));
            }
            request.addHeader("Content-Type", "application/soap+xml;charset=UTF-8");
            request.addHeader("Connection", "keep-alive");

            CloseableHttpResponse response = httpclient.execute(request);
            HttpClientResult hcResult = new HttpClientResult();
            try {

                hcResult.setStatus(response.getStatusLine().getStatusCode());

                log.info("http postXml url:" + url + ", params:" + xml + ", status:" + hcResult.getStatus());

                if (hcResult.getStatus() != HttpStatus.SC_OK) {
                    return hcResult;
                }

                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    hcResult.setData(EntityUtils.toString(resEntity, defaultCharset));
                }
                return hcResult;

            } finally {
                response.close();
            }

        }catch (Exception e) {
            log.error("http postXml url:" + url + ", params:" + xml, e);
        }

        return null;
    }



}
