package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 医生注册所需对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocRegisterDTO {
    private String name;
    private String idCard;
    private String password;
    private Integer id;
    private Integer doctorId;
}
