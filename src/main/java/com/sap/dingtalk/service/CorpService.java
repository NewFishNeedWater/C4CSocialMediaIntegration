package com.sap.dingtalk.service;

import com.sap.dingtalk.model.CorpInfo;

import java.util.List;

public interface CorpService {

    /**
     * @function to get corp information
     */
    CorpInfo findById(String corpId);

    /**
     * @function to get all CorpInfo with access token
     */

    List<CorpInfo> getAllCorpInfoWithToken();

    /**
     * @function to getSingleCorpInfo
     */

    CorpInfo getSingleCorpInfo();


}
