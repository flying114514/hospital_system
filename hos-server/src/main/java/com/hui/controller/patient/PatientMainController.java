package com.hui.controller.patient;

import com.hui.constant.JwtClaimsConstant;
import com.hui.context.BaseContext;
import com.hui.dto.*;
import com.hui.entity.ResultDetail;
import com.hui.properties.JwtProperties;
import com.hui.result.PageResult;
import com.hui.result.Result;
import com.hui.service.PatientMainService;
import com.hui.utils.JwtUtil;
import com.hui.vo.LoginVO;
import com.hui.vo.MedicalCardVO;
import com.hui.vo.PatientLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 患者主页面
 * */
@RequestMapping("/user/main")
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

    //查询用户是否有医保卡,如果没有,要添加医保卡账户,如果有,展示医保卡信息
    @PostMapping("/login/checkcard")
    public Result<MedicalCardVO> checkCard(MedicalCardDTO medicalCardDTO){
        Long patientId = BaseContext.getCurrentId();
        medicalCardDTO.setId(Math.toIntExact(patientId));
        MedicalCardVO medicalCardVO=patientMainService.checkCard(medicalCardDTO);
        return Result.success(medicalCardVO);
    }

    //患者充值  xxx你已成功向医保卡充值xxx元/充值失败,微信/现金余额不足
    @PostMapping("/recharge")
    public Result<ResultDetail> recharge(@RequestBody RechargeDTO rechargeDTO){
        //先查询用户余额是否足够,已经在用户建档时创建了银行账户
        ResultDetail resultDetail=patientMainService.checkMoney(rechargeDTO);
        return Result.success(resultDetail);
    }

    //患者查看历史缴费记录
    @GetMapping("/history")
    public Result<PageResult> selectPayHistory(@RequestBody PayHistoryPageDTO payHistoryPageDTO){
        Long patientId = BaseContext.getCurrentId();
        payHistoryPageDTO.setPatientId(Math.toIntExact(patientId));
        PageResult pageResult=patientMainService.selectPayHistory(payHistoryPageDTO);
        return Result.success(pageResult);
    }

    //患者查看历史挂号记录
    @GetMapping("/guahistory")
    public Result<PageResult> selectGuaHistory(@RequestBody GuaHistoryPageDTO guaHistoryPageDTO){
        Long patientId = BaseContext.getCurrentId();
        guaHistoryPageDTO.setPatientId(Math.toIntExact(patientId));
        PageResult pageResult=patientMainService.selectGuaHistory(guaHistoryPageDTO);
        return Result.success(pageResult);
    }

}
