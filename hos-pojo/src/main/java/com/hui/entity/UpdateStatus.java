package com.hui.entity;

import com.hui.constant.RegisteredStatusConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 支付完成后修改状态
 * */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UpdateStatus {
    private Integer registerId;
    private final Integer status= RegisteredStatusConstant.WAIT_FOR_CALL;
}
