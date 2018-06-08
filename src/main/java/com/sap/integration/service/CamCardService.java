package com.sap.integration.service;

import net.sf.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface CamCardService {


    JSONObject scanCard(MultipartFile file, String filePath);

    JSONObject scanCardForNormalFile(File file, String filePath);
}
