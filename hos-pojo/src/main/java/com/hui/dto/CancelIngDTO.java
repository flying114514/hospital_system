package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 将状态设置为取消中对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelIngDTO {
    private Integer status;
    private Integer patientId;
    private Integer number;
}
