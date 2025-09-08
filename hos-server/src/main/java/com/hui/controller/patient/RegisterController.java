package com.hui.controller.patient;

import com.hui.constant.MessageConstant;
import com.hui.dto.*;
import com.hui.entity.*;
import com.hui.mapper.RegisterMapper;
import com.hui.result.PageResult;
import com.hui.result.Result;
import com.hui.service.CreateService;
import com.hui.service.RegisterService;
import com.hui.vo.PayVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/user/register")
@Slf4j
public class RegisterController {


    @Autowired
    private RegisterService registerService;

    @Autowired
    private CreateService createService;

    @Autowired
    private RegisterMapper registerMapper;

    //首先查询患者信息,如果有,展示用户信息,退出界面,没有才能建档
    @GetMapping
    public Result<PatientBasicInfo> getPatientInfo(PatientBasicInfoDTO patientBasicInfoDTO, HttpSession session) {
        String idCard = patientBasicInfoDTO.getIdCard();

        if (idCard == null || (idCard.length() != 18)) {
            return Result.error(MessageConstant.IDCARD_ERROR);
        }

        //查到信息直接返回
        PatientBasicInfo basicInfo = createService.lambdaQuery()
                .eq(PatientBasicInfo::getIdCard, idCard)
                .one();

        if (basicInfo ==null){
            //没有该患者的信息,需要新增
            basicInfo =createService.insertPatientInfo(patientBasicInfoDTO);
            log.info("新增患者信息:{}",basicInfo);
            return Result.success(basicInfo);
        }
            //患者已存在,也要设置currentPatientId
        else{
            //向orders插入数据
            createService.insertPatientInfo(patientBasicInfoDTO);
        }
            // 使用Session存储患者ID
            session.setAttribute("currentPatientId", basicInfo.getId());
            return Result.success(basicInfo);

    }

    //患者挂号
    @PostMapping()
    public Result<List<DepartmentList>> register(RegistrationDTO registrationDTO, HttpSession session){

        Long currentPatientId = (Long) session.getAttribute("currentPatientId");
        if (currentPatientId == null) {
            return Result.error("请先进行患者信息查询或建档");
        }
        List<DepartmentList> list=registerService.register(registrationDTO,currentPatientId);
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
    public Result<PageTime> selectTime(TimePageQueryDTO timePageQueryDTO,HttpSession session){
        Long currentPatientId = (Long) session.getAttribute("currentPatientId");
        if (currentPatientId == null) {
            return Result.error("请先进行患者信息查询或建档");
        }
        PageTime pageTime=registerService.selectTime(timePageQueryDTO,currentPatientId);
        return Result.success(pageTime);
    }

    //患者点击取号,传入挂号数,医生对应总余浩减一,挂号数传入数据库
    @PostMapping("/getnumber")
    public Result<String> getNumber(HttpSession session,String number){
        Long currentPatientId = (Long) session.getAttribute("currentPatientId");
        String word=registerService.getNumber(currentPatientId, number);
        return Result.success(word);
    }

    //患者选择完号后,展示所有挂号信息,如果前端传送的数据是确定,展示数据,如果是取消,删除条目
    @GetMapping("/all")
    public Result<Registration> getAllInfo(HttpSession session,String word) {
        Long currentPatientId = (Long) session.getAttribute("currentPatientId");
        Registration registration;
        if (word.equals("确定")) {
            registration = registerService.getAllInfo(currentPatientId);
            session.setAttribute("registerId", registration.getId());
            session.setAttribute("payMoney", registration.getPrice());
            return Result.success(registration);
        } else {
            registerService.deleteInfo(currentPatientId);
            return Result.success();
        }

    }

    //患者缴费,微信/现金支付原价,医保卡支付打折
    @PostMapping("/pay")
    public Result<PayVO> pay(HttpSession session, PayDTO payDTO){

        Long patientId = (Long) session.getAttribute("currentPatientId");

        payDTO.setPatientId(Math.toIntExact(patientId));
        payDTO.setPrice((double) session.getAttribute("payMoney"));

        if(payDTO.getDetail().equals("确定")){//开始缴费
            PayVO payVO=registerService.pay(payDTO);
            return Result.success(payVO);
        }else{
            //患者取消支付,将挂号单的信息保存15分钟,状态设置为待支付,15分钟后状态设置为未挂号
            //获取挂号单id
            Integer registerId = (Integer) session.getAttribute("registerId");
            SetUnpaidDTO.builder()
                    .registerId(registerId)
            //将挂号单的状态设置为待支付,根据挂号单id
            registerMapper.setUnpaid(SetUnpaidDTO);

            //将挂号单的信息保存15分钟
            registerMapper.setEndTime(patientId);

        }



    }

}


