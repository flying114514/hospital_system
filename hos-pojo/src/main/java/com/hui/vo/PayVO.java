package com.hui.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 患者缴费返回对象
 * */
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class PayVO {
    private String detail;
}
