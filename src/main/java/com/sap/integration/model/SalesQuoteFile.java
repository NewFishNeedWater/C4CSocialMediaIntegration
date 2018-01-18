package com.sap.integration.model;
import javax.persistence.*;

@Entity
@Table(name = "salesquote_file")
public class SalesQuoteFile {


    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "node_id")
    private String nodeId;

    @Column(name= "file_name")
    private String fileName;

    @Column(name = "file_content")
    private String fileContent;

    @Column(name="open_id")
    private String openId;


    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
