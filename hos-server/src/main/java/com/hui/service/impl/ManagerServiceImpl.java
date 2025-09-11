package com.hui.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hui.dto.AllTimeDTO;
import com.hui.dto.ManagerLoginDTO;
import com.hui.mapper.ManagerMapper;
import com.hui.result.PageResult;
import com.hui.service.ManagerService;
import com.hui.vo.AllTimeVO;
import com.hui.vo.ManagerLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ManagerServiceImpl implements ManagerService {
    @Autowired
    private ManagerMapper managerMapper;

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
        //查询管理员是否存在
        ManagerLoginVO managerLoginVO=managerMapper.login(managerLoginDTO);
        if(managerLoginVO==null){
            return null;
        }
        return managerLoginVO;
    }
}
