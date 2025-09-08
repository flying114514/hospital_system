package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayHistoryDTO {
    private String paymentMethod;
    private String time;
    private Double money;
    private Integer patientId;
}
