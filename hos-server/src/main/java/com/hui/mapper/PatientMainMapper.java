package com.hui.mapper;

import com.hui.vo.LoginVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PatientMainMapper {

    //检查密码是否正确
    LoginVO getByName(String name);

    //检查银行余额
    Double getMoney(String paymentMethod, Long currentId);

    //更新医保卡余额
    void updateMoney(Double money, Long currentId);

    //银行账户减少余额
    void minusMoney(Double money, String paymentMethod,Long currentId);

}
