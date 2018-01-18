package com.sap.integration.serviceImpl;


import com.sap.integration.model.CorpInfo;
import com.sap.integration.repository.CorpInfoRepository;
import com.sap.integration.service.CorpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CorpServiceBean implements CorpService{

    @Autowired
    CorpInfoRepository corpInfoRepository;


    public CorpInfo findById(String corpId){

        return corpInfoRepository.findByCropId(corpId);
    }


    public List<CorpInfo> getAllCorpInfoWithToken(){

        return corpInfoRepository.findAllByAccessTokenIsNotNull();
    }

    public CorpInfo getSingleCorpInfo(){

        return corpInfoRepository.getDingTalkCorpInfo().get(0);
    }


}
