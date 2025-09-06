package com.hui.controller.total;

import com.hui.constant.BasicStatusConstant;
import com.hui.constant.MessageConstant;
import com.hui.result.Result;
import com.hui.service.MainService;
import com.hui.vo.RoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
@Slf4j
public class MainController {

    @Autowired
    private MainService mainService;

    //主界面,获取人物角色,跳转到不同界面
    @GetMapping
    public Result<RoleVO> getRole(String role){
        RoleVO roleVO=mainService.getRole(role);
        if (roleVO==null){
            log.info("获取角色失败");
            return Result.error(MessageConstant.GET_ROLE_FAIL);
        }
        log.info("获取角色成功");
        return Result.success(roleVO);
    }
}
