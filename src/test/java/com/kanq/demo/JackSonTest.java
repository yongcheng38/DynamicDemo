package com.kanq.demo;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kanq.demo.utils.FormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Date: 2020-09-24 14:08
 * @Author: yyc
 */
public class JackSonTest {
    public Apple preData(){
        Apple apple = new Apple();
        apple.setId(1);
        apple.setName("萧茗");
        apple.setNum(12);
        apple.setMoney(new BigDecimal("5000"));
        apple.setHobbies(new String[]{"高尔夫球", "棒球"});
        Map<String, BigDecimal> salary = new HashMap<String, BigDecimal>() {{
            put("2000", new BigDecimal(10000000));
            put("2010", new BigDecimal(62000000));
            put("2020", new BigDecimal(112400000));
        }};
        apple.setBirthday(LocalDateTime.now());
        apple.setSalary(salary);
        apple.setFriends(Arrays.asList("kobe", "curry", "james"));
        return apple;
    }

    @Test
    public void testJack() throws IOException {
        Apple apple = preData();
        ObjectMapper mapper = new ObjectMapper();
        //解决序列化日期类型报错问题
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);  //注意这里
        //解决Date日期序列化的问题
//        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        mapper.registerModule(new JavaTimeModule());
        //将对象转换json字符串
        String s = mapper.writeValueAsString(apple);
        //将对象数据存入文件
        mapper.writeValue(new File("src\\main\\resources\\config\\player.json"), apple);
        //忽略对象中没有定义的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //从本地文件中读取json数据
        Apple apple1 = mapper.readValue(
                new File("src\\main\\resources\\config\\player.json")
                , Apple.class);

        //从本地文件中读取json数据
        JSONObject obj = mapper.readValue(
                new File("src\\main\\resources\\config\\player.json")
                , JSONObject.class);
        //从URL获取JSON响应数据，并反序列化为java 对象
        JSONObject jsonObject = mapper.readValue(new URL("https://jsonplaceholder.typicode.com/posts/1"), JSONObject.class);
        System.out.println(String.valueOf(obj));
    }
    @Test
    public void testLocalDate(){
        System.out.println( FormatUtils.covertStringDate( null)+"");
    }

}
