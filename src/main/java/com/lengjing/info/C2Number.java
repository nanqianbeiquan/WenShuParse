/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lengjing.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tangzhichao
 */
public class C2Number {

    private static Map<String, String> numberMap = createNumberMap();
    //
    private static List<String> units = createUnit();

    private static List<String> unitsdw = dwcreateUnit();
    //
    private static Map<String, Long> unitMap = createUnitMap();

    public static String chinese2Num(String data) {
            long value = 0L;
         String  numberText= data.substring(0,data.indexOf("元"));
         String jf=data.substring(data.indexOf("元")+1);
         for (Map.Entry<String, Long> entry : unitMap.entrySet()) {
                String[] numberArray = numberText.split(entry.getKey());
                if (numberArray.length == 2) {
                    String tempPrefix = convert(numberArray[0]);
                    if (tempPrefix != null && tempPrefix.matches("\\d+")) {
                        value += Long.parseLong(tempPrefix) * entry.getValue();
                    }
                    numberText = numberArray[1];
                    continue;
                }
                if (numberArray.length == 1) {
                    String tempPrefix = numberText;
                    for (int i = units.size() - 1; i >= 0; i--) {
                        if (tempPrefix.endsWith(units.get(i))) {
                            tempPrefix = numberText.replace(units.get(i), "");
                            tempPrefix = convert(numberText);
                        }
                    }
                    try {
                        value += Long.parseLong(tempPrefix);
                        numberText = "";
                    } catch (Exception ex) {
                        //do nothing
                    }
                    break;
                }
            }
            String tempPrefix = convert(numberText);
            if (tempPrefix != null && tempPrefix.matches("\\d+")) {
                value += Long.parseLong(tempPrefix);
            }
            String result="";
            if(jf!=null && !jf.equals("")){
                String jiao="";
                if(jf.indexOf("角")>0) {
                     jiao = jf.substring(0, jf.indexOf("角"));
                    if(jf.indexOf("分")>0) {
                        String feng = jf.substring(jf.indexOf("角") + 1, jf.indexOf("分"));
                        result = numberMap.get(jiao) + numberMap.get(feng);
                    }else{
                        result = numberMap.get(jiao);
                    }
                }else{
                    if(jf.indexOf("分")>0) {
                        String feng = jf.substring(0, jf.indexOf("分"));
                        result = 0 + numberMap.get(feng);
                    }else{
                        result = numberMap.get(jiao);
                    }
                }

            }
            if(jf!=null && !jf.equals("")) {
                return value + "" + "." + result;
            }else{
                return value + "";
            }

    }

    /**
     * 一百以内的转法
     *
     * @param numberText
     * @return
     */
    private static String convert(String numberText) {
        while ((numberText.length() > 1 && numberText.startsWith("〇"))) {
            numberText = numberText.substring(1);
        }
        if(numberText.equals("拾万")){
            return "100000";
        }
        String result = "";
        String end = "";
        int zeroCount = 0;
        //根据单位补0
        for (int i = units.size() - 1; i >= 0; i--) {
            if (numberText.endsWith(units.get(i))) {
                zeroCount = i+1;
                break;
            }
        }
        for (int i = 0; i < zeroCount; i++) {
                end +="0";
        }
        //根据〇来算他的个数
        //  numberText = doCalcZero(numberText);
        //去掉单位字符
         boolean flag=false;
         String data="";
        if(numberText.startsWith("十") || numberText.startsWith("拾")){
            flag=true;
        }
        numberText = numberText.replaceAll("(?!十万|百万|千万|十亿|百亿|千亿)[十百千万亿]", "");
        //分割剩余字符
        String array[] = numberText.trim().split("|");
        int sum=0;
        for (int i=0;i<array.length;i++) {
                if (array[i]!=null && !array[i].equals("") && numberMap.get(array[i])!=null) {
                    result += numberMap.get(array[i]);
                }
        }
        result += end;
        if(flag){
            data="1"+result;
        }else{
            if(numberText.endsWith("拾")){
                result+="0";
            }
            data=result;
        }
        return data;
    }

    private static Map<String, String> createNumberMap() {
        Map<String, String> numberMap = new HashMap<String, String>();
        numberMap.put("零", "0");
        numberMap.put("一", "1");
        numberMap.put("二", "2");
        numberMap.put("三", "3");
        numberMap.put("四", "4");
        numberMap.put("五", "5");
        numberMap.put("六", "6");
        numberMap.put("七", "7");
        numberMap.put("八", "8");
        numberMap.put("九", "9");
        numberMap.put("壹","1");
        numberMap.put("贰","2");
        numberMap.put("叁","3");
        numberMap.put("肆","4");
        numberMap.put("伍","5");
        numberMap.put("陆","6");
        numberMap.put("柒","7");
        numberMap.put("捌","8");
        numberMap.put("玖","9");
        //numberMap.put("十","0");
        numberMap.put("百","00");
        numberMap.put("千","000");
        numberMap.put("万","0000");

        return numberMap;
    }

    private static List<String> createUnit() {
        List<String> list = new ArrayList<String>();
        list.add("十");
        list.add("百");
        list.add("千");
        list.add("万");
        list.add("十万");
        list.add("百万");
        list.add("千万");
        list.add("亿");
        list.add("十亿");
        list.add("百亿");
        list.add("千亿");
        return list;
    }

    private static List<String> dwcreateUnit() {
        List<String> list = new ArrayList<String>();
        list.add("十");
        list.add("百");
        list.add("千");
        list.add("万");
        list.add("亿");
        return list;
    }

    private static Map<String, Long> createUnitMap() {
        Map<String, Long> map = new LinkedHashMap<String, Long>();
        map.put("亿", 100000000L);
        map.put("万", 10000L);
        map.put("千", 1000L);
        return map;
    }

}
