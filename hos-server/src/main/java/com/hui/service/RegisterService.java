package com.hui.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hui.dto.*;
import com.hui.entity.DepartmentList;
import com.hui.entity.PageTime;
import com.hui.entity.Registration;
import com.hui.entity.TimePageQueryDTO;
import com.hui.result.PageResult;
import com.hui.vo.CancelOrderVO;
import com.hui.vo.PayVO;

import java.util.List;

public interface RegisterService extends IService<Registration> {
    //患者挂号
    List<DepartmentList> register(RegistrationDTO registrationDTO,Long currentPatientId);

    //根据科室名查询医生,科室等信息
    PageResult pageDoctor(DoctorPageQueryDTO doctorPageQueryDTO);

    //根据医生姓名查询详细信息
    PageTime selectTime(TimePageQueryDTO timePageQueryDTO,Long currentPatientId);

    //患者点击取号,传入挂号数,医生对应总余浩减一,挂号数传入数据库
    String getNumber(Long currentPatientId, String number);

    //查询挂号全部详细信息
    Registration getAllInfo(Long currentPatientId);

    //根据id删除数据
    void deleteInfo(Long currentPatientId);

    //患者缴费
    PayVO pay(PayDTO payDTO);

    //患者取消挂号
    CancelOrderVO cancelOrder(CancelOrderDTO cancelOrderDTO);

    //将退款原路退回
    void returnMoney(ReturnMoneyDTO returnMoneyDTO);
}
