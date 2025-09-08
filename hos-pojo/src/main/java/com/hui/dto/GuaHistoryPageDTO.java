package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 患者查询历史挂号数据所需对象
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuaHistoryPageDTO {
    private Integer page;
    private Integer pageSize;
    private Integer patientId;
}
