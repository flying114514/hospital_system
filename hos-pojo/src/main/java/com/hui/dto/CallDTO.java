package com.hui.dto;

import com.hui.constant.RegisteredStatusConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *改变叫号状态所需对象
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CallDTO {
    private Integer status;
    private Integer number;
    private Integer id;
    private LocalDateTime callTime;

}
