package com.hui.service;

import com.hui.dto.LoginDTO;
import com.hui.vo.LoginVO;
import com.hui.vo.PatientLoginVO;

public interface PatientMainService {

    //根据患者姓名检查密码
    LoginVO checkPassword(LoginDTO loginDTO);

}
