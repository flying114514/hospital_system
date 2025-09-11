package com.hui.mapper;

import com.github.pagehelper.Page;
import com.hui.dto.*;
import com.hui.entity.CallName;
import com.hui.entity.Cases;
import com.hui.entity.DetailInfo;
import com.hui.entity.DocBasic;
import com.hui.vo.BanInfoVO;
import com.hui.vo.DocLoginVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface DocMainMapper {
    //根据身份证号获取详细医生信息
    DocLoginVO getByIdCard(String idCard);

    //根据idCard,name获取医生基本信息
    DocBasic checkDoc(DocRegisterDTO docRegisterDTO);

    //医生注册
    void register(DocRegisterDTO docRegisterDTO);

    //重新设置密码
    void updatePassword(UpdatePasswordDTO updatePasswordDTO);

    //向doctor_details添加基本信息
    void addBasicInfo(DocRegisterDTO docRegisterDTO);

    //医生设置详细信息
    void setDetailInfo(DetailInfo detailInfo);

    //向doctor_details添加docid
    void insertDocId(InsertIdDTO insertIdDTO);

    //医生根据docid查询个人信息
    DetailInfo getDetailInfo(Long doctorId);

    //根据id查询级别
    @Select("select level from doctor_details where doctor_id=#{doctorId}")
    String getLevel(Integer doctorId);

    //根据id查询姓名
    @Select("select name from basic_doctor where id=#{doctorId}")
    String getNameById(Integer doctorId);

    //分页查询排班信息
    Page<BanInfoVO> getBanInfo(TimeInfoDTO timeInfoDTO);

    //添加排班表信息
    void setInfo(BanInfoDTO banInfoDTO);

    //每天两点删除一周前的信息
    void deleteSchedulesBefore(DeleteTimeDTO deleteTimeDTO);

    //批量添加排班号信息
    void batchSetBanInfo(List<BanInfoDTO> banInfoList);

    //医生叫号
    @Select("select patient_name,id from orders where number=#{number} and status=1")
    CallName getPatientInfo(CallNumberDTO callNumberDTO);

    //改变叫号状态
    @Update("update orders set status=#{status},call_time=#{callTime} where id=#{id}")
    void setStatus(CallDTO callDTO);

    //将当前患者状态改为就诊中
    void changeStatus(Integer number);

    //查询患者信息
    Cases getCases(Integer number);

    //添加病例
    void insertCases(Cases cases);

    //查询患者病例是否存在
    String checkCases(Integer number);

    //在挂号处添加医嘱
    void insertOrder(Cases cases);

    //就诊完成,将状态改为已完成
    void statusComplied(Integer number);

    //获取所有医生的level
    List<DoctorFeeDTO> getAllDoctorsWithVisitCount();

    //添加医生挂号费
    void updateDoctorRegistrationFee(UpdateDoctorFeeDTO updateDoctorFeeDTO);
}
