package com.hui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hui.dto.BankDTO;
import com.hui.entity.DepartmentList;
import com.hui.entity.PatientBasicInfo;
import org.apache.ibatis.annotations.Select;

public interface CreateMapper extends BaseMapper<PatientBasicInfo> {
    //存入orders表
    void insertInfo(PatientBasicInfo patientBasicInfo);

    //根据idCard查询
    @Select("select name from basic_patient where id_card=#{idCard}")
    DepartmentList selectInfo(String idCard);

    //根据idCard查询patientId
    Long getPatientIdByIdCard(String idCard);

    //新建银行账户
    void insertBank(BankDTO bankDTO);
}
