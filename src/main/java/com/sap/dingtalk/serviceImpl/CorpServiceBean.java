package com.sap.dingtalk.serviceImpl;


import com.sap.dingtalk.model.CorpInfo;
import com.sap.dingtalk.model.CorpInfoRepository;
import com.sap.dingtalk.service.CorpService;
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

        return corpInfoRepository.getSingleCorpInfo().get(0);
    }


}
