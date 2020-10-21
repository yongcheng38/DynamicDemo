package com.kanq.demo.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


/**
 * @author yyc
 */
@Controller
@RequestMapping("/files")
public class FileController {
    @Value("${filePath}")
    private String storagePath;
    private static final String PAGE_INDEX = "index";

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @GetMapping("/index")
    public String test(){
        logger.info("狗东西");
        return PAGE_INDEX ;
    }
    @GetMapping("/downloadFile")
    public void download(@NotEmpty @RequestParam("fileName") String serverFileName,
                         HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        String percentEncodedFileName = URLEncoder.encode(serverFileName, StandardCharsets.UTF_8.toString())
                .replaceAll("\\+", "%20");

        StringBuilder contentDispositionValue = new StringBuilder();
        contentDispositionValue.append("attachment; filename=")
                .append(percentEncodedFileName)
                .append(";")
                .append("filename*=")
                .append("utf-8''");

        response.setHeader("Content-disposition", contentDispositionValue.toString());

        try (InputStream inputStream =  FileUtils.openInputStream(new File(storagePath,serverFileName));
             OutputStream outputStream = response.getOutputStream()
        ) {
            IOUtils.copy(inputStream, outputStream);
        }

    }
    /**
     * @description 上传多文件
     * @createTime 2020/8/10 17:02
     **/
    @PostMapping("/uploadFiles")
    public String add(@RequestParam("files") MultipartFile[] files) throws IOException {
        if (files.length <= 0) {
            logger.error("上传的文件为空");
            return "redirect:/files/index";
        }
        //判断文件夹是否存在
        File filePath = new File(storagePath);
        if (!filePath.exists()){
            // 创建该文件夹
            filePath.mkdirs();
        }
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            FileUtils.copyToFile(file.getInputStream(),new File(storagePath,fileName));
//            file.transferTo(new File(storagePath,fileName));
        }
        logger.info("成功完成");
        return "";
    }
    @PostMapping("/uploadFile")
    public String add(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            logger.error("上传的文件为空");
            return PAGE_INDEX;
        }
       /* //判断存储的文件夹是否存在
        File filePath = new File(storagePath,file.getOriginalFilename());
        if (!filePath.getParentFile().exists()){
            // 创建该文件夹
            filePath.getParentFile().mkdirs();
        }
         String fileName = file.getOriginalFilename();
         file.transferTo(new File(storagePath,fileName));
        */
        String string = IOUtils.toString(file.getInputStream(),"UTF-8");
        JSONObject.parseObject(string);
        logger.info(string);
        return "mail";
    }

}
