package com.hui.service;

import com.hui.dto.*;
import com.hui.entity.ResultDetail;
import com.hui.result.PageResult;
import com.hui.vo.LoginVO;
import com.hui.vo.MedicalCardVO;

public interface PatientMainService {

    //根据患者姓名检查密码
    LoginVO checkPassword(LoginDTO loginDTO);

    //查询余额是否足够
    ResultDetail checkMoney(RechargeDTO rechargeDTO);

    //时间区间内查询充值历史记录
    PageResult selectPayHistory(PayHistoryPageDTO payHistoryPageDTO);

    //查询患者医保卡信息
    MedicalCardVO checkCard(MedicalCardDTO medicalCardDTO);

    //查询患者历史挂号数据
    PageResult selectGuaHistory(GuaHistoryPageDTO guaHistoryPageDTO);

    //患者为医院评分
    String setStar(StarDTO starDTO);
}
