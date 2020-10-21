package com.kanq.demo.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * @Description: 将世界标准时或时间戳的字符串转成对应的格式
 * @Date: 2020-08-21 16:57
 * @Author: yyc
 */
public class FormatUtils {
    /**
     *
     * @param strDate   世界标准时或时间戳
     * @param pattern   需要转的时间格式，默认格式为yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String covertStringDate(String strDate, Object... pattern){
        //定义要转换的时间格式
        String strPattern = "yyyy-MM-dd HH:mm:ss";
        if (pattern != null && pattern.length !=0) {
            for (int i = 0; i < pattern.length; i++) {
                strPattern = String.valueOf(pattern[i]);
                break;
            }
        }
        //将世界标准时或时间戳转成字符串,ZoneId设置东部时区
        DateTimeFormatter pattern1 = DateTimeFormatter.ofPattern(strPattern).withZone(ZoneId.of("Etc/GMT"));
        TemporalAccessor dateTime;
        if (StringUtils.isEmpty(strDate)) {
            dateTime = LocalDateTime.now();
        }else {
            //如果为纯数字说明为时间戳，否则就将其转换为世界标准时间（"2020-09-24T15:57:39.572Z"）
            if (StringUtils.isNumeric(strDate)) {
                dateTime = Instant.ofEpochMilli(Long.valueOf(strDate));
            } else {
                String strTemp = StringUtils.endsWith(strDate,"Z") ? strDate : StringUtils.join(strDate, "Z");
                dateTime = Instant.parse(strTemp);
            }
        }
        return pattern1.format(dateTime);
    }

}
