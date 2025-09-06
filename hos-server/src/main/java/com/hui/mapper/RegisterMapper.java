package com.hui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.hui.dto.DoctorPageQueryDTO;
import com.hui.entity.*;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RegisterMapper extends BaseMapper<Registration> {
    //根据科室名模糊查询医生相关信息
    Page<DoctorDetails> pageDoctor(DoctorPageQueryDTO doctorPageQueryDTO);

    //插入临时数据
    void update(TemplateInfo templateInfo);

    //获取科室信息
    @Select("select name from departments")
    List<DepartmentList> getDepartments();

    //根据医生姓名获取详细信息
    @Select("select * from doctor_details where name=#{name}")
    PageTime getDetailByName(String name);



}
