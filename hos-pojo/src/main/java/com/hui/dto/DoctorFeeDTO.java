package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DoctorFeeDTO {
    private String name;
    private Integer doctorId;
    private Integer number;//累计就诊数
    private String level;
}
