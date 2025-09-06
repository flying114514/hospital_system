package com.hui.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hui.dto.PatientBasicInfoDTO;
import com.hui.entity.PatientBasicInfo;
import com.hui.mapper.CreateMapper;
import com.hui.service.CreateService;
import org.springframework.stereotype.Service;

@Service
public class CreateServiceImpl extends ServiceImpl<CreateMapper, PatientBasicInfo> implements CreateService {

    @Override
    public PatientBasicInfo getPatientInfo(PatientBasicInfoDTO patientBasicInfoDTO) {
        return null;
    }

    @Override
    public PatientBasicInfo insertPatientInfo(PatientBasicInfoDTO patientBasicInfoDTO) {
        return null;
    }
}
