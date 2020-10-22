package com.kanq.demo.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;


/**
 * @author yyc
 */
@Component
public class ColumnJson {
/** logger */
private static final Logger LOGGER = LoggerFactory.getLogger(ColumnJson.class);
  private static JSONObject columnObject;
  static {
    try {
      ClassPathResource resource = new ClassPathResource("config/column.json");
      InputStream inputStream = resource.getInputStream();
      //第一种方法
      String columnStr = IOUtils.toString(inputStream, "utf-8");
      columnObject = JSONObject.parseObject(columnStr);
      //第二种方法
     // columnObject = new ObjectMapper().readValue(inputStream, JSONObject.class);

      LOGGER.info("初始化设备类型配置：{}", columnObject);
    } catch (IOException e) {
      LOGGER.error("初始化设备类型配置：{}", e.getMessage());
    }
  }

  public static  JSONArray getColumnValue(String key) {
    return columnObject.getJSONArray(key);
  }

  public static  JSONObject getColumn() {
    return columnObject;
  }
}
