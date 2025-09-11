package com.hui.mapper;

import com.github.pagehelper.Page;
import com.hui.dto.*;
import com.hui.entity.PayHistory;
import com.hui.vo.GuaHistoryVO;
import com.hui.vo.LoginVO;
import com.hui.vo.MedicalCardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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

    //根据挂单号获取支付方式
    @Select("select payment_method from orders where number=#{number} and status=#{status}")
    String getPaymentMethod(CancelIngDTO cancelingDTO);

    //将状态设置为取消中
    @Update("update orders set status=#{status} where number=#{number}")
    void setStatus(CancelIngDTO cancelingDTO);

    //见状态改回来
    @Update("update orders set status=#{status} where number=#{number}")
    void resrtStstus(CancelIngDTO cancelingDTO);

    //设置星评
    void setStar(StarDTO starDTO);

    //根据id查询患者姓名
    @Select("select name from basic_patient where id=#{patientId}")
    String getNameById(Long patientId);

    //检查患者今天有没有评过分
    @Select("select star from star where patient_id=#{patientId} and date(create_time) = CURDATE()")
    Double checkStar(StarDTO starDTO);

    //计算平均分
    @Select("SELECT ROUND(AVG(star), 1) FROM star WHERE star IS NOT NULL")
    Double getAvgStar();

    //获取全部挂号数据
    List<GuaHistoryVO> getAllTimeList(AllTimeDTO allTimeDTO);

    //获得全部缴费数据
    List<PayHistory> getAllPayList(AllTimeDTO allTimeDTO);
}
