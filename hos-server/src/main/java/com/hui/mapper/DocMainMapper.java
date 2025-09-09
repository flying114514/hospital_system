package com.hui.mapper;

import com.github.pagehelper.Page;
import com.hui.dto.*;
import com.hui.entity.DetailInfo;
import com.hui.entity.DocBasic;
import com.hui.result.PageResult;
import com.hui.vo.BanInfoVO;
import com.hui.vo.DocLoginVO;
import com.hui.vo.SetInfoVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    //医生设置排班信息
    void setBanInfo(BanInfoDTO banInfoDTO);

    //根据级别设置价格
    void setPriceByLevel(PriceDTO priceDTO);

    //根据id查询级别
    @Select("select level from doctor_details where doctor_id=#{doctorId}")
    String getLevel(Integer doctorId);

    //根据id查询姓名
    @Select("select name from basic_doctor where id=#{doctorId}")
    String getNameById(Integer doctorId);

    //分页查询排班信息
    Page<BanInfoVO> getBanInfo(TimeInfoDTO timeInfoDTO);
}
