package com.hui.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 挂号单实体类
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("orders")
public class Registration {
    @TableId(type = IdType.AUTO)
    private Integer id;//挂号单的id
    private Integer number;//挂号数
    private String patientName;
    private String age;
    private String doctorName;
    private Double price;
    private String department;
    private String departmentLocation;
    private String level;
    private String predictTime;
}
