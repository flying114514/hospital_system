package com.hui.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RegistrationDetailVO {
    /**
     * 挂号ID
     */
    private Long id;
    
    /**
     * 医生姓名
     */
    private String doctorName;
    
    /**
     * 科室名称
     */
    private String department;
    
    /**
     * 挂号费用
     */
    private BigDecimal price;
    
    /**
     * 就诊日期
     */
    private String visitDate;
    
    /**
     * 就诊时间段
     */
    private String timeSlot;
    
    /**
     * 挂号状态
     */
    private String status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
}