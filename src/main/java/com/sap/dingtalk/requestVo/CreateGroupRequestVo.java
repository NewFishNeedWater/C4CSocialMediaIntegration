package com.sap.dingtalk.requestVo;

import java.util.List;

public class CreateGroupRequestVo {

    private String name;
    private String owner;
    private List<String> useridlist;

    private String accountId;
    private String accountType;

    private String accountUUId;

    public String getAccountUUId() {
        return accountUUId;
    }

    public void setAccountUUId(String accountUUId) {
        this.accountUUId = accountUUId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }


    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getUseridlist() {
        return useridlist;
    }

    public void setUseridlist(List<String> useridlist) {
        this.useridlist = useridlist;
    }
}
