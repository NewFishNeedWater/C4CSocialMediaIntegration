package com.sap.integration.web.sunshine;


import com.sap.integration.service.CamCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;


/**
 *@author Joeyy
 *
 */
@Api(value="WeChat Service", tags = "{WeChat Service Controller}")
@RestController
public class C4CControllerForCamCard {

    private static Logger logger = LoggerFactory.getLogger(C4CControllerForCamCard.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private CamCardService camCardServiceBean;

    @ApiOperation(value="camcard scan",notes="camcard scan",httpMethod = "POST")
    @RequestMapping(value="/camcard/sacn" , method = RequestMethod.POST)
    public JSONObject publishSalesQuote(@RequestParam(value="file",required=false) MultipartFile file){


        //String filePath= "C:\\Users\\i345149\\Desktop\\temp\\"+file.getOriginalFilename();

        String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/"
                + file.getOriginalFilename();

        logger.warn(filePath);
        JSONObject reponse = camCardServiceBean.scanCard(file,filePath);




        return reponse;

    }

    @ApiOperation(value="camcard scan",notes="camcard scan",httpMethod = "POST")
    @RequestMapping(value="/camcard/sacnNormaFile" , method = RequestMethod.POST)
    public String publishCard(@ApiParam(name="camcard picture" , value = "picture for scaned from device", required = true) File file){




        String filePath= request.getSession().getServletContext().getRealPath("/") + "upload/"
                + "joeytest";

        camCardServiceBean.scanCardForNormalFile(file,filePath);


        return "";

    }


}