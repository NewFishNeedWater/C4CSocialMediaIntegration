package com.sap.dingtalk.web;

import com.sap.dingtalk.model.AccountGroup;
import com.sap.dingtalk.model.CorpInfo;
import com.sap.dingtalk.model.Group;
import com.sap.dingtalk.requestVo.CreateGroupRequestVo;
import com.sap.dingtalk.responseVo.CreateGroupResponseVo;
import com.sap.dingtalk.service.AccountGroupService;
import com.sap.dingtalk.service.CorpService;
import com.sap.dingtalk.service.DingTalkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

        CreateGroupResponseVo response = new CreateGroupResponseVo();

        CorpInfo corpInfo = corpService.getSingleCorpInfo();

        if(corpInfo.getCropId()==null||corpInfo.getCorpSecret()==null){

            logger.error("corp Id is empty or corp secret is empty");
            response.setErrorDesc("corp Id is empty or corp secret is empty");

        }

        if(null==corpInfo.getAccessToken() || corpInfo.getAccessToken().isEmpty()){

            corpInfo = dingTalkService.getToken(corpInfo);

        }
        Group group = new Group();

        group.setName(vo.getName());
        group.setOwner(vo.getOwner());
        group.setUseridlist(vo.getUseridlist());


        group = dingTalkService.createGroup(group,corpInfo);


        //if the token is expired, we need to refresh the token and re-try to create group
        if(group.getErrCode()==40014){
            corpInfo = dingTalkService.getToken(corpInfo);
            group = dingTalkService.createGroup(group,corpInfo);
        }

        response.setCorpInfo(corpInfo);
        response.setGroup(group);


        AccountGroup accountGroup = new AccountGroup();


        accountGroup.setAccountId(vo.getAccountId());
        accountGroup.setAccountUUId(vo.getAccountUUId());
        accountGroup.setConversationId(group.getOpenConversationId());
        accountGroup.setGroupId(group.getChatId());

        accountGroupService.save(accountGroup);


        return response;

    }

}