package com.hui.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 医生详细信息
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName
public class DoctorDetails {
    private String name;
    private String department;
    private String departmentLocation;
    private String workExperience;
    private Double price;
    private String level;
}
