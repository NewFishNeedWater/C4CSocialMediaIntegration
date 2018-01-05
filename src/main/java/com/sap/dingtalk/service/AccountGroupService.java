package com.sap.dingtalk.service;

import com.sap.dingtalk.model.AccountGroup;

public interface AccountGroupService {


    /**
     * @function save the Account Group mapping
     */
    void save(AccountGroup accountGroup);


    /**
     * @function to get Account group mapping by account UUId
     */

    AccountGroup getAccountGroupByUUId(String UUId);


    /**
     * @function to get Account Group by Account Id
     *
     */
    AccountGroup getAccountGroupByAccountId(String id);

}
