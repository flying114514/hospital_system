package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 挂号所需实体类
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationDTO {
    private String name;
    private String idCard;
    private String Card;
}
