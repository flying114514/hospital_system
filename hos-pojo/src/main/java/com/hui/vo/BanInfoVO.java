package com.hui.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BanInfoVO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime predictTime;
}
