package com.hui.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 挂号数据
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("orders")
public class RegistrationVO {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String patientName;
    private String doctorName;
    private String createTime;
    private String appointmentTime;
    private Double price;
    private String paymentMethod;

}
