package com.sap.dingtalk.serviceImpl;

import com.sap.dingtalk.constants.Constants;
import com.sap.dingtalk.model.AccountGroup;
import com.sap.dingtalk.model.CorpInfo;
import com.sap.dingtalk.model.CorpInfoRepository;
import com.sap.dingtalk.model.Group;
import com.sap.dingtalk.service.AccountGroupService;
import com.sap.dingtalk.service.DingTalkService;
import com.sap.dingtalk.utils.HttpRequestUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DingTalkServiceBean implements DingTalkService{

    private static Logger logger = LoggerFactory.getLogger(DingTalkServiceBean.class);


    @Autowired
    CorpInfoRepository corpInfoRepository;


    @Override
    public CorpInfo getToken(CorpInfo corpInfo){


        StringBuffer errMsg=new StringBuffer();

        if(null==corpInfo){
            logger.error("corpInfo is empty");
            errMsg.append("corpInfo is empty");

        }else{
            if(corpInfo.getCropId()==null){
                logger.error("corpId is empty");
                errMsg.append("corpId is empty");


            }
            if(corpInfo.getCorpSecret()==null){
                logger.error("corp secret is empty");
                errMsg.append("corpId is empty");
            }
        }


        if(!errMsg.toString().isEmpty()){

            corpInfo.setErrMsg(errMsg.toString());
            return corpInfo;

        }

        String requestUrl = Constants.DING_TALK_WEBSERVICE_URL +"/gettoken?corpid="+corpInfo.getCropId()+"&corpsecret="+corpInfo.getCorpSecret();

        String token = HttpRequestUtils.httpGet(requestUrl).get("access_token")==null?null:HttpRequestUtils.httpGet(requestUrl).get("access_token").toString();
        corpInfo.setAccessToken(token);

        corpInfoRepository.save(corpInfo);

        return corpInfo;

    }


    @Override
    public Group createGroup(Group group,CorpInfo info){
        StringBuffer errMsg=new StringBuffer();

        if(group==null){

            logger.error("group is empty");
            errMsg.append("group is empty");
        }else{

            if(group.getName()==null){
                logger.error("group name is empty");
                errMsg.append("group name is empty");
            }else{

            }
            if(group.getOwner()==null){
                logger.error("group owner is empty");
                errMsg.append("group owner is empty");
            }else{
            }

            if(group.getUseridlist()==null || group.getUseridlist().isEmpty()){
                logger.error("group user list is empty");
                errMsg.append("group user list is empty");
            }

        }

        if(!errMsg.toString().isEmpty()){

            group.setErrMsg(errMsg.toString());
            return group;

        }

        String requestUrlForCreateGroup =Constants.DING_TALK_WEBSERVICE_URL+"/chat/create?access_token="+info.getAccessToken();

        JSONObject jsonObject = JSONObject.fromObject(group);

        JSONObject response = HttpRequestUtils.httpPost(requestUrlForCreateGroup, jsonObject,false);

        group.setChatId(response.get("chatid")==null?null:response.get("chatid").toString());

        group.setOpenConversationId(response.get("openConversationId")==null?null:response.get("openConversationId").toString());

        group.setConversationTag(response.get("conversationTag")==null?null:response.get("conversationTag").toString());

        group.setErrMsg(response.get("errmsg")==null?null:response.get("errmsg").toString());

        group.setErrCode(response.get("errcode")==null?null:Integer.parseInt(response.get("errcode").toString()));


        return group;

    }

}
