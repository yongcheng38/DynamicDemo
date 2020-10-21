package com.kanq.demo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * @Description:
 * @Date: 2020-09-07 16:58
 * @Author: yyc
 */
public class ListIn8 {

    private static  List<Apple> appleList;

    static {
        appleList = new ArrayList<>();//存放apple对象集合
        Apple apple1 =  new Apple(1,"苹果1",new BigDecimal("3.25"),10);
        Apple apple12 = new Apple(1,"苹果2",new BigDecimal("1.35"),20);
        Apple apple13 = new Apple(1,"苹果2",new BigDecimal("1.35"),20);
        Apple apple2 =  new Apple(2,"香蕉",new BigDecimal("2.89"),30);
        Apple apple3 =  new Apple(3,"荔枝",new BigDecimal("9.99"),40);

        appleList.add(apple1);
        appleList.add(apple12);
        appleList.add(apple13);
        appleList.add(apple2);
        appleList.add(apple3);
    }

    /**
     * list集合分组
     */
    @Test
    public void groupList(){
        Map<Integer, List<Apple>> collect = appleList.stream().collect(Collectors.groupingBy(Apple::getId));
        //多字段组合分组
        Map<String, List<Apple>> collect1 = appleList.stream().collect(Collectors.groupingBy(e -> fetchGroupKey(e)));
        //List转Map id为key，apple对象为value。可以用 (k1,k2)->k1 来设置，如果有重复的key,则保留key1,舍弃key2
        Map<Integer, Apple> appleMap = appleList.stream().collect(Collectors.toMap(Apple::getId, a -> a,(k1,k2)->k1));
        // 根据对象id去重对象属性
        List<Apple> unique = appleList.stream().collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<Apple>(Comparator.comparing(Apple::getId))), ArrayList::new)
        );
        //过滤出符合条件的数据
        List<Apple> filterList = appleList.stream().filter(a -> a.getName().equals("苹果2")).collect(Collectors.toList());

        //去重字符串
        List<String> list = new ArrayList<>();
        list.add("苹果1");
        list.add("苹果1");
        list.add("苹果2");
        list.add("苹果3");
        List uniqueStr = list.stream().distinct().collect(Collectors.toList());
        appleMap.forEach((k,v) -> System.out.println(k+"=="+v.toString()));

        filterList.forEach(System.out::println);
    }
    @Test
    public void sumCount(){
        int sum =0;
//        for (int i = 0; i <  1000; i++) {
//            sum =sum+i;
//        }
        int i = 1;
        while (i<100){
            sum+=i;
            i++;
        }
        System.out.println(sum);
    }
    private static String fetchGroupKey(Apple user){
        return user.getId() +"#"+ user.getName();
    }

}
