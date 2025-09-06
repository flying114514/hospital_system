package com.hui.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装临时向表存入的数据
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemplateInfo {
    private String patientName;
    private String doctorName;
    private String idCard;
    private String Card;
    private String department;

}
