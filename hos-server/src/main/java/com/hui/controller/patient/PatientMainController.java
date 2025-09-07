package com.hui.controller.patient;

import com.hui.constant.JwtClaimsConstant;
import com.hui.dto.LoginDTO;
import com.hui.properties.JwtProperties;
import com.hui.result.Result;
import com.hui.service.PatientMainService;
import com.hui.utils.JwtUtil;
import com.hui.vo.LoginVO;
import com.hui.vo.PatientLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 患者主页面
 * */
@RequestMapping("/user")
@RestController
@Slf4j
public class PatientMainController {

    @Autowired
    private PatientMainService patientMainService;

    @Autowired
    private JwtProperties jwtProperties;

    //患者登录
    @PostMapping("/login")
    public Result<PatientLoginVO> login(@RequestBody LoginDTO loginDTO){
        log.info("患者登录:{}",loginDTO);
        String name = loginDTO.getName();
        LoginVO loginVO =patientMainService.checkPassword(loginDTO);
            //登录成功后，生成jwt令牌,包含患者id
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtClaimsConstant.PAT_ID, loginVO.getId());
            String token = JwtUtil.createJWT(
                    jwtProperties.getPatSecretKey(),
                    jwtProperties.getPatTtl(),
                    claims);

        PatientLoginVO patientLoginVO = PatientLoginVO.builder()
                    .id(Long.valueOf(loginVO.getId()))
                    .name(name)
                    .token(token)
                    .build();
            return Result.success(patientLoginVO);

    }

    //患者充值  xxx你已成功向医保卡充值xxx元/充值失败,微信/现金余额不足
    @PostMapping("/recharge")
    public Result<String> recharge(Double money){
        //先查询用户余额是否足够
        return Result.success("");
    }
}
