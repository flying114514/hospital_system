package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 用来封装传给setNumber的对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeDTO {
    private String number;
    private String Status;
    private String currentPatientId;
    private String predictTime;
}
