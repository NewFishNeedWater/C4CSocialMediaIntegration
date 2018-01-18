package com.sap.integration.serviceImpl;


import com.sap.integration.model.AccountGroup;
import com.sap.integration.repository.AccountGroupRepository;
import com.sap.integration.service.AccountGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountGroupServiceBean implements AccountGroupService{

    @Autowired
    AccountGroupRepository accountGroupRepository;


    @Override
    public void save(AccountGroup accountGroup){

        accountGroupRepository.save(accountGroup);

    }

    @Override
    public AccountGroup getAccountGroupByUUId(String UUId){

        return accountGroupRepository.findByAccountUUId(UUId);

    }


    @Override
    public AccountGroup getAccountGroupByAccountId(String id){

        return accountGroupRepository.findByAccountId(id);

    }

}
