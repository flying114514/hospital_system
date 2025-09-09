package com.hui.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 基本获取医生信息对象
 * */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DocBasic {
    private String name;
    private Integer id;
    private String idCard;
}
