package com.kanq.demo;

import com.alibaba.fastjson.JSONArray;
import com.kanq.demo.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RedisUtil redisUtil;
    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplicationTests.class);
    @Test
    void contextLoads() {
         String url = "http://127.0.0.1:8089/http/api/putDevice/3";
        // 处理请求消息头参数
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String str = "{\"484021\": {\"L1_LF_1\": 10.2}} ";
        // 处理携带body参数
        HttpEntity<String> httpEntity = new HttpEntity(str, headers);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(url, HttpMethod.POST,httpEntity, String.class);
        // 获取响应体
        LOGGER.info("op=DemoApplicationTests.contextLoads,HTTP 响应body： {}", responseEntity.getBody());
        try {
            int i = 0;
            int j = 1 / i;
        } catch (Exception e) {
            LOGGER.error("【SpringBootDemoLogbackApplication】启动异常：{}", e.getMessage());
        }

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        long currentTimeMillis = Instant.now().toEpochMilli();
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(currentTimeMillis/1000, 0, ZoneOffset.ofHours(8));
        LOGGER.info(localDateTime.format(pattern));

    }

    @Test
    public void strTest(){
        List<Map<String,Object>> listObject =new ArrayList<>();
        Map<String,Object> map = new HashMap<>(16);
        map.put("JCSJ010","1");
        map.put("JCSJ020","2");
        map.put("JCSJ030","3");
        map.put("JCSJ040","4");
        map.put("JCSJ050","5");
        map.put("JCSJ060","6");
        listObject.add(map);
        String cacheObject =  redisUtil.getCacheObject("listMap");
        redisUtil.deleteObject("listMap");
        redisUtil.leftPushList("listObject",listObject);
        redisUtil.rightPopList("listObject");
        JSONArray jsonArray = JSONArray.parseArray(cacheObject);
        LOGGER.info(jsonArray.toJSONString());
    }

}
