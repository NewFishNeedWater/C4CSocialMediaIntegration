package com.sap.integration.service;

import com.sap.integration.model.CorpInfo;
import com.sap.integration.model.Group;
import com.sap.integration.vo.requestVo.SendTextMessageVo;

import java.util.Map;


public interface DingTalkService {


    /**
     * @funciont to get token from ding talk server
     * @param corpInfo
     * @return the corp information which is contain the token
     */
    CorpInfo getToken(CorpInfo corpInfo);


    /**
     * @function to create Group based on userId List
     * @param group
     * @return the group created by ding talk
     */
    Group createGroup(Group group,CorpInfo info);


    /**
     * @function to send message to chat
     * @return the status message
     */
    Map<String,Object> sendTextMessage(String accessToken, SendTextMessageVo vo);

}
