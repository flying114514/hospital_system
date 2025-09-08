package com.hui.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hui.constant.RegisteredStatusConstant;
import com.hui.context.BaseContext;
import com.hui.dto.*;
import com.hui.entity.*;
import com.hui.mapper.CreateMapper;
import com.hui.mapper.RegisterMapper;
import com.hui.result.PageResult;
import com.hui.service.RegisterService;
import com.hui.vo.CancelOrderVO;
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
        Double remain;
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
            remain=registerMapper.getBankById(payDTO);

            if (remain<price){
                payVO.setDetail("余额不足,请更换缴费方式或充值后缴费");
                throw new RuntimeException("余额不足");
            }
            //余额充足,在相应位置扣钱
            registerMapper.minusMoney(payDTO);

        }else {
            //医保卡支付,打折
            price *= 0.5;
            payDTO.setPrice(price);

            //查询医保卡余额是否足够
            remain=registerMapper.getCardById(payDTO);
            if (remain<price){
                payVO.setDetail("余额不足,请更换缴费方式或充值后缴费");
                throw new RuntimeException("余额不足");
            }
            //在medical_card直接修改
            registerMapper.minusCard(payDTO);

            String detail="医保卡";
            //用医保卡交钱,修改orders里的price
            registerMapper.setPrice(payDTO,detail);
        }

            //更改挂号单状态为待叫号
            UpdateStatus updateStatus = UpdateStatus.builder()
                    .id(Math.toIntExact(payDTO.getPatientId())).build();
            registerMapper.updateStatus(updateStatus);

            payVO.setDetail(rawPaymentMethod+"支付成功");
            //返回成功信息
            return payVO;


    }

    //患者取消挂号
    @Override
    public CancelOrderVO cancelOrder(CancelOrderDTO cancelOrderDTO) {
        CancelOrderVO cancelOrderVO = null;

        //判断当前时间距离预计就诊时间是否还有15分钟,只查询待叫号的挂号单
        cancelOrderDTO.setStatus(RegisteredStatusConstant.WAIT_FOR_CALL);

        //前端已经为我们传递的只可能是一个数据,返回的不可能是list
        LocalDateTime time=registerMapper.getEndTime(cancelOrderDTO);

        //在15分钟内,不可取消
        if (time.isAfter(LocalDateTime.now().plusMinutes(15))){
            cancelOrderVO.setDetail("距离预计就诊时间不足15分钟,取消挂号失败");
            return cancelOrderVO;
        }
        //不在15分钟内,可以取消
        registerMapper.cancelOrder(cancelOrderDTO);
        cancelOrderVO.setDetail("取消挂号单成功");
        return cancelOrderVO;
    }


}
