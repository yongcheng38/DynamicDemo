package com.kanq.demo;

import java.util.*;

/**
 * @Description:
 * @Date: 2020-11-17 10:13
 * @Author: yyc
 */
public class TestList {
    public static void main(String[] args) {
        List<String> numbersList = new ArrayList<>();
        numbersList.add("小伙");
        numbersList.add("陌森");
        numbersList.add("萌新");
        numbersList.add("小伙");
        numbersList.add("小伙子");
        numbersList.add("陌森1");
        numbersList.add("陌森");
        List<String>  list = removeDuplicate1(numbersList);

        //List<String> list = numbersList.stream().distinct().collect(Collectors.toList());
    }
    //list集合去重1
    public static List<String> removeDuplicate(List<String> list) {

        LinkedHashSet<String> hashSet = new LinkedHashSet<>(list);

        ArrayList<String> listWithoutDuplicates = new ArrayList<>(hashSet);

        return listWithoutDuplicates;
    }
    private static List<String> removeDuplicate1(List<String> list) {
        Set<String> set = new HashSet<>(list.size());
        List<String> result = new ArrayList<>(list.size());
        for (String str : list) {
            if (set.add(str)) {
                result.add(str);
            }
        }
        System.out.println(result);
        list.clear();
        list.addAll(result);
        return list;

    }
    private static List<String>  removeDuplicate2(List<String> list) {
        List<String> result = new ArrayList<>(list.size());
        for (String str : list) {
            if (!result.contains(str)) {
                result.add(str);
            }
        }
        list.clear();
        list.addAll(result);
        return list;
    }
}
