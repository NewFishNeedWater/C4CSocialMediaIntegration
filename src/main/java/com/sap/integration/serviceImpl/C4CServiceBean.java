package com.sap.integration.serviceImpl;

import com.sap.integration.model.SalesQuoteFile;
import com.sap.integration.repository.SalesQuoteFileRepository;
import com.sap.integration.service.C4CService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class C4CServiceBean implements C4CService{


    @Autowired
    SalesQuoteFileRepository salesQuoteFileRepository;



    public void saveSalesQuoteInfo(SalesQuoteFile file){
        salesQuoteFileRepository.save(file);
    }

    public SalesQuoteFile getSalesQuoteByNodeId(String nodeId){

        return salesQuoteFileRepository.findByNodeId(nodeId);

    }


}
