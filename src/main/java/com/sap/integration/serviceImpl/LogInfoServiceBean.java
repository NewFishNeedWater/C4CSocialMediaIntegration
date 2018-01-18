package com.sap.integration.serviceImpl;

import com.sap.integration.repository.LogInfoRepository;
import com.sap.integration.model.LogInfo;
import com.sap.integration.service.LogInfoService;
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
