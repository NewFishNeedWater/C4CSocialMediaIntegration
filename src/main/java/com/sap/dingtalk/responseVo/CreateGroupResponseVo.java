package com.sap.dingtalk.responseVo;

import com.sap.dingtalk.model.CorpInfo;
import com.sap.dingtalk.model.Group;

public class CreateGroupResponseVo {

    private CorpInfo corpInfo;
    private String errorCode;
    private String errorDesc;

    private Group group;


    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public CorpInfo getCorpInfo() {
        return corpInfo;
    }

    public void setCorpInfo(CorpInfo corpInfo) {
        this.corpInfo = corpInfo;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }
}
