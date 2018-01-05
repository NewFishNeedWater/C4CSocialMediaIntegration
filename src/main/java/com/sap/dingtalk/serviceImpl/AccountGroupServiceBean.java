package com.sap.dingtalk.serviceImpl;


import com.sap.dingtalk.model.AccountGroup;
import com.sap.dingtalk.repository.AccountGroupRepository;
import com.sap.dingtalk.service.AccountGroupService;
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
