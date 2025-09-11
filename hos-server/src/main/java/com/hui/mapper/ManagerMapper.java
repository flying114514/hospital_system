package com.hui.mapper;

import com.github.pagehelper.Page;
import com.hui.dto.AllTimeDTO;
import com.hui.dto.ManagerLoginDTO;
import com.hui.entity.DoctorVisitStats;
import com.hui.vo.AllTimeVO;
import com.hui.vo.ManagerLoginVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ManagerMapper {

    //得到一周内的医生排版数据
    Page<AllTimeVO> getAllTime(AllTimeDTO allTimeDTO);

    //查询管理员是否存在
    ManagerLoginVO login(ManagerLoginDTO managerLoginDTO);

    //获取top5
    List<DoctorVisitStats> getTopDoctorsByVisits(int number);

    //指定时间内模糊查询医生排版信息
    List<AllTimeVO> getAllTimeList(AllTimeDTO allTimeDTO);
}
