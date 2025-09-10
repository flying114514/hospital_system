package com.hui.task;

import com.hui.constant.RegisteredStatusConstant;
import com.hui.dto.DeleteTimeDTO;
import com.hui.mapper.DocMainMapper;
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

    //处理超时挂号单
    //每分钟处理一次
    @Scheduled(cron="0 * * * * ? ")
    public void processTimeoutOrder(){
        log.info("定时处理超时订单{}",LocalDateTime.now());
        //查询超时订单
        List<OverTimeVO> ordersList = registerMapper.getOverTimeOrders(RegisteredStatusConstant.UN_PAID, LocalDateTime.now().plusMinutes(-15));
        if(ordersList!=null && ordersList.size()>0){
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
   @Scheduled(cron="0 * * * * ? ")
   public void processPatientNotArrived(){
       log.info("处理患者失约{}",LocalDateTime.now());
       // 查询叫号时间超过15分钟且患者未到达的订单
       // status应为"叫号中"状态，当前时间超过callTime
       List<OverTimeVO> ordersList = registerMapper.getCalledButNotArrivedOrders(
               RegisteredStatusConstant.CALLING,
               LocalDateTime.now()
       );

       if(ordersList != null && !ordersList.isEmpty()) {
           for (OverTimeVO order : ordersList) {
               // 更新状态为"失约"
               order.setStatus(RegisteredStatusConstant.FAIL);
               registerMapper.updateFail(order);
           }
           log.info("处理了 {} 个失约患者", ordersList.size());
       }

   }



}
