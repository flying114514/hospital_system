package com.hui.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cases {
    private Integer patientId;
    private String patientName;
    private String doctorName;
    private Integer doctorId;
    private String cases;
    private LocalDateTime createTime;
    private Integer number;

}
