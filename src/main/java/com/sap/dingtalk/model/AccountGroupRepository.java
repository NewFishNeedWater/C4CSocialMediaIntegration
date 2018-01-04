package com.sap.dingtalk.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountGroupRepository extends JpaRepository<AccountGroup,String> {

    AccountGroup findByAccountUUId(String UUId);


}
