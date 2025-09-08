package com.hui.mapper;

import com.github.pagehelper.Page;
import com.hui.dto.*;
import com.hui.entity.PayHistory;
import com.hui.vo.GuaHistoryVO;
import com.hui.vo.LoginVO;
import com.hui.vo.MedicalCardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Mapper
public interface PatientMainMapper {

    //检查密码是否正确
    LoginVO getByName(String name);

    //检查银行余额
    Double getMoney(String paymentMethod, Long currentId);

    //更新医保卡余额
    void updateMoney(Double money, Long currentId);

    //银行账户减少余额
    void minusMoney(MinusMoneyDTO minusMoneyDTO);

    //向历史充值表添加数据
    void insertPay(PayHistoryDTO payHistoryDTO);

    //返回分页查询历史充值记录结果
    Page<PayHistory> selectPayHistory(PayHistoryPageDTO payHistoryPageDTO);

    //根据id查询医保卡信息
    MedicalCardVO checkCard(Integer patientId);

    //根据id创建医保卡
    void createCard(MedicalCardDTO medicalCardDTO);

    //根据id获取患者姓名
    @Select("select name from basic_patient where id=#{patientId}" )
    String getPatientName(Integer patientId);

    //查询历史挂号信息
    Page<GuaHistoryVO> selectGuaHistory(GuaHistoryPageDTO guaHistoryPageDTO);
}
