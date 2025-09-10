package com.hui.dto;

import com.hui.constant.RegisteredStatusConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.status.StatusConsoleListener;

/**
 * 医生叫号返回结果对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallNumberDTO {
    private Integer number;
    private Integer status= RegisteredStatusConstant.WAIT_FOR_CALL;

}
