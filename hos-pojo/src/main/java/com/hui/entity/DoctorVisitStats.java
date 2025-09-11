package com.hui.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorVisitStats {
    private String doctorName;
    private String date= LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private Integer visitCount;

}
