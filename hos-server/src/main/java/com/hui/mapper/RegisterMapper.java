package com.hui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.hui.dto.*;
import com.hui.entity.*;
import com.hui.vo.OverTimeVO;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

public interface RegisterMapper extends BaseMapper<Registration> {
    //根据科室名模糊查询医生相关信息
    Page<DoctorDetails> pageDoctor(DoctorPageQueryDTO doctorPageQueryDTO);

    //插入临时数据
    void update(TemplateInfo templateInfo);

    //获取科室信息
    @Select("select name from departments")
    List<DepartmentList> getDepartments();

    //根据医生姓名获取详细信息
    @Select("select * from doctor_details where name=#{name}")
    PageTime getDetailByName(String name);

    //根据医生姓名查找余浩
    Page<TimeDetails> pageTime(TimePageQueryDTO timePageQueryDTO);

    //根据身份证号获取id
    @Select("select id from basic_patient where id_card=#{idCard} and status=0")
    Long getIdByIdCard(String idCard);

    //将号数存如挂号单
    void setNumber(TimeDTO timeDTO);

    //根据患者id查询医生id
    Long getDoctorId(Long currentPatientId);

    //根据医生id,更新医生号表,被选的号状态改为1
    void updateDoctorTime(Long doctorId, String number);

    //根据医生id,将余数减一
    void updateDoctorRemain(Long workId);

    //根据患者id查询详细信息
    Registration getAllInfo(Long currentPatientId);

    //根据患者id删除状态是未挂号的条目
    void deleteInfo(Long currentPatientId);

    //根据id查询银行账户余额
    Double getBankById(PayDTO payDTO);

    //患者缴费,根据支付方式在银行账户扣钱
    void minusMoney(PayDTO payDTO);

    //患者交给,在医保卡里扣钱
    void minusCard(PayDTO payDTO);

    //根据挂号单id直接精确设置挂号单结束时间和状态
    void setUnpaid(SetUnpaidDTO setUnpaidDTO);

    //患者支付成功后,向orders补全信息
    void insertFullInfo(OrderFull orderFull);

    //查询超时未支付挂号单
    List<OverTimeVO> getOverTimeOrders(Integer unPaid, LocalDateTime localDateTime);

    //更新未支付状态
    void updateOverTime(OverTimeVO orders);

    //支付完成后更改挂号单状态
    void updateStatus(UpdateStatus updateStatus);

    //查询医保卡余额是否足够
    Double getCardById(PayDTO payDTO);

    //用医保卡支付,将orders里的price改为一半
    void setPrice(PayDTO payDTO);

    //患者取消挂号
    void cancelOrder(CancelIngDTO cancelingDTO);

    //获取想要取消的挂号单的结束时间
    LocalDateTime getPreTime(CancelIngDTO cancelingDTO);

    //先更改orders里的支付方式
    void setPaymentMethod(String paymentMethod);

    //从orders获取交的钱
    @Select("select price from orders where number=#{number} and status=#{status}")
    Double getMoney(CancelIngDTO cancelingDTO);

    //将退款返回给银行账户
    void returnBankMoney(ReturnMoneyDTO returnMoneyDTO);

    //将退款返回给医保卡
    void returnCardMoney(ReturnMoneyDTO returnMoneyDTO);

    //将医生的号还回去
    void returnDocNum(ReturnDocNumDTO returnDocNumDTO);

    //根据号数获取predictTime
    @Select("select predict_time from time_details where number=#{number}")
    String getPredictTime(String number);

    //根据号数获取workId
    @Select("select work_id from time_details where number=#{number}")
    Integer getWorkId(String number);

    //查询正在呼叫但未到号的挂号单
    List<OverTimeVO> getCalledButNotArrivedOrders(Integer status, LocalDateTime now);

    //更新挂号单状态为失败
    void updateFail(OverTimeVO order);
}
