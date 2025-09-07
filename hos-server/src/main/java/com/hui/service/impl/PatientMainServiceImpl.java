package com.hui.service.impl;

import com.hui.constant.MessageConstant;
import com.hui.context.BaseContext;
import com.hui.dto.LoginDTO;
import com.hui.dto.RechargeDTO;
import com.hui.exception.AccountNotFoundException;
import com.hui.exception.PasswordErrorException;
import com.hui.mapper.PatientMainMapper;
import com.hui.service.PatientMainService;
import com.hui.vo.LoginVO;
import com.hui.vo.PatientLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PatientMainServiceImpl implements PatientMainService {

    @Autowired
    private PatientMainMapper patientMainMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


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
    public String checkMoney(RechargeDTO rechargeDTO) {
        Long id=BaseContext.getCurrentId();
        Double money = rechargeDTO.getMoney();//要存的钱
        String paymentMethod = rechargeDTO.getPaymentMethod();
        Double remain=patientMainMapper.getMoney(paymentMethod,id);
        if (remain<money){
            return "余额不足";
        }
        //银行减少余额
        patientMainMapper.minusMoney(money,paymentMethod,id);

        //医保卡修改余额
        patientMainMapper.updateMoney(money,id);
        return "充值"+money+"成功";
    }
}
