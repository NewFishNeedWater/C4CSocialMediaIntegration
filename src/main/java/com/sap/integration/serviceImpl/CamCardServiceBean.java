package com.sap.integration.serviceImpl;


import com.sap.integration.service.CamCardService;
import com.sap.integration.utils.HttpRequestUtils;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class CamCardServiceBean implements CamCardService {


    public JSONObject scanCard(MultipartFile file,String filePath){

        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                // 转存文件
                file.transferTo(new File(filePath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        File newFile = new File(filePath);



        String url ="http://bcr2.intsig.net/BCRService/BCR_VCF2?PIN=4C-34-88-86-7E-82&user=3181064544@qq.com&pass=TWSD3AXSK44FEF3T&json=1&lang=15";

        JSONObject responst = HttpRequestUtils.postFile(url,newFile);

        return responst;
    }


    public JSONObject scanCardForNormalFile(File file,String filePath){



        String url ="http://bcr2.intsig.net/BCRService/BCR_VCF2?PIN=4C-34-88-86-7E-82&user=3181064544@qq.com&pass=TWSD3AXSK44FEF3T&json=1&lang=15";

        JSONObject responst = HttpRequestUtils.postFile(url,file);

        return responst;
    }



}
