package com.hui.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hui.dto.DoctorPageQueryDTO;
import com.hui.dto.RegistrationDTO;
import com.hui.entity.*;
import com.hui.mapper.CreateMapper;
import com.hui.mapper.RegisterMapper;
import com.hui.result.PageResult;
import com.hui.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterServiceImpl extends ServiceImpl<RegisterMapper, Registration> implements RegisterService {

    @Autowired
    private RegisterMapper registerMapper;
    @Autowired
    private CreateMapper createMapper;


    //患者挂号
    @Override
    public List<DepartmentList> register(RegistrationDTO registrationDTO) {
        String patientName = registrationDTO.getName();
        String idCard = registrationDTO.getIdCard();
        String Card = registrationDTO.getCard();
        TemplateInfo templateInfo = new TemplateInfo().builder()
                .patientName(patientName)
                .idCard(idCard)
                .Card(Card)
                .build();
        registerMapper.update(templateInfo);
        List<DepartmentList> departmentLists = registerMapper.getDepartments();
        return departmentLists;//返回科室名列表
    }

    //根据科室名称模糊分页查询相关信息
    @Override
    public PageResult pageDoctor(DoctorPageQueryDTO doctorPageQueryDTO) {
        int pageSize = doctorPageQueryDTO.getPageSize();
        int pageNum = doctorPageQueryDTO.getPage();
        PageHelper.startPage(pageNum, pageSize);
        Page<DoctorDetails> page = registerMapper.pageDoctor(doctorPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //根据医生姓名获取详细信息
    @Override
    public PageTime selectTime(TimePageQueryDTO timePageQueryDTO) {
        String name = timePageQueryDTO.getName();

        PageTime pageTime =registerMapper.getDetailByName(name);
        //TODO余浩相关,简单点
        pageTime.setPageResult(new PageResult());


        return null;
    }


}
