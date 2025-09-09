package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 将医生的号还回去
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnDocNumDTO {
    private Integer number;
}
