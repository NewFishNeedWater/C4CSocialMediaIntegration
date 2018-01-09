package com.sap.dingtalk.vo.requestVo;

public class AccountUpdateInfoRequestVo {


    private String businessObject;

    private String businessObjectId;

    private String event;

    private String odataServiceEndpoint;

    public String getBusinessObject() {
        return businessObject;
    }

    public void setBusinessObject(String businessObject) {
        this.businessObject = businessObject;
    }

    public String getBusinessObjectId() {
        return businessObjectId;
    }

    public void setBusinessObjectId(String businessObjectId) {
        this.businessObjectId = businessObjectId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getOdataServiceEndpoint() {
        return odataServiceEndpoint;
    }

    public void setOdataServiceEndpoint(String odataServiceEndpoint) {
        this.odataServiceEndpoint = odataServiceEndpoint;
    }
}
