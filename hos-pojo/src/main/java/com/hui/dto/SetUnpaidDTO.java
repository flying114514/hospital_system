package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设置未支付状态所需对象
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetUnpaidDTO {
    private Integer registerId;
    private String time;
}
