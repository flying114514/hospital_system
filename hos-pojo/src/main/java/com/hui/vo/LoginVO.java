package com.hui.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取登录返回信息
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginVO {
    private String id;
    private String name;
    private String password;;

}
