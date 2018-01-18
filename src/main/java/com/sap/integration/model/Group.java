package com.sap.integration.model;

import java.util.List;

public class Group {


    private String name;
    private String owner;
    private List<String> useridlist;

    private String chatId;
    private String openConversationId;
    private String conversationTag;

    private String errMsg;

    private Integer errCode;

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getOpenConversationId() {
        return openConversationId;
    }

    public void setOpenConversationId(String openConversationId) {
        this.openConversationId = openConversationId;
    }

    public String getConversationTag() {
        return conversationTag;
    }

    public void setConversationTag(String conversationTag) {
        this.conversationTag = conversationTag;
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



