package com.sap.dingtalk.serviceImpl;

import com.sap.dingtalk.constants.Constants;
import com.sap.dingtalk.model.CorpInfo;
import com.sap.dingtalk.Repository.CorpInfoRepository;
import com.sap.dingtalk.model.Group;
import com.sap.dingtalk.requestVo.SendTextMessageVo;
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

            logger.warn(errMsg.toString());
            corpInfo.setErrMsg(errMsg.toString());
            return corpInfo;

        }

        String requestUrl = Constants.DING_TALK_WEBSERVICE_URL +"/gettoken?corpid="+corpInfo.getCropId()+"&corpsecret="+corpInfo.getCorpSecret();
        JSONObject object = HttpRequestUtils.httpGet(requestUrl,Constants.DING_TALK_API_GET_TOKEN);
        corpInfo.setAccessToken(object.get("access_token")==null?null:object.get("access_token").toString());
        corpInfo.setErrMsg(object.get("errmsg")==null?null:object.get("errmsg").toString());
        corpInfo.setErrMsg(object.get("errcode")==null?null:object.get("errcode").toString());

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

        JSONObject response = HttpRequestUtils.httpPost(requestUrlForCreateGroup, jsonObject,Constants.DING_TALK_API_CREATE_CHAT);

        group.setChatId(response.get("chatid")==null?null:response.get("chatid").toString());

        group.setOpenConversationId(response.get("openConversationId")==null?null:response.get("openConversationId").toString());

        group.setConversationTag(response.get("conversationTag")==null?null:response.get("conversationTag").toString());

        group.setErrMsg(response.get("errmsg")==null?null:response.get("errmsg").toString());

        group.setErrCode(response.get("errcode")==null?null:Integer.parseInt(response.get("errcode").toString()));


        return group;

    }


    public String sendTextMessage(String accessToken, SendTextMessageVo vo){

        StringBuffer errMsg=new StringBuffer();


        if(vo.getChatid()==null || vo.getChatid().isEmpty()){
            logger.error("chatId is empty");
            errMsg.append("chatId is empty");

        }
        if(vo.getText()==null){
            logger.error("message is empty");
            errMsg.append("message is empty");
        }else{
            if(vo.getText().getContent()==null||vo.getText().getContent().isEmpty()){
                logger.error("message is empty");
                errMsg.append("message is empty");
            }
        }


        if(!errMsg.toString().isEmpty()){

            return errMsg.toString();

        }


        String requestUrlForSendMessage =Constants.DING_TALK_WEBSERVICE_URL+"/chat/send?access_token="+accessToken;

        JSONObject jsonObject = JSONObject.fromObject(vo);

        JSONObject response = HttpRequestUtils.httpPost(requestUrlForSendMessage, jsonObject,Constants.DING_TALK_API_SEND_GROUP_MESSAGE);


        String statusMessage = response.get("errmsg")==null?null:response.get("errmsg").toString();


        return statusMessage;

    }

}
