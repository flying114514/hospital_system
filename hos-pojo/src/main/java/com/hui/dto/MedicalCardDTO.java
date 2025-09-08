package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 患者查询医保卡时传递信息
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalCardDTO {
    private Integer id;
    private String name;
    private Integer balance=0;

}
