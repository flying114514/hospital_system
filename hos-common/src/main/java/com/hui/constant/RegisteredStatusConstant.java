package com.hui.constant;

/**
 *患者挂号状态枚举类
 * */
public class RegisteredStatusConstant {

    //未挂号
    public static final Integer UN_REGISTERED = 0;

    //待叫号
    public static final Integer WAIT_FOR_CALL = 1;

    //就诊中
    public static final Integer DIAGNOSIS = 2;

    //失约
    public static final Integer FAIL = 3;

    //未支付
    public static final Integer UN_PAID = 4;

    //住院中
    public static final Integer IN_HOSPITAL = 5;

    //已超时
    public static final Integer OVER_TIME = 6;

    //取消中
    public static final Integer CANCELING = 7;

    //叫号中
    public static final Integer CALLING = 8;

    //已完成
    public static final Integer COMPLETED = 9;



}
