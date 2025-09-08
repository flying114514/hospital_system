package com.hui.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hui.dto.DoctorPageQueryDTO;
import com.hui.dto.PayDTO;
import com.hui.dto.RegistrationDTO;
import com.hui.dto.TimeDTO;
import com.hui.entity.*;
import com.hui.mapper.CreateMapper;
import com.hui.mapper.RegisterMapper;
import com.hui.result.PageResult;
import com.hui.service.RegisterService;
import com.hui.vo.PayVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegisterServiceImpl extends ServiceImpl<RegisterMapper, Registration> implements RegisterService {

    @Autowired
    private RegisterMapper registerMapper;
    @Autowired
    private CreateMapper createMapper;


    //患者挂号
    @Override
    public List<DepartmentList> register(RegistrationDTO registrationDTO,Long currentPatientId) {
        List<DepartmentList> departmentLists = registerMapper.getDepartments();
        return departmentLists;//返回科室名列表
    }

    //根据科室名称模糊分页查询相关信息
    @Override
    public PageResult pageDoctor(DoctorPageQueryDTO doctorPageQueryDTO) {
        int pageSize = doctorPageQueryDTO.getPageSize();
        int pageNum = doctorPageQueryDTO.getPage();
        PageHelper.startPage(pageNum, pageSize);
        Page<DoctorDetails> page = registerMapper.pageDoctor(doctorPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //根据医生姓名获取详细信息
    @Override
    public PageTime selectTime(TimePageQueryDTO timePageQueryDTO, Long currentPatientId) {
        String name = timePageQueryDTO.getName();

        PageTime pageTime =registerMapper.getDetailByName(name);

        PageHelper.startPage(timePageQueryDTO.getPage(),timePageQueryDTO.getPageSize());

        Page<TimeDetails> page=registerMapper.pageTime(timePageQueryDTO);
        pageTime.setPageResult(new PageResult(page.getTotal(),page.getResult()));

        TemplateInfo templateInfo = TemplateInfo.builder()
                .doctorName(name)
                .doctorId(String.valueOf(pageTime.getDoctorId()))
                .department(pageTime.getDepartment())
                .departmentLocation(pageTime.getDepartmentLocation())
                .price(pageTime.getPrice())
                .id(String.valueOf(currentPatientId))
                .level(pageTime.getLevel())
                .build();
        registerMapper.update(templateInfo);


        return pageTime;
    }

    //患者点击取号,传入挂号数,医生对应总余浩减一,挂号数传入数据库
    @Override//number是传入的number
    public String getNumber(Long currentPatientId, String number) {
        //不用判断挂号数是否在可选的数内,因为前端已经判断过了
        //能选到的号都是没人选的,可以选择的,不需要检验
        //直接将该数存入患者挂号单中,并且其他患者挂号是将查不到这个号

        TimeDTO timeDTO = TimeDTO.builder()
                .currentPatientId(String.valueOf(currentPatientId))
                .Status("0")
                .number(number)
                .predictTime(String.valueOf(LocalDateTime.now().plusMinutes(15)))
                .build();

        //将该号存入患者挂号单
        registerMapper.setNumber(timeDTO);
        //其他患者查不到该号
        //改变挂单号状态为1
        //根据患者id查询orders,获取该患者现在的医生id
        Long doctorId=registerMapper.getDoctorId(currentPatientId);

        if(doctorId!=null){
            //根据获得的医生id查询time_details,将被选择的号状态设置为1
            registerMapper.updateDoctorTime(doctorId,number);

            //修改患者点击科室后出现的医生取号列表

            //更改doctor_details表,将余数减一
            registerMapper.updateDoctorRemain(doctorId);
            return "取得号:" + number;
        }

        return "未获得医生相关信息";


    }

    //获取用户全部详细信息
    @Override
    public Registration getAllInfo(Long currentPatientId) {
        Registration registration=registerMapper.getAllInfo(currentPatientId);

        return registration;
    }

    //根据患者id删除数据
    @Override
    public void deleteInfo(Long currentPatientId) {
        registerMapper.deleteInfo(currentPatientId);
    }

    //患者缴费
    @Override
    public PayVO pay(PayDTO payDTO) {

        //根据id查询银行账户或医保卡,两种方式不同
        String paymentMethod = payDTO.getPaymentMethod();
        String rawPaymentMethod=paymentMethod;
        Double price = payDTO.getPrice();

        PayVO payVO = new PayVO();
        if(paymentMethod.equals("微信") || paymentMethod.equals("现金")){
            //原价挂号,调用bank表

            //将微信和现金转成数据库格式
            if (paymentMethod.equals("微信")){
                paymentMethod="wechat_pay";
            }else {
                paymentMethod="cash";
            }
            payDTO.setPaymentMethod(paymentMethod);

            //先查询银行余额,判断是否足够
            Double remain=registerMapper.getBankById(payDTO);
            if (remain<price){
                payVO.setDetail("余额不足,请更换缴费方式或充值后缴费");
                throw new RuntimeException("余额不足");
            }
            //余额充足,在相应位置扣钱
            registerMapper.minusMoney(payDTO);
            //TODO:在患者个人界面的查询历史挂号记录
            payVO.setDetail(rawPaymentMethod+"支付成功");

            return payVO;
            //返回成功信息
        }else {
            //医保卡支付,打折
            price*=0.5;
            payDTO.setPrice(price);
            //在medical_card直接修改
            registerMapper.minusCard(payDTO);
            //TODO更改成功,在患者个人界面查询历史挂号记录,微信支付和医保卡支付所存的表和逻辑是一样的
            payVO.setDetail("医保卡支付成功");
            return payVO;
        }
    }


}
