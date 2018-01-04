package com.sap.dingtalk.service;

import com.sap.dingtalk.model.CorpInfo;
import com.sap.dingtalk.model.Group;


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

}
