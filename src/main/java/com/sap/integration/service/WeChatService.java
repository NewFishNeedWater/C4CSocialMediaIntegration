package com.sap.integration.service;


import com.sap.integration.model.CorpInfo;
import com.sap.integration.vo.wechatVo.MessageVo;
import com.sap.integration.vo.wechatVo.WeChatValidationVo;

import java.util.Map;


public interface WeChatService {


    /**
     * @funciont to get token from We Chat server
     * @param corpInfo
     * @return the corp information which is contain the token
     */
    CorpInfo getToken(CorpInfo corpInfo);

    /**
     * @function to validation wechat token which is from we chat server
     * @param info
     * @return crypto
     */
    String validateWeChatToken(WeChatValidationVo info);


    /**
     * @function to send message to user
     * @param vo
     * @return error code & erro msg
     */
    Map<String,Object> sendCustomMessage(MessageVo vo, CorpInfo info);


}
