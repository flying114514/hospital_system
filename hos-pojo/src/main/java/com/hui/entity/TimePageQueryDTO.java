package com.hui.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 预约时间分页
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TimePageQueryDTO {

    private String name;

    private Integer page;

    private Integer pageSize;
}
