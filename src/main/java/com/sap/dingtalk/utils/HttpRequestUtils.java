package com.sap.dingtalk.utils;


import com.sap.dingtalk.model.LogInfo;
import com.sap.dingtalk.service.LogInfoService;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;


@Component
public class HttpRequestUtils {


    private static HttpRequestUtils httpRequestUtils;


    @Autowired
    private LogInfoService logInfoService;


    public void setLogInfoService(LogInfoService logInfoService) {
        this.logInfoService = logInfoService;
    }

    private static Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);


    @PostConstruct
    public void init() {
        httpRequestUtils = this;
        httpRequestUtils.logInfoService = this.logInfoService;
    }

    /**
     * httpPost
     * @param url
     * @param jsonParam
     * @return
     */
    public static JSONObject httpPost(String url,JSONObject jsonParam,String apiName){
        return httpPost(url, jsonParam, false,apiName);
    }

    /**
     * post请求
     * @param url
     * @param jsonParam
     * @param noNeedResponse
     * @return
     */
    public static JSONObject httpPost(String url,JSONObject jsonParam, boolean noNeedResponse,String apiName){


        HttpClient httpClient;
        JSONObject jsonResult = null;
        HttpPost method = new HttpPost(url);

        if(jsonParam!=null){
            logger.warn("POST:Request:"+jsonParam.toString());
        }else{
            logger.warn("POST:Request is empty");
        }

        LogInfo log = new LogInfo();
        log.setStartTime(new Date());
        log.setRequest(jsonParam.toString());
        log.setApiName(apiName);
        log.setApiType("OUT");

        try {

            httpClient = new SSLClient();
            if (null != jsonParam) {
                //set charset to utf-8
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(method);
            HttpEntity entity=result.getEntity();
            url = URLDecoder.decode(url, "UTF-8");


            /**request successfully and get response**/
            if (result.getStatusLine().getStatusCode() == 200) {

                log.setEndTime(new Date());
                String str = "";
                try {
                    /**read json string from response server**/
                    str = EntityUtils.toString(entity,"UTF-8").trim();
                    if (noNeedResponse) {
                        return null;
                    }
                    /**convert the json string to json entity**/
                    jsonResult = JSONObject.fromObject(str);
                    logger.warn("POST:Response:"+jsonResult.toString());
                    log.setResponse(str);
                } catch (Exception e) {
                    log.setResponse(e.getMessage());
                    logger.error("Post failed:" + url, e);
                }
            }
        } catch (IOException e) {
            log.setResponse(e.getMessage());
            logger.error("Post failed:" + url, e);
        } catch(Exception e){
            log.setResponse(e.getMessage());
            logger.error("Post failed:" + url, e);
        }

        httpRequestUtils.logInfoService.saveLog(log);

        return jsonResult;
    }


    /**
     *
     * @param url
     * @return
     */
    public static JSONObject httpGet(String url,String apiName){

        LogInfo log = new LogInfo();

        log.setRequest(url);
        log.setStartTime(new Date());
        log.setApiName(apiName);
        log.setApiType("OUT");
        JSONObject jsonResult = null;
        try {
            HttpClient client = new DefaultHttpClient();

            HttpGet request = new HttpGet(url);

            HttpResponse response = client.execute(request);

            logger.warn("GET:Request Url:"+url);

            /**request successfully and get response**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**read json string from response server**/
                log.setEndTime(new Date());
                String strResult = EntityUtils.toString(response.getEntity(),"UTF-8").trim();
                /**convert the json string to json entity**/
                jsonResult = JSONObject.fromObject(strResult);
                log.setResponse(strResult);
                logger.warn("GET:Response:"+jsonResult.toString());
                url = URLDecoder.decode(url, "UTF-8");
            } else {
                log.setResponse(response.toString());
                logger.error("get failed:" + url);
            }
        } catch (IOException e) {
            log.setResponse(e.getMessage());
            logger.error("get failed:" + url, e);
        }

        httpRequestUtils.logInfoService.saveLog(log);
        return jsonResult;
    }
}