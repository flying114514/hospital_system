package com.hui.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 获取id
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallName {
    private String patientName;
    private Integer id;
}
