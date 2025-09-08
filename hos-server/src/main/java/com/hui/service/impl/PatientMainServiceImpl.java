package com.hui.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hui.constant.MessageConstant;
import com.hui.context.BaseContext;
import com.hui.dto.*;
import com.hui.entity.PayHistory;
import com.hui.entity.ResultDetail;
import com.hui.exception.AccountNotFoundException;
import com.hui.exception.PasswordErrorException;
import com.hui.mapper.PatientMainMapper;
import com.hui.result.PageResult;
import com.hui.service.PatientMainService;
import com.hui.vo.LoginVO;
import com.hui.vo.MedicalCardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PatientMainServiceImpl implements PatientMainService {

    @Autowired
    private PatientMainMapper patientMainMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PatientMainService patientMainService;


    //根据患者姓名检查密码
    @Override
    public LoginVO checkPassword(LoginDTO loginDTO) {
        String password = loginDTO.getPassword();
        //1、根据姓名查询数据库中的数据
        LoginVO loginVO =patientMainMapper.getByName(loginDTO.getName());
        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (loginVO == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //对前端传过来的密码进行加密处理
        boolean result = passwordEncoder.matches(password, loginVO.getPassword());
        if (!result) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        //3、返回实体对象
        return loginVO;
    }

    //查询余额是否足够
    @Override
    public ResultDetail checkMoney(RechargeDTO rechargeDTO) {
        Long id=BaseContext.getCurrentId();
        Double money = rechargeDTO.getMoney();//要存的钱
        String paymentMethod = rechargeDTO.getPaymentMethod();

        //将前端传来的信息转化
        if (paymentMethod.equals("微信")){
            paymentMethod="wechat_pay";
        }else if(paymentMethod.equals("现金")){
            paymentMethod="cash";
        }

        String result;
        Double remain=patientMainMapper.getMoney(paymentMethod,id);
        if (remain<money){
            result="余额不足";
        }

        //银行减少余额
        MinusMoneyDTO minusMoneyDTO = MinusMoneyDTO.builder()
                .money(money)
                .patientId(Math.toIntExact(id))
                .paymentMethod(paymentMethod).build();
        patientMainMapper.minusMoney(minusMoneyDTO);

        //医保卡修改余额
        patientMainMapper.updateMoney(money,id);

        //向历史充值表添加数据
        PayHistoryDTO payHistoryDTO = PayHistoryDTO.builder()
                .patientId(Math.toIntExact(id))
                .paymentMethod(paymentMethod)
                .money(money)
                .time(String.valueOf(LocalDateTime.now()))
                .build();
        patientMainMapper.insertPay(payHistoryDTO);
        result="充值"+money+"成功";
        return new ResultDetail(result);
    }

    //查询时间区间内的充值记录
    @Override
    public PageResult selectPayHistory(PayHistoryPageDTO payHistoryPageDTO) {
        Integer pageSize = payHistoryPageDTO.getPageSize();
        Integer pageNum = payHistoryPageDTO.getPage();
        PageHelper.startPage(pageNum,pageSize);
        Page<PayHistory> page=patientMainMapper.selectPayHistory(payHistoryPageDTO);

        return new PageResult(page.getTotal(),page.getResult());
    }

    //查询患者医保卡信息
    @Override
    public MedicalCardVO checkCard(MedicalCardDTO medicalCardDTO) {
        Integer patientId = medicalCardDTO.getId();
        //根据id查询医保卡
        MedicalCardVO medicalCardVO=patientMainMapper.checkCard(patientId);
        medicalCardVO.setDetail("您的医保卡查询成功");

        if(medicalCardVO==null){
            //根据id查询患者姓名
            String name = patientMainMapper.getPatientName(patientId);
            medicalCardDTO.setName(name);

            //新增医保卡
            patientMainMapper.createCard(medicalCardDTO);
            //再次查询医保卡
            medicalCardVO=patientMainMapper.checkCard(patientId);
            medicalCardVO.setDetail("您的医保卡尚不存在,已为您新建医保卡");
        }
        return medicalCardVO;
    }
}
