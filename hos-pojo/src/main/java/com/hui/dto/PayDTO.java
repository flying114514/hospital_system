package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 患者缴费时需要传递的对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayDTO {
    private String paymentMethod;
    private String detail;
    private Double price;//要交的钱
    private Integer patientId;
    private Integer registerId;
}
