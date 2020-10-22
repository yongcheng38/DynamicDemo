package com.kanq.demo;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @Description:
 * @Date: 2020-10-20 10:17
 * @Author: yyc
 */
public class TestEasyExcel {
    private List<Apple> data() {
        List<Apple> list = new ArrayList<Apple>();
        for (int i = 0; i < 10; i++) {
            Apple data = new Apple();
            data.setId(i);
            data.setName("名字"+i);
            data.setMoney(new BigDecimal("0.5"+i));
            data.setBirthday(LocalDateTime.now());
            list.add(data);
        }
        return list;
    }
    @Test
    public void writeExcel(){
        // 写法2
       String fileName =  "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(fileName, Apple.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
            excelWriter.write(data(), writeSheet);
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
//        response.setContentType("application/vnd.ms-excel; charset=utf-8");
//        response.setCharacterEncoding("utf-8");
//        String fileName = "三好学生表";
//        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8") + ".xlsx");
//        EasyExcel.write(response.getOutputStream(), User.class).sheet("test").doWrite(data());

    }
    @Test
    public void dinList(){
        List<String> list  =   new  ArrayList<String>();
        list.add("aaa");
        list.add("bbb");
        list.add("aaa");
        list.add("aba");
        list.add("bbb");

        List newList = new ArrayList(new HashSet(list));

        System.out.println( "去重后的集合： " + newList);

    }
}
