package com.hui.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 医生登录返回对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocLoginVO {
    private String name;
    private String idCard;
    private String password;
    private Integer id;
}
