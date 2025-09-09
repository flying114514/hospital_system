package com.hui.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 返回登录成功对象
 * */
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class DocLoginResult {
    private Integer id;
    private String Detail;
    private String token;
}
