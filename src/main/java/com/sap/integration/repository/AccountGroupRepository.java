package com.sap.integration.repository;

import com.sap.integration.model.AccountGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountGroupRepository extends JpaRepository<AccountGroup,String> {

    AccountGroup findByAccountUUId(String UUId);

    AccountGroup findByAccountId(String id);


}
