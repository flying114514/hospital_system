package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllTimeDTO {
    private String name;
    private Integer page;
    private Integer pageSize;
    private LocalDateTime time;
    private Integer patientId;
}
