package com.hui.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 医生注册返回结果
 * */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DocRegisterVO {
    private String name;
    private String detail;
    private Integer id;
    private Integer doctorId;
}
