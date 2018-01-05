package com.sap.dingtalk.serviceImpl;

import com.sap.dingtalk.repository.LogInfoRepository;
import com.sap.dingtalk.model.LogInfo;
import com.sap.dingtalk.service.LogInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LogInfoServiceBean implements LogInfoService {
    private static Logger logger = LoggerFactory.getLogger(LogInfoServiceBean.class);

    @Autowired
    LogInfoRepository logInfoRepository;

    @Override
    public void saveLog(LogInfo log){

        logInfoRepository.save(log);

    }
}
