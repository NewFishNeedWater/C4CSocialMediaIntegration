package com.sap.dingtalk.web;

import com.sap.dingtalk.constants.Constants;
import com.sap.dingtalk.model.AccountGroup;
import com.sap.dingtalk.model.CorpInfo;
import com.sap.dingtalk.model.Group;
import com.sap.dingtalk.model.LogInfo;
import com.sap.dingtalk.vo.requestVo.*;
import com.sap.dingtalk.vo.responseVo.CommonVo;
import com.sap.dingtalk.service.AccountGroupService;
import com.sap.dingtalk.service.CorpService;
import com.sap.dingtalk.service.DingTalkService;
import com.sap.dingtalk.service.LogInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 *@author Joeyy
 *
 */
@Api(value="Ding Talk Service", tags = "{Ding Talk Controller}")
@RestController
public class C4CController {

    private static Logger logger = LoggerFactory.getLogger(C4CController.class);

    @Autowired
    DingTalkService dingTalkService;

    @Autowired
    CorpService corpService;

    @Autowired
    AccountGroupService accountGroupService;

    @Autowired
    LogInfoService logInfoService;



    @ApiOperation(value="CreateChatGroup",notes="Create Chat Group Based On User List",httpMethod = "POST")
    @RequestMapping("/chat/create")
    public CommonVo createGroup(@RequestBody @ApiParam(name="CreateGroupRequestVo" , value = "create group request Vo", required = true) CreateGroupRequestVo vo){

        logger.warn("CreateChatGroup--Request from external:"+ JSONObject.fromObject(vo).toString());

        LogInfo log = new LogInfo();

        log.setApiName("CreateChatGroup");

        log.setApiType("IN");

        log.setStartTime(new Date());

        log.setRequest(JSONObject.fromObject(vo).toString());



        CommonVo response = new CommonVo();

        CorpInfo corpInfo = corpService.getSingleCorpInfo();

        if(corpInfo.getCropId()==null||corpInfo.getCorpSecret()==null){

            logger.error("corp Id is empty or corp secret is empty");
            response.setErrMsg("corp Id is empty or corp secret is empty");

        }

        if(null==corpInfo.getAccessToken() || corpInfo.getAccessToken().isEmpty()){

            logger.warn("Token Is empty , trying to get it from Ding Talk server");

            corpInfo = dingTalkService.getToken(corpInfo);

        }

        AccountGroup accountGroupInDB=accountGroupService.getAccountGroupByAccountId(vo.getAccountId());

        if(accountGroupInDB!=null){
            logger.warn("There is an account group existing for Account Id: "+vo.getAccountId());
            response.setErrMsg("There is an account group existing for Account Id :"+vo.getAccountId());
            response.setErrCode("40099");
            //response.setCorpInfo(new CorpInfo());
            //response.setGroup(new Group());
            logger.warn("CreateChatGroup--Response to external:"+ JSONObject.fromObject(response).toString());

            log.setEndTime(new Date());

            log.setResponse(JSONObject.fromObject(response).toString());

            log.setErrorCode(response.getErrCode());
            log.setErrorMsg(response.getErrMsg());

            logInfoService.saveLog(log);

            return response;
        }

        vo.getAccountId();
        Group group = new Group();

        group.setName(vo.getName());
        group.setOwner(vo.getOwner());
        group.setUseridlist(vo.getUseridlist());


        group = dingTalkService.createGroup(group,corpInfo);


        //if the token is expired, we need to refresh the token and re-try to create group
        if(group.getErrCode()==40014){
            logger.warn("Token Is empty , trying to get it from Ding Talk server");
            try{
                corpInfo = refreshToken(corpInfo);
            }catch(Exception e){
                logger.warn(e.getMessage(),e);
                response.setErrMsg(e.getMessage());
                response.setErrCode("40095");
                logger.warn("CreateChatGroup--Response to external:"+ JSONObject.fromObject(response).toString());

                log.setEndTime(new Date());

                log.setResponse(JSONObject.fromObject(response).toString());

                log.setErrorCode(response.getErrCode());
                log.setErrorMsg(response.getErrMsg());
                logInfoService.saveLog(log);
                return response;

            }
            group = dingTalkService.createGroup(group,corpInfo);
        }

        if(0==group.getErrCode() && group.getErrMsg().equals("ok")){

            AccountGroup accountGroup = new AccountGroup();
            accountGroup.setAccountId(vo.getAccountId());
            accountGroup.setAccountUUId(vo.getAccountUUId());
            accountGroup.setConversationId(group.getOpenConversationId());
            accountGroup.setGroupId(group.getChatId());

            accountGroupService.save(accountGroup);

            SendTextMessageVo textVo=new SendTextMessageVo();

            textVo.setChatid(accountGroup.getGroupId());
            textVo.setMsgtype(Constants.DING_TALK_MESSAGE_TYPE_TEXT);

            TextMessage message = new TextMessage();
            message.setContent(Constants.DING_TALK_MESSAGE_WELCOME+"\n"+"Account Id:"+accountGroup.getAccountId()+"\n"+"Account Type:"+vo.getAccountType());

            textVo.setText(message);
            dingTalkService.sendTextMessage(corpInfo.getAccessToken(),textVo);
        }

        //response.setCorpInfo(corpInfo);
        //response.setGroup(group);
        response.setErrCode("0");
        response.setErrMsg("ok");

        logger.warn("CreateChatGroup--Response to external:"+ JSONObject.fromObject(response).toString());
        log.setEndTime(new Date());

        log.setResponse(JSONObject.fromObject(response).toString());

        log.setErrorCode(response.getErrCode());
        log.setErrorMsg(response.getErrMsg());

        logInfoService.saveLog(log);

        return response;

    }

