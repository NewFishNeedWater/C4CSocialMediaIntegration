package com.sap.dingtalk.repository;


import com.sap.dingtalk.model.CorpInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CorpInfoRepository extends JpaRepository<CorpInfo,String> {


    CorpInfo findByCropId(String cropId);

    List<CorpInfo> findAllByAccessTokenIsNotNull();

    @Query("from CorpInfo c")
    List<CorpInfo> getSingleCorpInfo();



}
