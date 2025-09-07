package com.hui.mapper;

import com.hui.vo.LoginVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PatientMainMapper {

    //检查密码是否正确
    LoginVO getByName(String name);
}
