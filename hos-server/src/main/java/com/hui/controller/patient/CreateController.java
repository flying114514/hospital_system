package com.hui.controller.patient;

import com.hui.dto.PatientBasicInfoDTO;
import com.hui.entity.PatientBasicInfo;
import com.hui.result.Result;
import com.hui.service.CreateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 患者建档相关,只要进来了就一定是开始建档
 * */
@RestController
@RequestMapping("/user/create")
@Slf4j
public class CreateController {

    @Autowired
    private CreateService createService;

    /**
     * 患者建档,需要传进来一个对象,封装患者身份证号,手机号,根据身份证算出性别,年龄,状态统一为未挂号
     * ,同时对身份证进行验证,返回异常信息,如果成功直接返回数据
     */
    //首先查询患者信息,如果有,展示用户信息,退出界面,没有才能建档
    @GetMapping
    public Result<PatientBasicInfo> getPatientInfo(PatientBasicInfoDTO patientBasicInfoDTO) {
        //查到信息直接返回
        PatientBasicInfo patientBasicInfo =createService.getPatientInfo(patientBasicInfoDTO);
        if (patientBasicInfo ==null){
            //没有该患者的信息,需要新增
            patientBasicInfo =createService.insertPatientInfo(patientBasicInfoDTO);
            return Result.success(patientBasicInfo);
        }
        return Result.success(patientBasicInfo);
    }
}
