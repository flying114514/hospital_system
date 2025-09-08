package com.hui.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 患者支付成功后,向orders表中补全信息
 * */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderFull {
    private Integer id;
    private LocalDateTime createTime;
    private String paymentMethod;
}
