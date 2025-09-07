package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新建银行账户对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankDTO {
    private String name;
    private Double cash;
    private Double wechatPay;
    private String patientId;
}
