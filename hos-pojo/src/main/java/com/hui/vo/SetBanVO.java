package com.hui.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 医生设置排班信息返回结果对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SetBanVO {
    private String detail;
}
