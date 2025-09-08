package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 减少英航账户所需对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MinusMoneyDTO {
    private String paymentMethod;
    private Double money;
    private Integer patientId;
}
