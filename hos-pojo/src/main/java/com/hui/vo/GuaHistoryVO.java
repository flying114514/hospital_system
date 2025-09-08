package com.hui.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 分页查找历史挂号数据
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuaHistoryVO {
    private String doctorName;
    private String department;
    private String departmentLocation;
    private String createTime;
    private Double price;
    private String paymentMethod;
    private String level;
    private Integer status;

}
