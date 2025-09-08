package com.hui.task;

import com.hui.constant.RegisteredStatusConstant;
import com.hui.mapper.RegisterMapper;
import com.hui.vo.OverTimeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Ref;
import java.time.LocalDateTime;
import java.util.List;

//定时任务类
@Component//识别并放在容器管理
@Slf4j
public class OrderTask {

    @Autowired
    private RegisterMapper registerMapper;

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

    //每天凌晨一点处理用户未点确认收货,显示还在派送中的订单
    //@Scheduled(cron = "0 0 1 * * ? ")
    //public void processDeliveryOrder(){
    //    log.info("处理用户未点确认收货,显示还在派送中的订单:{},",LocalDateTime.now());
    //    List<Orders> ordersList = registerMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusMinutes(-60));
//
    //    if(ordersList!=null && ordersList.size()>0){
    //        for (Orders orders : ordersList) {
    //            orders.setStatus(Orders.COMPLETED);
    //            orders.setCancelReason("支付超时，取消订单");
    //            orders.setCancelTime(LocalDateTime.now());
    //            registerMapper.update(orders);
    //        }
    //    }
//
    //}
}
