package com.hui.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 *患者查询历史充值记录对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayHistory {
    private Double money;
    private String paymentMethod;
    private String time;
}
