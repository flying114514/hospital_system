package com.hui.dto;

import lombok.Data;

@Data
public class DoctorPageQueryDTO {

    private Integer page;

    private Integer pageSize;
    //科室名称
    private String name;

}
