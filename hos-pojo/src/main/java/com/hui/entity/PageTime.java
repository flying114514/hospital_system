package com.hui.entity;


import com.hui.result.PageResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查余号返回结果
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageTime {
    private String doctorName;
    private String level;
    private String workExperience;
    private String department;
    private String departmentLocation;
    private String remain;

    private PageResult pageResult;
}
