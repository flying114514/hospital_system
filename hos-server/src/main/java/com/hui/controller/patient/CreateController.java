package com.hui.controller.patient;

import com.hui.constant.BasicStatusConstant;
import com.hui.constant.MessageConstant;
import com.hui.dto.PatientBasicInfoDTO;
import com.hui.entity.PatientBasicInfo;
import com.hui.entity.SetPassword;
import com.hui.result.Result;
import com.hui.service.CreateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
/**
 * 患者建档相关,只要进来了就一定是开始建档
 * */
@RestController
@RequestMapping("/user/create")
@Slf4j
public class CreateController {

    @Autowired
    private CreateService createService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 患者建档,需要传进来一个对象,封装患者身份证号,手机号,根据身份证算出性别,年龄,状态统一为未挂号
     * ,同时对身份证进行验证,返回异常信息,如果成功直接返回数据
     */

    //新增密码
    @PostMapping("/password")
    public Result setPassword(@RequestBody SetPassword setPassword){
        //获取用户信息
        PatientBasicInfo basicInfo = createService.lambdaQuery()
                .eq(PatientBasicInfo::getIdCard, setPassword.getIdCard())
                .one();
        if (basicInfo == null){
            return Result.error(MessageConstant.IDCARD_ERROR);
        }

        //密码加密
        // 获取用户输入的原始密码
        String rowPassword = setPassword.getPassword();

        // 使用 BCryptPasswordEncoder 对密码进行加密
        String encodedPassword = passwordEncoder.encode(rowPassword);

        //新增密码
        createService.lambdaUpdate()
                .eq(PatientBasicInfo::getId,basicInfo.getId())
                .set(PatientBasicInfo::getPassword,encodedPassword)
                .update();
        log.info("用户:{}新增密码成功",basicInfo.getName());
        return Result.success(BasicStatusConstant.PASSWORD_SUCCESS);
    }
}
