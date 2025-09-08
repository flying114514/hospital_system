package com.hui.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 获取在15分钟内未完成挂号的订单
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class EndTimeVO {
    private LocalDateTime time;
    private Integer id;//挂号单id

}
