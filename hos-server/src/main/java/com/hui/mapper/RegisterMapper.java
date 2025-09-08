package com.hui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.hui.dto.DoctorPageQueryDTO;
import com.hui.dto.PayDTO;
import com.hui.dto.TimeDTO;
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

    //根据医生姓名查找余浩
    Page<TimeDetails> pageTime(TimePageQueryDTO timePageQueryDTO);

    //根据身份证号获取id
    @Select("select id from basic_patient where id_card=#{idCard} and status=0")
    Long getIdByIdCard(String idCard);

    //将号数存如挂号单
    void setNumber(TimeDTO timeDTO);

    //根据患者id查询医生id
    Long getDoctorId(Long currentPatientId);

    //根据医生id,更新医生号表,被选的号状态改为1
    void updateDoctorTime(Long doctorId, String number);

    //根据医生id,将余数减一
    void updateDoctorRemain(Long doctorId);

    //根据患者id查询详细信息
    Registration getAllInfo(Long currentPatientId);

    //根据患者id删除状态是未挂号的条目
    void deleteInfo(Long currentPatientId);

    //根据id查询银行账户余额
    Double getBankById(PayDTO payDTO);

    //患者缴费,根据支付方式在银行账户扣钱
    void minusMoney(PayDTO payDTO);

    //患者交给,在医保卡里扣钱
    void minusCard(PayDTO payDTO);

    //根据患者id和未支付的状态设置挂号单将要过期的时间
    void setEndTime(Long patientId);

    //根据挂号单id直接精确设置挂号单结束时间和状态
    void setUnpaid(Integer registerId);
}
