package com.hui.service;

import com.hui.dto.*;
import com.hui.entity.DetailInfo;
import com.hui.entity.DocLoginResult;
import com.hui.result.PageResult;
import com.hui.vo.DocRegisterVO;
import com.hui.vo.SetBanVO;
import com.hui.vo.SetInfoVO;

public interface DocMainService {
    //医生登录功能
    DocLoginResult login(DocLoginDTO docLoginDTO);

    //医生注册功能
    DocRegisterVO register(DocRegisterDTO docRegisterDTO);

    //医生忘记密码,重新设置密码
    void updatePassword(UpdatePasswordDTO updatePasswordDTO);

    //医生设置详细信息
    SetInfoVO setDetailInfo(DetailInfo detailInfo);

    //医生设置排班信息
    SetBanVO setBanInfo(BanInfoDTO banInfoDTO);

    //根据级别设置价格
    void setPriceByLevel(String level);

    //分页查询排班信息
    PageResult getBanInfo(TimeInfoDTO timeInfoDTO);
}
