package com.hui.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hui.constant.MessageConstant;
import com.hui.constant.RegisteredStatusConstant;
import com.hui.context.BaseContext;
import com.hui.dto.*;
import com.hui.entity.*;
import com.hui.exception.AccountNotFoundException;
import com.hui.exception.PasswordErrorException;
import com.hui.mapper.DocMainMapper;
import com.hui.result.PageResult;
import com.hui.service.DocMainService;
import com.hui.service.ManagerService;
import com.hui.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DocMainServiceImpl implements DocMainService {

    @Autowired
    private DocMainMapper docMainMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DocMainService docMainService;

    @Autowired
    private ManagerServiceImpl managerServiceImpl;

    //医生登录功能
    @Override
    @Transactional
    public DocLoginResult login(DocLoginDTO docLoginDTO) {
        String password = docLoginDTO.getPassword();
        DocLoginResult docLoginResult = new DocLoginResult();
        //1、根据身份证查询数据库中的数据
        DocLoginVO docLoginVO = docMainMapper.getByIdCard(docLoginDTO.getIdCard());

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
        docLoginResult.setDetail(docLoginVO.getName() + "登录成功");

        //3、返回实体对象
        return docLoginResult;
    }

    //医生注册功能
    @Override
    @Transactional
    public DocRegisterVO register(DocRegisterDTO docRegisterDTO) {
        //尝试匹配医生idCard和name,表中是否存在医生
        DocBasic docBasic = docMainMapper.checkDoc(docRegisterDTO);

        DocRegisterVO docRegisterVO = new DocRegisterVO();
        if (docBasic != null) {
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

        docRegisterVO.setDetail(docRegisterDTO.getName() + "您已注册成功");
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
        SetBanVO setBanVO = new SetBanVO();

        LocalDateTime beginTime = banInfoDTO.getBeginTime();
        LocalDateTime endTime = banInfoDTO.getEndTime();
        if (beginTime.isAfter(endTime)) {
            setBanVO.setDetail("开始时间晚于结束时间");
            return setBanVO;
        }
        if (endTime.isBefore(LocalDateTime.now())) {
            setBanVO.setDetail("时间不能晚于当前时间");
            return setBanVO;
        }

        //先查询级别
        String level = docMainMapper.getLevel(banInfoDTO.getDoctorId());

        //根据级别设置预计就诊时间
        switch (level) {
            case "普通":
                banInfoDTO.setPredictTime(beginTime.plusMinutes(15));
                banInfoDTO.setRemain(20);
                break;
            case "专家":
                banInfoDTO.setPredictTime(beginTime.plusMinutes(20));
                banInfoDTO.setRemain(10);
                break;
            case "急诊":
                banInfoDTO.setPredictTime(beginTime.plusMinutes(10));
                banInfoDTO.setRemain(30);
                break;
        }

        //查询姓名
        String name = docMainMapper.getNameById(banInfoDTO.getDoctorId());
        banInfoDTO.setName(name);

        //向排班表中添加数据
        docMainMapper.setInfo(banInfoDTO);

        Integer remain = banInfoDTO.getRemain();

        //主键返回workId后给banInfo

        //添加remain个号
        List<BanInfoDTO> banInfoList = new ArrayList<>();
        for (int i = 0; i < remain; i++) {
            // 创建每个号的详细信息
            BanInfoDTO info = new BanInfoDTO();
            // 复制 banInfoDTO 的属性到 info
            BeanUtils.copyProperties(banInfoDTO, info);
            //将多次的SQL语言变成单次
            banInfoList.add(info);
        }

        docMainMapper.batchSetBanInfo(banInfoList);

        setBanVO.setDetail("设置排班成功");
        // 排班信息变更后，清除缓存
        managerServiceImpl.clearScheduleCache();
        log.info("添加排班信息成功，已清除相关缓存");
        return setBanVO;
    }


    //分页查询排班信息
    @Override
    @Transactional
    public PageResult getBanInfo(TimeInfoDTO timeInfoDTO) {
        Integer pageNum = timeInfoDTO.getPage();
        Integer pageSize = timeInfoDTO.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        Page<BanInfoVO> page = docMainMapper.getBanInfo(timeInfoDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    //定时删除排班信息
    @Override
    @Transactional
    public void deleteSchedulesBefore(DeleteTimeDTO deleteTimeDTO) {
        deleteTimeDTO.setDoctorId(Math.toIntExact(BaseContext.getCurrentId()));
        docMainMapper.deleteSchedulesBefore(deleteTimeDTO);
    }

    //医生叫号功能
    @Override
    @Transactional
    public CallNumberVO callNumber(CallNumberDTO callNumberDTO) {
        //根据待叫号的status和number查患者
        CallName callName = docMainMapper.getPatientInfo(callNumberDTO);

        if (callName == null) {
            return new CallNumberVO("没有患者可以叫号");
        }

        //将状态设置为叫号中
        CallDTO callDTO = CallDTO.builder()
                .id(callName.getId())
                .callTime(LocalDateTime.now().plusMinutes(15))
                .status(RegisteredStatusConstant.CALLING).build();

        //根据orderId设置状态为叫号中,将截至时间设置为15分钟后
        docMainMapper.setStatus(callDTO);

        return new CallNumberVO("患者" + callName.getPatientName() + "已被叫号");
    }

    //医生接诊功能
    @Override
    @Transactional
    public CasesVO patientArrived(CasesDTO casesDTO) {
        Integer number = casesDTO.getNumber();

        if (docMainMapper.checkCases(number) == null) {
            return new CasesVO("该患者未叫号");
        }
        //将当前患者状态改为就诊中
        docMainMapper.changeStatus(casesDTO.getNumber());

        Cases cases = docMainMapper.getCases(casesDTO.getNumber());
        cases.setCases(casesDTO.getCases());
        cases.setCreateTime(LocalDateTime.now());
        cases.setNumber(number);

        docMainMapper.insertCases(cases);

        docMainMapper.insertOrder(cases);

        //就诊结束,将状态改为已完成
        docMainMapper.statusComplied(casesDTO.getNumber());

        return new CasesVO("接诊成功");
    }

}
