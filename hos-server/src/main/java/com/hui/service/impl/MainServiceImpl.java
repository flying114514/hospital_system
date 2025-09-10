package com.hui.service.impl;

import com.hui.constant.BasicStatusConstant;
import com.hui.service.MainService;
import com.hui.vo.RoleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MainServiceImpl implements MainService {

    @Override
    @Transactional
    public RoleVO getRole(String role) {
        if (role.equals(BasicStatusConstant.DOCTOR)){
            return new RoleVO(BasicStatusConstant.DOCTOR,BasicStatusConstant.DOCTOR_SUCCESS);

        }else if(role.equals(BasicStatusConstant.PATIENT)){
            return new RoleVO(BasicStatusConstant.PATIENT,BasicStatusConstant.PATIENT_SUCCESS);
        }
        return null;
    }
}
