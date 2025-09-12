package com.hui.task;

import com.hui.constant.RegisteredStatusConstant;
import com.hui.dto.DeleteTimeDTO;
import com.hui.dto.DoctorFeeDTO;
import com.hui.dto.UpdateDoctorFeeDTO;
import com.hui.mapper.DocMainMapper;
import com.hui.mapper.PatientMainMapper;
import com.hui.mapper.RegisterMapper;
import com.hui.service.DocMainService;
import com.hui.vo.OverTimeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

//定时任务类
@Component//识别并放在容器管理
@Slf4j
public class OrderTask {

    @Autowired
    private RegisterMapper registerMapper;

    @Autowired
    private DocMainMapper docMainMapper;

    @Autowired
    private DocMainService docMainService;

    @Autowired
    private PatientMainMapper patientMainMapper;

    //处理超时挂号单
    //每分钟处理一次
    @Scheduled(cron = "0 * * * * ? ")
    public void processTimeoutOrder() {
        log.info("定时处理超时订单{}", LocalDateTime.now());
        //查询超时订单
        List<OverTimeVO> ordersList = registerMapper.getOverTimeOrders(RegisteredStatusConstant.UN_PAID, LocalDateTime.now().plusMinutes(-15));
        if (ordersList != null && ordersList.size() > 0) {
            for (OverTimeVO orders : ordersList) {
                orders.setStatus(RegisteredStatusConstant.OVER_TIME);
                registerMapper.updateOverTime(orders);
            }
        }
    }

    // 定时清理过期排班数据
    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨2点执行,清理work_time里的信息
    public void cleanupExpiredSchedules() {
        LocalDateTime expiredDate = LocalDateTime.now().minusDays(7);
        DeleteTimeDTO deleteTimeDTO = DeleteTimeDTO.builder()
                .expiredDate(expiredDate)
                .build();
        docMainService.deleteSchedulesBefore(deleteTimeDTO);
    }

    //TODO患者失约 两种情况,1.患者挂号没来(超过endTime,status为1)(应该不行,都挂号了,不叫不好)  2.医生叫号后15分钟内没来

    //每分钟查询有没有叫号后15分钟没有来的
    @Scheduled(cron = "0 * * * * ? ")
    public void processPatientNotArrived() {
        log.info("处理患者失约{}", LocalDateTime.now());
        // 查询叫号时间超过15分钟且患者未到达的订单
        // status应为"叫号中"状态，当前时间超过callTime
        List<OverTimeVO> ordersList = registerMapper.getCalledButNotArrivedOrders(
                RegisteredStatusConstant.CALLING,
                LocalDateTime.now()
        );

        if (ordersList != null && !ordersList.isEmpty()) {
            for (OverTimeVO order : ordersList) {
                // 更新状态为"失约"
                order.setStatus(RegisteredStatusConstant.FAIL);
                registerMapper.updateFail(order);
            }
            log.info("处理了 {} 个失约患者", ordersList.size());
        }
    }


    //每天凌晨,根据医生的级别和累计完成就诊数和医院星级为医生分配挂号费
    //@Scheduled(cron = "0 0 0 * * ?") // 每天凌晨0点执行

    //为方便演示,每五分钟更新价格
    @Scheduled(cron = "0 */5 * * * ? ")
    public void assignRegistrationFees() {
        log.info("开始为医生分配挂号费: {}", LocalDateTime.now());

        try {
            // 1. 获取医院当前星级评分
            Double avgStar = patientMainMapper.getAvgStar();

            // 2. 获取所有医生的基本信息和累计就诊数
            List<DoctorFeeDTO> doctors = docMainMapper.getAllDoctorsWithVisitCount();

            // 3. 为每个医生计算并分配挂号费
            for (DoctorFeeDTO doctor : doctors) {
                // 计算基础挂号费
                double baseFee = calculateBaseFee(doctor.getLevel());

                // 根据累计就诊数增加费用
                double visitBonus = calculateVisitBonus(doctor.getNumber(), doctor.getLevel());

                // 根据医院星级增加费用
                double hospitalStarBonus = calculateHospitalStarBonus(avgStar);

                // 计算最终挂号费
                double finalFee = baseFee + visitBonus + hospitalStarBonus;

                // 更新医生的挂号费
                UpdateDoctorFeeDTO updateDoctorFeeDTO = UpdateDoctorFeeDTO.builder()
                        .doctorId(doctor.getDoctorId())
                        .registrationFee(finalFee)
                        .build();

                docMainMapper.updateDoctorRegistrationFee(updateDoctorFeeDTO);

                log.info("医生ID: {}, 姓名: {}, 基础费用: {}, 就诊奖励: {}, 医院星级奖励: {}, 最终费用: {}",
                        doctor.getDoctorId(), doctor.getName(), baseFee, visitBonus, hospitalStarBonus, finalFee);
            }

            log.info("医生挂号费分配完成，共处理 {} 个医生", doctors.size());
        } catch (Exception e) {
            log.error("医生挂号费分配失败: ", e);
        }
    }

    private double calculateHospitalStarBonus(Double avgStar) {
        if (avgStar != null) {
            if (avgStar >= 4.5) {
                return 5;
            } else if (avgStar >= 4.0) {
                return 4;
            } else if (avgStar >= 3.5) {
                return 3;
            } else if (avgStar >= 3.0) {
                return 2;
            } else if (avgStar >= 2.5) {
                return 1;
            }
        }
        return 0;
    }

    private double calculateVisitBonus(Integer number, String level) {
        switch (level) {
            case "普通":
                return (double) number / 10;
            case "专家":
                return (double) number / 20;
            case "急诊":
                return (double) number / 30;
        }
        return 0;
    }

    private double calculateBaseFee(String level) {
        switch (level) {
            case "普通":
                return 10;
            case "专家":
                return 20;
            case "急诊":
                return 30;
        }
        return 0;
    }


}
