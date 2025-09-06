package com.hui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hui.entity.PatientBasicInfo;

public interface CreateMapper extends BaseMapper<PatientBasicInfo> {
    //存入orders表
    void insertInfo(PatientBasicInfo patientBasicInfo);
}
