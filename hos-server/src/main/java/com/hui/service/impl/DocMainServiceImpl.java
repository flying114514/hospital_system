package com.hui.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hui.constant.MessageConstant;
import com.hui.context.BaseContext;
import com.hui.dto.*;
import com.hui.entity.DetailInfo;
import com.hui.entity.DocBasic;
import com.hui.entity.DocLoginResult;
import com.hui.entity.PayHistory;
import com.hui.exception.AccountNotFoundException;
import com.hui.exception.PasswordErrorException;
import com.hui.mapper.DocMainMapper;
import com.hui.result.PageResult;
import com.hui.service.DocMainService;
import com.hui.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class DocMainServiceImpl implements DocMainService {

    @Autowired
    private DocMainMapper docMainMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //医生登录功能
    @Override
    @Transactional
    public DocLoginResult login(DocLoginDTO docLoginDTO) {
        String password = docLoginDTO.getPassword();
        DocLoginResult docLoginResult=new DocLoginResult();
        //1、根据身份证查询数据库中的数据
        DocLoginVO docLoginVO =docMainMapper.getByIdCard(docLoginDTO.getIdCard());

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (docLoginVO == null) {
            //账号不存在
            docLoginResult.setDetail("账号不存在");
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //对前端传过来的密码进行解密处理
        boolean result = passwordEncoder.matches(password, docLoginVO.getPassword());
        if (!result) {
            //密码错误
            docLoginResult.setDetail("密码错误");
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        docLoginResult.setId(docLoginVO.getId());
        docLoginResult.setDetail(docLoginVO.getName()+"登录成功");

        //3、返回实体对象
        return docLoginResult;
    }

    //医生注册功能
    @Override
    @Transactional
    public DocRegisterVO register(DocRegisterDTO docRegisterDTO) {
        //尝试匹配医生idCard和name,表中是否存在医生
        DocBasic docBasic=docMainMapper.checkDoc(docRegisterDTO);

        DocRegisterVO docRegisterVO=new DocRegisterVO();
        if(docBasic!=null){
            //信息已存在,不可注册,忘记密码
            docRegisterVO.setDetail("您已注册过了");
            return docRegisterVO;
        }
        //可以注册,加密密码
        String encodedPassword = passwordEncoder.encode(docRegisterDTO.getPassword());
        docRegisterDTO.setPassword(encodedPassword);

        //向basic_doctor里添加基本信息
        docMainMapper.register(docRegisterDTO);

        Integer doctorId = docRegisterDTO.getDoctorId();
        docRegisterVO.setDoctorId(doctorId);

        //向doctor_detail里添加基本信息
        docMainMapper.addBasicInfo(docRegisterDTO);

        Integer id = docRegisterDTO.getId();
        docRegisterVO.setId(id);

        docRegisterVO.setDetail(docRegisterDTO.getName()+"您已注册成功");
        return docRegisterVO;
    }

    //医生忘记密码
    @Override
    @Transactional
    public void updatePassword(UpdatePasswordDTO updatePasswordDTO) {

        updatePasswordDTO.setPassword(passwordEncoder.encode(updatePasswordDTO.getPassword()));

        docMainMapper.updatePassword(updatePasswordDTO);
    }

    //设置医生详细信息
    @Override
    @Transactional
    public SetInfoVO setDetailInfo(DetailInfo detailInfo) {
        docMainMapper.setDetailInfo(detailInfo);
        SetInfoVO setInfoVO = new SetInfoVO();
        setInfoVO.setDetail("设置信息成功");
        return setInfoVO;
    }

    //医生设置排班信息
    @Override
    @Transactional
    public SetBanVO setBanInfo(BanInfoDTO banInfoDTO) {

        //先查询级别
        String level = docMainMapper.getLevel(banInfoDTO.getDoctorId());

        //根据级别设置预计就诊时间
        switch (level){
            case "普通":
                banInfoDTO.setPredictTime(banInfoDTO.getBeginTime().plusMinutes(15));
                break;
            case "专家":
                banInfoDTO.setPredictTime(banInfoDTO.getBeginTime().plusMinutes(20));
                break;
            case "急诊":
                banInfoDTO.setPredictTime(banInfoDTO.getBeginTime().plusMinutes(10));
                break;
        }

        //查询姓名
        String name=docMainMapper.getNameById(banInfoDTO.getDoctorId());
        banInfoDTO.setName(name);
        docMainMapper.setBanInfo(banInfoDTO);
        SetBanVO setBanVO = new SetBanVO();
        setBanVO.setDetail("设置排班成功");
        return setBanVO;
    }

    //根据级别设置价格
    @Override
    public void setPriceByLevel(String level) {
        Double price = 0.0;
        switch (level){
            case "普通":
                price=10.0;
                break;
            case "专家":
                price=20.0;
                break;
            case "急诊":
                price=30.0;
                break;
        }
        PriceDTO priceDTO = PriceDTO.builder()
                .price(price)
                .doctorId(Math.toIntExact(BaseContext.getCurrentId()))
                .build();
        docMainMapper.setPriceByLevel(priceDTO);
    }

    //分页查询排班信息
    @Override
    public PageResult getBanInfo(TimeInfoDTO timeInfoDTO) {
        Integer pageNum = timeInfoDTO.getPage();
        Integer pageSize = timeInfoDTO.getPageSize();
        PageHelper.startPage(pageNum,pageSize);
        Page<BanInfoVO> page=docMainMapper.getBanInfo(timeInfoDTO);

        return new PageResult(page.getTotal(),page.getResult());
    }

}
