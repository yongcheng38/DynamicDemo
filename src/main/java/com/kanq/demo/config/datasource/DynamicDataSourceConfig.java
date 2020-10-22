package com.kanq.demo.config.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 功能描述：多数据源配置
 *
 * @Author: fxl
 * @Date: 2020/7/3 13:43
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource.druid")
public class DynamicDataSourceConfig {

    private List<Map<String, Object>>  sourceList ;

    public List<Map<String, Object>> getSourceList(){
        return  sourceList;
    }
    public void  setSourceList(List<Map<String, Object>> sourceConfig){
        this.sourceList= sourceConfig;
    }
}
