package com.sap.integration.serviceImpl;




import com.sap.integration.constants.Constants;
import com.sap.integration.model.CorpInfo;
import com.sap.integration.repository.CorpInfoRepository;
import com.sap.integration.repository.SalesQuoteFileRepository;
import com.sap.integration.service.WeChatService;
import com.sap.integration.vo.wechatVo.MessageVo;
import com.sap.integration.vo.wechatVo.WeChatValidationVo;
import com.sap.integration.utils.EncryptUtils;
import com.sap.integration.utils.HttpRequestUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WeChatServiceBean implements WeChatService {

    private static Logger logger = LoggerFactory.getLogger(WeChatServiceBean.class);


    @Autowired
    CorpInfoRepository corpInfoRepository;

    @Autowired
    SalesQuoteFileRepository salesQuoteFileRepository;

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

        String requestUrl = Constants.WECHAT_URL +"/token?grant_type=client_credential&appid="+corpInfo.getCropId()+"&secret="+corpInfo.getCorpSecret();

        JSONObject object = HttpRequestUtils.httpGet(requestUrl,Constants.WECHAT_API_GET_TOKEN);

        corpInfo.setAccessToken(object.get("access_token")==null?null:object.get("access_token").toString());

        corpInfo.setErrMsg(object.get("errmsg")==null?null:object.get("errmsg").toString());
        corpInfo.setErrorCode(object.get("errcode")==null?null:object.get("errcode").toString());

        corpInfoRepository.save(corpInfo);

        return corpInfo;

    }


    @Override
    public String validateWeChatToken(WeChatValidationVo info){


        List<String> tempList = new ArrayList<String>();

        tempList.add(info.getTimestamp());
        tempList.add(Constants.WECHAT_TOKEN);
        tempList.add(info.getNonce());

        Collections.sort(tempList);

        String stringToValidate =String.join("",tempList);


        if(EncryptUtils.encode(stringToValidate).equals(info.getSignature())){
            return info.getEchostr();
        }else{
            return "Bad Token";

        }


    }


    @Override
    public Map<String,Object> sendCustomMessage(MessageVo vo, CorpInfo info){

        StringBuffer errMsg=new StringBuffer();


        Map<String,Object> msgMap = new HashMap<>();
        if(null==info){
            logger.error("corpInfo is empty");
            errMsg.append("corpInfo is empty");

        }else{
            if(info.getCropId()==null){
                logger.error("corpId is empty");
                errMsg.append("corpId is empty");


            }
            if(info.getCorpSecret()==null){
                logger.error("corp secret is empty");
                errMsg.append("corpId is empty");
            }
        }


        if(!errMsg.toString().isEmpty()){

            logger.warn(errMsg.toString());
            msgMap.put("errmsg",errMsg.toString());

            return msgMap;

        }



        String requestUrlForSendMessage =Constants.WECHAT_URL+"/message/custom/send?access_token="+info.getAccessToken();

        JSONObject jsonObject = JSONObject.fromObject(vo);

        JSONObject response = HttpRequestUtils.httpPost(requestUrlForSendMessage, jsonObject,Constants.WECHAT_API_SEND_MESSAGE);


        msgMap.put("errmsg",response.get("errmsg")==null?null:response.get("errmsg").toString());
        msgMap.put("errcode",response.get("errcode")==null?null:Integer.parseInt(response.get("errcode").toString()));


        return msgMap;


    }


}
