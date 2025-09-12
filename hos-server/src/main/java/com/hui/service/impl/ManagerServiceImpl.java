package com.hui.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hui.constant.MessageConstant;
import com.hui.dto.AllTimeDTO;
import com.hui.dto.ManagerLoginDTO;
import com.hui.entity.DoctorVisitStats;
import com.hui.exception.AccountNotFoundException;
import com.hui.exception.PasswordErrorException;
import com.hui.mapper.ManagerMapper;
import com.hui.result.PageResult;
import com.hui.service.ManagerService;
import com.hui.vo.AllTimeVO;
import com.hui.vo.ManagerLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ManagerServiceImpl implements ManagerService {
    @Autowired
    private ManagerMapper managerMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //获取所有排版信息
    @Override
    @Transactional
    public PageResult getAllTime(AllTimeDTO allTimeDTO) {
        Integer pageNum = allTimeDTO.getPage();
        Integer pageSize = allTimeDTO.getPageSize();
        PageHelper.startPage(pageNum,pageSize);
        PageHelper.orderBy("begin_time");
        allTimeDTO.setTime(LocalDateTime.now().minusDays(7));

        Page<AllTimeVO> page=managerMapper.getAllTime(allTimeDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //管理员登录
    @Override
    @Transactional
    public ManagerLoginVO login(ManagerLoginDTO managerLoginDTO) {
        String password = managerLoginDTO.getPassword();
        //查询管理员是否存在
        ManagerLoginVO managerLoginVO=managerMapper.login(managerLoginDTO);
        if(managerLoginVO==null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        //密码比对
        //对前端传过来的密码进行加密处理
        boolean result = passwordEncoder.matches(password, managerLoginVO.getPassword());
        if (!result) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        return managerLoginVO;
    }

    //实时获取top5
    @Override
    @Transactional
    public List<DoctorVisitStats> getTopDoctorsByVisits(int number) {
        return managerMapper.getTopDoctorsByVisits(number);
    }

    //指定时间内模糊查询医生排版信息
    @Override
    @Transactional
    public List<AllTimeVO> getAllTimeList(AllTimeDTO allTimeDTO) {
        return managerMapper.getAllTimeList(allTimeDTO);
    }


}
