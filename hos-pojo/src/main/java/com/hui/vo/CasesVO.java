package com.hui.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 医生填写病例对象
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CasesVO {
    private String detail;
}
