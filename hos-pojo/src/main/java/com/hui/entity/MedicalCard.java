package com.hui.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 医保卡
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("medical_card")
public class MedicalCard {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String username;//患者名
    private Integer userId;// 患者id
    private String cardNo; // 医保卡号
    private Double balance; // 医保余额
}
