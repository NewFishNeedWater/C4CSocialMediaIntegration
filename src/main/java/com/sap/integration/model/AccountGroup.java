package com.sap.integration.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account_group")
public class AccountGroup {

    @Column(name="account_id")
    private String accountId;

    @Id
    @Column(name="account_uuid")
    private String accountUUId;

    @Column(name="group_id")
    private String groupId;

    @Column(name="conversation_id")
    private String conversationId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountUUId() {
        return accountUUId;
    }

    public void setAccountUUId(String accountUUId) {
        this.accountUUId = accountUUId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
