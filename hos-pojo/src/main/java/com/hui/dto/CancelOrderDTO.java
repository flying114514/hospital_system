package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 患者取消挂号索要传递的对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelOrderDTO {
    private String Detail;
    private Integer patientId;
    private Integer Status;
}
