package com.hui.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * 查询排班信息所需对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeInfoDTO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;
    private Integer doctorId;
    private Integer page;
    private Integer pageSize;
}
