package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 插入id所需对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsertIdDTO {
    private Integer doctorId;
    private Integer id;
}
