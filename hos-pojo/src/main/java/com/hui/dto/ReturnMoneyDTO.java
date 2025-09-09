package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 *患者退款所需对象
 * */
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class ReturnMoneyDTO {
    private String paymentMethod;
    private Double money;
    private Integer patientId;
}
