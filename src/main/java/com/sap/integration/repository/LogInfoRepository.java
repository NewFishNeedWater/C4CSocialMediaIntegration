package com.sap.integration.repository;


import com.sap.integration.model.LogInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogInfoRepository extends JpaRepository<LogInfo,Integer> {

}
