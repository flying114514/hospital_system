package com.hui.task;
//TODO 定时任务类
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
////定时任务类
//@Component//识别并放在容器管理
//@Slf4j
public class OrderTask {
//
//    @Autowired
//    private OrderMapper orderMapper;
//
//    //处理超时订单
//    //每分钟处理一次
//    @Scheduled(cron="0 * * * * ? ")
//    public void processTimeoutOrder(){
//        log.info("定时处理超时订单{}",LocalDateTime.now());
//        //查询超时订单
//        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));
//        if(ordersList!=null && ordersList.size()>0){
//            for (Orders orders : ordersList) {
//                orders.setStatus(Orders.CANCELLED);
//                orders.setCancelReason("支付超时，取消订单");
//                orders.setCancelTime(LocalDateTime.now());
//                orderMapper.update(orders);
//            }
//        }
//    }
//
//    //每天凌晨一点处理用户未点确认收货,显示还在派送中的订单
//    @Scheduled(cron = "0 0 1 * * ? ")
//    public void processDeliveryOrder(){
//        log.info("处理用户未点确认收货,显示还在派送中的订单:{},",LocalDateTime.now());
//        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusMinutes(-60));
//
//        if(ordersList!=null && ordersList.size()>0){
//            for (Orders orders : ordersList) {
//                orders.setStatus(Orders.COMPLETED);
//                orders.setCancelReason("支付超时，取消订单");
//                orders.setCancelTime(LocalDateTime.now());
//                orderMapper.update(orders);
//            }
//        }
//
//    }
}
