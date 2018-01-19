package com.sap.integration.repository;


import com.sap.integration.model.SalesQuoteFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesQuoteFileRepository extends JpaRepository<SalesQuoteFile,Integer> {


    SalesQuoteFile findByNodeId(String nodeId);
}
