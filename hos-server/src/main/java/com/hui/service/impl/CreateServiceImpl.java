package com.hui.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hui.constant.RegisteredStatusConstant;
import com.hui.dto.BankDTO;
import com.hui.dto.PatientBasicInfoDTO;
import com.hui.entity.PatientBasicInfo;
import com.hui.mapper.CreateMapper;
import com.hui.service.CreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class CreateServiceImpl extends ServiceImpl<CreateMapper, PatientBasicInfo> implements CreateService {

    @Autowired
    private CreateMapper createMapper;



    /**
     * 根据患者身份证补充信息,并新增患者
     * */
    @Override
    @Transactional
    public PatientBasicInfo insertPatientInfo(PatientBasicInfoDTO patientBasicInfoDTO) {

        String idCard = patientBasicInfoDTO.getIdCard();

        //根据身份证号获取性别
        String gender = getGenderFromIdCard(idCard);

        // 根据身份证号计算年龄
        Integer age = calculateAgeFromIdCard(idCard);


        PatientBasicInfo patientBasicInfo = PatientBasicInfo.builder()
                .phone(patientBasicInfoDTO.getPhone())
                .idCard(patientBasicInfoDTO.getIdCard())
                .name(patientBasicInfoDTO.getName())
                .gender(gender)
                .age(age)
                .status(RegisteredStatusConstant.UN_REGISTERED)//默认状态未预约
                .id(createMapper.getPatientIdByIdCard(idCard))
                .build();

        //判断患者是否已建档,向orders插入数据,判断basic_patient中是否能查到
        if (createMapper.selectInfo(idCard)==null) {
            this.save(patientBasicInfo);
            //建完档新建银行账户
            BankDTO bankDTO = BankDTO.builder()
                    .name(patientBasicInfo.getName())
                    .patientId(String.valueOf(createMapper.getPatientIdByIdCard(idCard)))
                    .cash(10000.0)
                    .wechatPay(10000.0).build();
            createMapper.insertBank(bankDTO);
        }
        createMapper.insertInfo(patientBasicInfo);


        return patientBasicInfo;
    }



    /**
     * 根据身份证号计算年龄
     *
     * @param idCard 身份证号
     * @return 年龄
     */
    @Transactional
    public Integer calculateAgeFromIdCard(String idCard) {
        try {
            String birthDateStr=idCard.substring(6, 14);
            // 解析出生日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date birthDate = sdf.parse(birthDateStr);

            // 计算年龄
            Calendar birthCalendar = Calendar.getInstance();
            birthCalendar.setTime(birthDate);

            Calendar currentCalendar = Calendar.getInstance();

            int age = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

            // 如果今年的生日还没到，年龄减1
            if (currentCalendar.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            return age;
        } catch (Exception e) {
            // 解析失败返回null
            return null;
        }
    }private String getGenderFromIdCard(String idCard){
        // 通过身份证号倒数第二位数字判断性别
        String gender = "";
        // 获取倒数第二位字符
        char secondLastChar = idCard.charAt(idCard.length() - 2);
        // 判断是否为数字
        if (Character.isDigit(secondLastChar)) {
            int secondLastDigit = Character.getNumericValue(secondLastChar);
            // 奇数为男，偶数为女
            gender = (secondLastDigit % 2 == 1) ? "男" : "女";
        }
        return gender;

    }
}
