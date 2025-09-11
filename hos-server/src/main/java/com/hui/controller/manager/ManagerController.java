package com.hui.controller.manager;

import com.hui.constant.JwtClaimsConstant;
import com.hui.dto.AllTimeDTO;
import com.hui.dto.ManagerLoginDTO;
import com.hui.properties.JwtProperties;
import com.hui.result.PageResult;
import com.hui.result.Result;
import com.hui.service.ManagerService;
import com.hui.utils.JwtUtil;
import com.hui.vo.ManagerLoginVO;
import com.hui.vo.PatientLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/manager")
@Slf4j
public class ManagerController {

    @Autowired
    public ManagerService managerService;

    @Autowired
    public JwtProperties jwtProperties;

    //管理员登录
    @PostMapping("/login")
    public Result<PatientLoginVO> login(@RequestBody ManagerLoginDTO managerLoginDTO){
        log.info("管理员登录:{}",managerLoginDTO);
        String name = managerLoginDTO.getName();
        ManagerLoginVO managerLoginVO=managerService.login(managerLoginDTO);
        //登录成功后，生成jwt令牌,包含患者id
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.MAN_ID, managerLoginVO.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getManSecretKey(),
                jwtProperties.getManTtl(),
                claims);

        PatientLoginVO patientLoginVO = PatientLoginVO.builder()
                .id(Long.valueOf(managerLoginVO.getId()))
                .name(name)
                .token(token)
                .build();
        return Result.success(patientLoginVO);
    }

    //根据时间,姓名模糊分页得到所有排班信息
    @GetMapping("/timeinfo")
    public Result<PageResult> getAllTime(@RequestBody AllTimeDTO allTimeDTO){
        PageResult pageResult=managerService.getAllTime(allTimeDTO);
        return Result.success(pageResult);
    }




}
