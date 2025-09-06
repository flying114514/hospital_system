package com.hui.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 余浩信息
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeDetails {
    private String number;
    private String name;//余浩名

    private String beginTime;
    private String endTime;

}
