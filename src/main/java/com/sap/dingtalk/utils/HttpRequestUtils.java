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
import org.hibernate.service.spi.InjectService;
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

    private static Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);    //日志记录


    @PostConstruct
    public void init() {
        httpRequestUtils = this;
        httpRequestUtils.logInfoService = this.logInfoService;
    }

    /**
     * httpPost
     * @param url  路径
     * @param jsonParam 参数
     * @return
     */
    public static JSONObject httpPost(String url,JSONObject jsonParam){
        return httpPost(url, jsonParam, false);
    }

    /**
     * post请求
     * @param url         url地址
     * @param jsonParam     参数
     * @param noNeedResponse    不需要返回结果
     * @return
     */
    public static JSONObject httpPost(String url,JSONObject jsonParam, boolean noNeedResponse){

        //post请求返回结果
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

        try {

            httpClient = new SSLClient();
            if (null != jsonParam) {
                //解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(method);
            HttpEntity entity=result.getEntity();
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == 200) {

                log.setEndTime(new Date());
                String str = "";
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(entity);
                    if (noNeedResponse) {
                        return null;
                    }
                    /**把json字符串转换成json对象**/
                    jsonResult = JSONObject.fromObject(str);
                    logger.warn("POST:Response:"+jsonResult.toString());
                    log.setResponse(str);
                } catch (Exception e) {
                    log.setResponse(e.getMessage());
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        } catch (IOException e) {
            log.setResponse(e.getMessage());
            logger.error("post请求提交失败:" + url, e);
        } catch(Exception e){
            log.setResponse(e.getMessage());
            logger.error("post请求提交失败:" + url, e);
        }

        httpRequestUtils.logInfoService.saveLog(log);

        return jsonResult;
    }


    /**
     * 发送get请求
     * @param url    路径
     * @return
     */
    public static JSONObject httpGet(String url){

        LogInfo log = new LogInfo();
        //get请求返回结果
        log.setRequest(url);
        log.setStartTime(new Date());
        JSONObject jsonResult = null;
        try {
            HttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpGet request = new HttpGet(url);

            HttpResponse response = client.execute(request);

            logger.warn("GET:Request Url:"+url);

            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                log.setEndTime(new Date());
                String strResult = EntityUtils.toString(response.getEntity());
                /**把json字符串转换成json对象**/
                jsonResult = JSONObject.fromObject(strResult);
                log.setResponse(strResult);
                logger.warn("GET:Response:"+jsonResult.toString());
                url = URLDecoder.decode(url, "UTF-8");
            } else {
                log.setResponse(response.toString());
                logger.error("get请求提交失败:" + url);
            }
        } catch (IOException e) {
            log.setResponse(e.getMessage());
            logger.error("get请求提交失败:" + url, e);
        }

        httpRequestUtils.logInfoService.saveLog(log);
        return jsonResult;
    }
}