package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
/**
 *医生忘记密码所需对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePasswordDTO {
    private String idCard;
    private String name;
    private String password;
}
