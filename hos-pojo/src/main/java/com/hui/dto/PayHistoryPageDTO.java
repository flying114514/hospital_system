package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询患者历史充值记录时传递的对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayHistoryPageDTO {
    private Integer patientId;
    private Integer page;
    private Integer pageSize;
    private String beginTime;
    private String endTime;
}
