package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 医生登录所需对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocLoginDTO {
    private String password;
    private String idCard;
}
