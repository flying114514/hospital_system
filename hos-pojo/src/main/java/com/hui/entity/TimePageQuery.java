package com.hui.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 挂号选择完科室后展示的医生数据
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimePageQuery {
    private String name;
    private String department;
    private String departmentLocation;
    private Double price;
    private Integer remain;//剩余号数
    private String level;

}
