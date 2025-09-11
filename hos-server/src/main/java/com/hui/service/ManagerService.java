package com.hui.service;

import com.hui.dto.AllTimeDTO;
import com.hui.dto.ManagerLoginDTO;
import com.hui.entity.DoctorVisitStats;
import com.hui.result.PageResult;
import com.hui.vo.AllTimeVO;
import com.hui.vo.ManagerLoginVO;

import java.util.List;

public interface ManagerService {

    //根据名字模糊查询一周内排版信息
    PageResult getAllTime(AllTimeDTO allTimeDTO);

    //管理员登录
    ManagerLoginVO login(ManagerLoginDTO managerLoginDTO);

    //实时获取top5
    List<DoctorVisitStats> getTopDoctorsByVisits(int number);

    //指定时间内模糊查询患者数量,根据每天的日期分组
    List<AllTimeVO> getAllTimeList(AllTimeDTO allTimeDTO);
}