    @ApiOperation(value="AccountUpdateNotification",notes="Notification for Account Update",httpMethod = "POST")
    @RequestMapping("/notification/accountUpdate")
    public CommonVo createGroup(@RequestBody @ApiParam(name="AccountUpdateInfoRequestVo" , value = "Account Update Notification Request Vo", required = true) AccountUpdateInfoRequestVo vo){

        logger.warn("AccountUpdateNotification -- Request from external:"+ JSONObject.fromObject(vo).toString());

        LogInfo log = new LogInfo();

        log.setApiName("AccountUpdateNotification");

        log.setApiType("IN");

        log.setStartTime(new Date());

        log.setRequest(JSONObject.fromObject(vo).toString());



        CommonVo responseVo = new CommonVo();

        String BOId= vo.getBusinessObjectId();

        StringBuffer errMsg  = new StringBuffer();
        if(BOId==null|| BOId.isEmpty()){

            logger.warn("BOId is empty");

            errMsg.append("BOId is empty");

            responseVo.setErrMsg(errMsg.toString());
            logger.warn("AccountUpdateNotification--Response to external:"+ JSONObject.fromObject(responseVo).toString());


            return responseVo;

        }


        CorpInfo corpInfo = corpService.getSingleCorpInfo();

        if(corpInfo.getCropId()==null||corpInfo.getCorpSecret()==null){

            logger.error("corp Id is empty or corp secret is empty");
            responseVo.setErrMsg("corp Id is empty or corp secret is empty");
            responseVo.setErrCode("40097");
            log.setEndTime(new Date());

            log.setResponse(JSONObject.fromObject(responseVo).toString());

            log.setErrorCode(responseVo.getErrCode());
            log.setErrorMsg(responseVo.getErrMsg());
            logInfoService.saveLog(log);
            return responseVo;
        }

        if(null==corpInfo.getAccessToken() || corpInfo.getAccessToken().isEmpty()){

            logger.warn("Token Is empty , trying to get it from Ding Talk server");

            corpInfo = dingTalkService.getToken(corpInfo);

        }


        //get Account Group mapping based on UUid
        AccountGroup accountGroup = accountGroupService.getAccountGroupByUUId(BOId);

        if(null==accountGroup){

            logger.error("cannot find account group by UUid"+BOId );
            responseVo.setErrMsg("cannot find account group by UUid"+BOId );
            responseVo.setErrCode("40098");
            logger.warn("AccountUpdateNotification--Response to external:"+ JSONObject.fromObject(responseVo).toString());

            log.setEndTime(new Date());

            log.setResponse(JSONObject.fromObject(responseVo).toString());

            log.setErrorCode(responseVo.getErrCode());
            log.setErrorMsg(responseVo.getErrMsg());
            logInfoService.saveLog(log);
            return responseVo;

        }

        //send text to group chat
        SendTextMessageVo textVo=new SendTextMessageVo();

        textVo.setChatid(accountGroup.getGroupId());
        textVo.setMsgtype(Constants.DING_TALK_MESSAGE_TYPE_TEXT);

        TextMessage message = new TextMessage();

        message.setContent("Account has been changed"+"\n"+"Account Id:"+accountGroup.getAccountId()+"\n");

        textVo.setText(message);
        Map<String,Object> responseMap = dingTalkService.sendTextMessage(corpInfo.getAccessToken(),textVo);


        //if the token is expired, we need to refresh the token and re-try to send message
        if(Integer.parseInt(responseMap.get("errcode").toString())==40014){
            logger.warn("Token Is empty , trying to get it from Ding Talk server");

            try{
                corpInfo = refreshToken(corpInfo);
            }catch(Exception e){
                logger.warn(e.getMessage(),e);
                responseVo.setErrMsg(e.getMessage());
                responseVo.setErrCode("40095");
                logger.warn("AccountUpdateNotification--Response to external:"+ JSONObject.fromObject(responseVo).toString());

                log.setEndTime(new Date());

                log.setResponse(JSONObject.fromObject(responseVo).toString());

                log.setErrorCode(responseVo.getErrCode());
                log.setErrorMsg(responseVo.getErrMsg());
                logInfoService.saveLog(log);
                return responseVo;

            }

            responseMap = dingTalkService.sendTextMessage(corpInfo.getAccessToken(),textVo);
        }

        responseVo.setErrMsg(responseMap.get("errmsg").toString());

        responseVo.setErrCode(responseMap.get("errcode").toString());

        logger.warn("AccountUpdateNotification--Response to external:"+ JSONObject.fromObject(responseVo).toString());

        log.setEndTime(new Date());

        log.setResponse(JSONObject.fromObject(responseVo).toString());

        log.setErrorCode(responseVo.getErrCode());
        log.setErrorMsg(responseVo.getErrMsg());

        logInfoService.saveLog(log);
        return responseVo;

    }



    @ApiOperation(value="LeadUpdateNotification",notes="Notification for Lead Update",httpMethod = "POST")
    @RequestMapping("/notification/leadUpdate")
    public CommonVo createGroup(@RequestBody @ApiParam(name="LeadUpdateInfoRequestVo" , value = "Lead Update Notification Request Vo", required = true) LeadUpdateInfoRequestVo vo){


        CommonVo responseVo = new CommonVo();
        logger.warn("LeadUpdateNotification -- Request from external:"+ JSONObject.fromObject(vo).toString());

        LogInfo log = new LogInfo();
        log.setApiName("LeadUpdateNotification");
        log.setApiType("IN");
        log.setStartTime(new Date());
        log.setRequest(JSONObject.fromObject(vo).toString());









        responseVo.setErrMsg("ok");
        responseVo.setErrCode("0");
        log.setEndTime(new Date());
        log.setResponse(JSONObject.fromObject(responseVo).toString());
        log.setErrorCode(responseVo.getErrCode());
        log.setErrorMsg(responseVo.getErrMsg());



        return responseVo;

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

        CorpInfo corpInfo = dingTalkService.getToken(info);

        return corpInfo;

    }

}