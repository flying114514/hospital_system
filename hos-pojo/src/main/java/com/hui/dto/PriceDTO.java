package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 根据级别设置价格所需对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceDTO {
    private Double price;
    private Integer doctorId;
}
