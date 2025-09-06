package com.hui.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hui.dto.PatientBasicInfoDTO;
import com.hui.entity.PatientBasicInfo;

//该接口用于患者建档
public interface CreateService extends IService<PatientBasicInfo> {

    //根据患者身份证补充信息,并新增患者
    PatientBasicInfo insertPatientInfo(PatientBasicInfoDTO patientBasicInfoDTO);

}
