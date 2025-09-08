package com.hui.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 患者取消挂号返回结果
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelOrderVO {
    private String Detail;
    private Integer patientId;
}
