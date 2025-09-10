package com.hui.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 医生叫号所需对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallNumberVO {
    private String detail;

}
