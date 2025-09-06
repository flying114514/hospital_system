package com.hui.annotation;


import com.hui.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//aop:当执行到某些方法时自动执行操作

//自定义注解,用于标识某个方法需要进行功能字段自动填充处理

@Target(ElementType.METHOD)//注解要加的位置
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //指定当前操作数据库类型
    OperationType value();

}
