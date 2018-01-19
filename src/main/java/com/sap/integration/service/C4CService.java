package com.sap.integration.service;

import com.sap.integration.model.SalesQuoteFile;

public interface C4CService {


    void saveSalesQuoteInfo(SalesQuoteFile file);

    SalesQuoteFile getSalesQuoteByNodeId(String nodeId);


}
