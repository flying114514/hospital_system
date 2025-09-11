package com.hui.controller.manager;

import com.hui.dto.AllTimeDTO;
import com.hui.dto.ManagerLoginDTO;
import com.hui.result.PageResult;
import com.hui.result.Result;
import com.hui.service.ManagerService;
import com.hui.vo.ManagerLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")
@Slf4j
public class ManagerController {

    @Autowired
    public ManagerService managerService;

    //管理员登录
    @PostMapping("/login")
    public Result<ManagerLoginVO> login(@RequestBody ManagerLoginDTO managerLoginDTO){
        log.info("管理员登录:{}",managerLoginDTO);
        String name = managerLoginDTO.getName();

        ManagerLoginVO managerLoginVO=managerService.login(managerLoginDTO);
        return Result.success(managerLoginVO);
    }

    //根据时间,姓名模糊分页得到所有排班信息
    @GetMapping("/timeinfo")
    public Result<PageResult> getAllTime(@RequestBody AllTimeDTO allTimeDTO){
        PageResult pageResult=managerService.getAllTime(allTimeDTO);
        return Result.success(pageResult);
    }




}
