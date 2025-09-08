package com.hui.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 检查医保卡返回对象
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalCardVO {
    private String name;
    private Integer patientId;
    private Double balance;
    private String detail;

}
