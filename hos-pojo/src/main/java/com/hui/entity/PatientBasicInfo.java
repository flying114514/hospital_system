package com.hui.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 患者基本信息
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("basic_patient")
@Builder
public class PatientBasicInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String gender;

    @TableField("id_card")
    private String idCard;

    private Integer age;

    private String phone;

    private Integer status;

    private String password;


}
