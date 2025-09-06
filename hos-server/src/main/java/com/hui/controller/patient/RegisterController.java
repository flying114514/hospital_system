package com.hui.controller.patient;

import com.hui.dto.DoctorPageQueryDTO;
import com.hui.dto.RegistrationDTO;
import com.hui.entity.DepartmentList;
import com.hui.entity.PageTime;
import com.hui.entity.Registration;
import com.hui.entity.TimePageQueryDTO;
import com.hui.result.PageResult;
import com.hui.result.Result;
import com.hui.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/register")
@Slf4j
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    //患者挂号
    @PostMapping()
    public Result<List<DepartmentList>> register(RegistrationDTO registrationDTO){
        List<DepartmentList> list=registerService.register(registrationDTO);
        return Result.success(list);
    }


    //患者选择科室,选择完展示科室下所有医生和科室位置,医生相关信息分页查找
    @GetMapping("/page")
    public Result<PageResult> selectDepartment(DoctorPageQueryDTO doctorPageQueryDTO){
        PageResult pageResult=registerService.pageDoctor(doctorPageQueryDTO);
        return Result.success(pageResult);
    }

    //患者选择医生,获得医生详细信息,从小到大获取号数,直到号满,展示时间
    @GetMapping("/page/time")
    public Result<PageTime> selectTime(TimePageQueryDTO timePageQueryDTO){
        PageTime pageTime=registerService.selectTime(timePageQueryDTO);
        return Result.success(pageTime);
    }

    //患者点击取号,


    //患者选择完号后,展示所有挂号信息
    @GetMapping("/page/{id}/list")
    public Result<Registration> listRegistration(Registration registration){
        //registerService.listRegistration(registration);
        return null;
    }



}
