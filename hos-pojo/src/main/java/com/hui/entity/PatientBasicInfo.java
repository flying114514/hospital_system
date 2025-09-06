package com.hui.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("basic_patient")
public class PatientBasicInfo {
    @TableId(value="id",type= IdType.AUTO)
    private Long id;
    private String name;
    private String gender;
    private String idCard;
    private Integer age;
    private String phone;
    private Integer status;
}
