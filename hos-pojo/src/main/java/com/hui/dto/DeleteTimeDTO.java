package com.hui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * 定时删除排版信息所需对象
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteTimeDTO {
    private LocalDateTime expiredDate;
    private Integer doctorId;

}
