package com.sap.dingtalk.web;

import com.sap.dingtalk.constants.Constants;
import com.sap.dingtalk.model.AccountGroup;
import com.sap.dingtalk.model.CorpInfo;
import com.sap.dingtalk.model.Group;
import com.sap.dingtalk.requestVo.CreateGroupRequestVo;
import com.sap.dingtalk.requestVo.SendTextMessageVo;
import com.sap.dingtalk.requestVo.TextMessage;
import com.sap.dingtalk.responseVo.CreateGroupResponseVo;
import com.sap.dingtalk.service.AccountGroupService;
import com.sap.dingtalk.service.CorpService;
import com.sap.dingtalk.service.DingTalkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



    @ApiOperation(value="CreateChatGroup",notes="CreateChatGroupBasedOnUserList",httpMethod = "POST")
    @RequestMapping("/chat/create")
    public CreateGroupResponseVo createGroup(@RequestBody @ApiParam(name="CreateGroupRequestVo" , value = "CreateGroupRequestVo", required = true) CreateGroupRequestVo vo){

        logger.warn("Request from external:"+ JSONObject.fromObject(vo).toString());

        CreateGroupResponseVo response = new CreateGroupResponseVo();

        CorpInfo corpInfo = corpService.getSingleCorpInfo();

        if(corpInfo.getCropId()==null||corpInfo.getCorpSecret()==null){

            logger.error("corp Id is empty or corp secret is empty");
            response.setErrorDesc("corp Id is empty or corp secret is empty");

        }

        if(null==corpInfo.getAccessToken() || corpInfo.getAccessToken().isEmpty()){

            logger.warn("Token Is empty , trying to get it from Ding Talk server");

            corpInfo = dingTalkService.getToken(corpInfo);

        }
        Group group = new Group();

        group.setName(vo.getName());
        group.setOwner(vo.getOwner());
        group.setUseridlist(vo.getUseridlist());


        group = dingTalkService.createGroup(group,corpInfo);


        //if the token is expired, we need to refresh the token and re-try to create group
        if(group.getErrCode()==40014){
            logger.warn("Token Is empty , trying to get it from Ding Talk server");
            corpInfo = dingTalkService.getToken(corpInfo);
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
            message.setContent(Constants.DING_TALK_MESSAGE_WELCOME+"\n"+"AccountId:"+accountGroup.getAccountId()+"\n"+"AccountUUId:"+accountGroup.getAccountUUId());

            textVo.setText(message);
            dingTalkService.sendTextMessage(corpInfo.getAccessToken(),textVo);
        }

        response.setCorpInfo(corpInfo);
        response.setGroup(group);


        return response;

    }

}