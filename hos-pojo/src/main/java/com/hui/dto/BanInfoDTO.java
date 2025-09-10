package com.hui.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 设置排班信息所需对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BanInfoDTO {
    private String name;
    private Integer doctorId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime predictTime;
    private String level;
    private Integer status;
    private Integer remain;
    private Integer workId;
}
