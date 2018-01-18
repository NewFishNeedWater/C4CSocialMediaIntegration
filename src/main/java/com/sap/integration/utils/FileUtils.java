package com.sap.integration.utils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);
    static BASE64Encoder encoder = new sun.misc.BASE64Encoder();
    static BASE64Decoder decoder = new sun.misc.BASE64Decoder();


    public static String getPDFBinary(File file) {
        FileInputStream fin =null;
        BufferedInputStream bin =null;
        ByteArrayOutputStream baos = null;
        BufferedOutputStream bout =null;
        try {

            fin = new FileInputStream(file);

            bin = new BufferedInputStream(fin);

            baos = new ByteArrayOutputStream();

            bout = new BufferedOutputStream(baos);
            byte[] buffer = new byte[1024];
            int len = bin.read(buffer);
            while(len != -1){
                bout.write(buffer, 0, len);
                len = bin.read(buffer);
            }
            bout.flush();
            byte[] bytes = baos.toByteArray();

            return encoder.encodeBuffer(bytes).trim();

        } catch (FileNotFoundException e) {
            logger.warn(e.getMessage(),e);
        } catch (IOException e) {
            logger.warn(e.getMessage(),e);
        }finally{
        try {
            fin.close();
            bin.close();

            bout.close();
        } catch (IOException e) {
            logger.warn(e.getMessage(),e);
        }
        }
        return null;
    }


    public static String base64StringToPDF(String base64sString,String fileName){
        BufferedInputStream bin = null;
        FileOutputStream fout = null;
        BufferedOutputStream bout = null;
        String filePath="";
        try {

        byte[] bytes = decoder.decodeBuffer(base64sString);

            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

            bin = new BufferedInputStream(bais);

            filePath = "/opt/tomcat/webapps/salesQuotes/"+fileName+".pdf";

            File file = new File(filePath);

            fout  = new FileOutputStream(file);

            bout = new BufferedOutputStream(fout);

            byte[] buffers = new byte[1024];
            int len = bin.read(buffers);
            while(len != -1){
                bout.write(buffers, 0, len);
                len = bin.read(buffers);
            }

            bout.flush();

        } catch (IOException e) {
            logger.warn(e.getMessage(),e);
        }finally{
            try {
            bin.close();
                fout.close();
                bout.close();
            } catch (IOException e) {
                logger.warn(e.getMessage(),e);
            }
        }
        return filePath;
    }


}
