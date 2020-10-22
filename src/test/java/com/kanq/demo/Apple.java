package com.kanq.demo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Date: 2020-09-07 16:59
 * @Author: yyc
 */
public class Apple {
    @ExcelProperty("序号")
    private Integer id;
    @ExcelProperty("名称")
    private String name;
    @ExcelProperty("金额")
    private BigDecimal money;
    @ExcelIgnore
    private Integer num;
    @ExcelProperty(value = "时间",converter = LocalDateTimeConverter.class)
    private LocalDateTime birthday;
    @ExcelIgnore
    private String[] hobbies;    //业余爱好,数组
    @ExcelIgnore
    private List<String> friends;   //  朋友
    @ExcelIgnore
    private Map<String, BigDecimal> salary; //年收入

    public Apple(){};
    public Apple(Integer id, String name, BigDecimal money, Integer num) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.num = num;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String[] getHobbies() {
        return hobbies;
    }

    public void setHobbies(String[] hobbies) {
        this.hobbies = hobbies;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public Map<String, BigDecimal> getSalary() {
        return salary;
    }

    public void setSalary(Map<String, BigDecimal> salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", money=" + money +
                ", num=" + num +
                ", hobbies=" + Arrays.toString(hobbies) +
                ", friends=" + friends +
                ", salary=" + salary +
                '}';
    }
}
