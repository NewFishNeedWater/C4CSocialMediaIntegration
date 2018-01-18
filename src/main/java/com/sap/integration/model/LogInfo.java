package com.sap.integration.model;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "log_info")
public class LogInfo {

    @Id
    @GeneratedValue
    private Integer id;


    @Column(name= "api_name")
    private String apiName;

    @Column(name = "api_type")
    private String apiType;
    private String request;
    private String response;
    @Column(name="error_code")
    private String errorCode;
    @Column(name="error_msg")
    private String errorMsg;
    @Column(name="start_datetime")
    private Date startTime;
    @Column(name="end_datetime")
    private Date endTime;

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiType() {
        return apiType;
    }

    public void setApiType(String apiType) {
        this.apiType = apiType;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
