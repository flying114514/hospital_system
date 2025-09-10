package com.hui.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 医生设置详细信息所需对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailInfo {
    private Integer id;
    private String departmentLocation;
    private String name;
    private String department;
    private String profile;
    private String level;
    private String workExperience;
    private Integer doctorId;
}
