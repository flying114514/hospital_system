package com.hui.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hui.dto.PatientBasicInfoDTO;
import com.hui.entity.PatientBasicInfo;

//该接口用于患者建档
public interface CreateService extends IService<PatientBasicInfo> {
    //查询数据库是否存在该用户
    PatientBasicInfo getPatientInfo(PatientBasicInfoDTO patientBasicInfoDTO);

    //插入患者信息
    PatientBasicInfo insertPatientInfo(PatientBasicInfoDTO patientBasicInfoDTO);
}
