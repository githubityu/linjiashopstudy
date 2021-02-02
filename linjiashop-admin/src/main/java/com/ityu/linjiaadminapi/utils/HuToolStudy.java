package com.ityu.linjiaadminapi.utils;

import cn.hutool.core.clone.CloneSupport;
import cn.hutool.core.util.ObjectUtil;

public class HuToolStudy {

    private static class Dog extends CloneSupport<Dog>{
        private String name = "zhangsan";
    }


    public static void main(String[] args) {
        System.out.println("=====张三李四");
    }



}
