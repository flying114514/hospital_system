package com.hui.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 定时任务类需要的对象
 * */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OverTimeVO {
    private Integer id;//挂号单id
    private Integer status;
}
