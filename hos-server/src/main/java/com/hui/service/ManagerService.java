package com.hui.service;

import com.hui.dto.AllTimeDTO;
import com.hui.dto.ManagerLoginDTO;
import com.hui.result.PageResult;
import com.hui.vo.ManagerLoginVO;

public interface ManagerService {

    //根据名字模糊查询一周内排版信息
    PageResult getAllTime(AllTimeDTO allTimeDTO);

    //管理员登录
    ManagerLoginVO login(ManagerLoginDTO managerLoginDTO);
}
