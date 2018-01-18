package com.sap.integration.web.wechat;




import com.sap.integration.constants.Constants;
import com.sap.integration.model.CorpInfo;
import com.sap.integration.model.LogInfo;
import com.sap.integration.model.SalesQuoteFile;
import com.sap.integration.service.C4CService;
import com.sap.integration.service.CorpService;
import com.sap.integration.service.LogInfoService;
import com.sap.integration.service.WeChatService;
import com.sap.integration.utils.FileUtils;
import com.sap.integration.vo.requestVo.C4CSalesQuoteInfo;
import com.sap.integration.vo.responseVo.CommonVo;
import com.sap.integration.vo.wechatVo.Articles;
import com.sap.integration.vo.wechatVo.MessageVo;
import com.sap.integration.vo.wechatVo.News;
import com.sap.integration.vo.wechatVo.WeChatValidationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 *@author Joeyy
 *
 */
@Api(value="WeChat Service", tags = "{WeChat Service Controller}")
@RestController
public class C4CControllerForWeChat {

    private static Logger logger = LoggerFactory.getLogger(C4CControllerForWeChat.class);


    @Autowired
    CorpService corpService;

    @Autowired
    WeChatService weChatService;

    @Autowired
    LogInfoService logInfoService;

    @Autowired
    C4CService c4CService;


    @RequestMapping(value = "/wechat",method = RequestMethod.GET)
    public String validateWeChatToken(String signature,String timestamp,String nonce,String echostr){

        WeChatValidationVo vo = new WeChatValidationVo();

        vo.setEchostr(echostr);
        vo.setSignature(signature);
        vo.setTimestamp(timestamp);
        vo.setNonce(nonce);

        logger.warn("WeChat Validation--Request from external:"+ JSONObject.fromObject(vo).toString());

        LogInfo log = new LogInfo();

        log.setApiName("WeChat Validation");

        log.setApiType("IN");

        log.setStartTime(new Date());

        log.setRequest(JSONObject.fromObject(vo).toString());



        String crypto = weChatService.validateWeChatToken(vo);



        logger.warn("WeChat Validation--Response to external:"+crypto);
        log.setEndTime(new Date());

        log.setResponse(crypto);

        logInfoService.saveLog(log);

        return crypto;

    }

    @ApiOperation(value="Sales Quote Info",notes="Publish Sales Quote to wechat",httpMethod = "POST")
    @RequestMapping(value="/salesQuote/publish" , method = RequestMethod.POST)
    public CommonVo publishSalesQuote(@RequestBody @ApiParam(name="Sales Quote Info" , value = "Received Sales Quote Info from C4C", required = true) C4CSalesQuoteInfo vo){

        logger.warn("Publish Sales Quote--Request from external:"+ JSONObject.fromObject(vo).toString());

        LogInfo log = new LogInfo();

        log.setApiName("Publish Sales Quote");

        log.setApiType("IN");

        log.setStartTime(new Date());

        log.setRequest(JSONObject.fromObject(vo).toString());



        CommonVo response = new CommonVo();

        CorpInfo corpInfo = corpService.getSingleCorpInfo();

        if(corpInfo.getCropId()==null||corpInfo.getCorpSecret()==null){

            logger.error("corp Id is empty or corp secret is empty");
            response.setErrorDesc("corp Id is empty or corp secret is empty");

        }

        if(null==corpInfo.getAccessToken() || corpInfo.getAccessToken().isEmpty()){

            logger.warn("Token Is empty , trying to get it from We Chat server");


            corpInfo = weChatService.getToken(corpInfo);


        }
        SalesQuoteFile file = new SalesQuoteFile();


        String filePath = FileUtils.base64StringToPDF(vo.getFileContent(),vo.getNodeId());

        file.setFileContent(filePath);
        file.setFileName(vo.getFileName());
        file.setNodeId(vo.getNodeId());
        file.setOpenId(vo.getOpenId());


        c4CService.saveSalesQuoteInfo(file);


        MessageVo message = new MessageVo();

        News news = new News();
        Articles articles = new Articles();

        List<Articles> articlesList = new ArrayList<>();

        message.setTouser(vo.getOpenId());
        message.setNews(news);
        message.setMsgtype("news");

        articles.setTitle(vo.getFileName());
        articles.setUrl(Constants.APP_URL+"/salesQuotes/"+vo.getNodeId()+".pdf");
        articlesList.add(articles);
        news.setArticles(articlesList);


        Map<String,Object> msgMap= weChatService.sendCustomMessage(message,corpInfo);

        String errMsg = msgMap.get("errmsg").toString();
        Integer errCode = Integer.parseInt(msgMap.get("errcode").toString());

        if((!"ok".equals(errMsg) )|| (!errCode.equals(0))){

            response.setErrorCode(errCode.toString());
            response.setErrorDesc(errMsg);

            logger.warn("Publish Sales Quote--Response to external:"+ JSONObject.fromObject(response).toString());
            log.setEndTime(new Date());

            log.setResponse(JSONObject.fromObject(response).toString());

            log.setErrorCode(response.getErrorCode());
            log.setErrorMsg(response.getErrorDesc());

            logInfoService.saveLog(log);

            return response;

        }

        if(errCode.equals(40001)){
            corpInfo=weChatService.getToken(corpInfo);
            msgMap= weChatService.sendCustomMessage(message,corpInfo);
            errMsg = msgMap.get("errmsg").toString();
            errCode = Integer.parseInt(msgMap.get("errcode").toString());


            if((!"ok".equals(errMsg) )|| (!errCode.equals(0))){

                response.setErrorCode(errCode.toString());
                response.setErrorDesc(errMsg);

                logger.warn("Publish Sales Quote--Response to external:"+ JSONObject.fromObject(response).toString());
                log.setEndTime(new Date());

                log.setResponse(JSONObject.fromObject(response).toString());

                log.setErrorCode(response.getErrorCode());
                log.setErrorMsg(response.getErrorDesc());

                logInfoService.saveLog(log);

                return response;

            }

        }

        response.setErrorCode("0");
        response.setErrorDesc("ok");

        logger.warn("Publish Sales Quote--Response to external:"+ JSONObject.fromObject(response).toString());
        log.setEndTime(new Date());

        log.setResponse(JSONObject.fromObject(response).toString());

        log.setErrorCode(response.getErrorCode());
        log.setErrorMsg(response.getErrorDesc());

        logInfoService.saveLog(log);

        return response;

    }









    /**
     * @function to refresh the token
     * @return new corp info contains new token
     */
    private CorpInfo refreshToken(CorpInfo info) throws Exception {


        if(info==null){
            throw new Exception("corp info is empty");

        }
        if(info.getCropId()==null||info.getCropId().isEmpty()){
            throw new Exception("corp id is empty");
        }
        if(info.getCorpSecret()==null || info.getCorpSecret().isEmpty()){
            throw new Exception("corp secret is empty");
        }

        CorpInfo corpInfo = weChatService.getToken(info);

        return corpInfo;

    }

}