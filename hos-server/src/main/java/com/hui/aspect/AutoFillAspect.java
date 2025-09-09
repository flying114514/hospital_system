package com.hui.aspect;



import com.hui.annotation.AutoFill;
import com.hui.constant.AutoFillConstant;
import com.hui.context.BaseContext;
import com.hui.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

//自定义切面类,实现公共字段自动填充处理逻辑
@Aspect//切面
@Component//标识切面类,将该类对象加入到spring容器中
@Slf4j//日志对象
public class AutoFillAspect {

    //指定切入点,在com.hui.mapper包下的所有类的所有方法上,如果方法上有@AutoFill注解,则执行自动填充
    @Pointcut("execution(* com.hui.mapper.*.*(..)) && @annotation(com.hui.annotation.AutoFill)")
    public void autoFillPointcut(){}

    //为公共字段赋值
    @Before("autoFillPointcut()")//前置通知,方法在匹配切入点表达式后执行以下操作
    public void autoFill(JoinPoint joinPoint){//joinpoint:连接点,即目标方法
        log.info("自动填充");
        //获取当前被拦截的方法,如果时INSERT,修改两个,如果是UPDATE,修改四个
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//通过切入点获取签名,强转成方法签名
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取方法上的注解对象
        OperationType operationType = autoFill.value();//获取注解中的值,即操作类型,也就是INSERT或者UPDATE

        //获取实体employee
        Object[] args = joinPoint.getArgs();//获取方法中的所有参数,约定employee实体类作为第一个参数
        if(args==null || args.length==0){
            return;
        }

        Object entity = args[0];//Obejct类型接收,因为实体类型不固定

        // 准备赋值数据,id通过定义的BaseContext工具类调用ThreadLocal获取
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();


        //根据不同的操作类型进行赋值,通过对应的属性反射赋值
        if(operationType==OperationType.INSERT){//新增操作,4个字段
            try {//通过反射映射实体类的方法,获取实体类的set方法
                //通过实体类,getDeclaredMethod方法,第一个参数是要获取的方法名,第二个方法是该方法的参数类型
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);

                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_Appointment_TIME, LocalDateTime.class);

                //通过反射为对象赋值,invoke方法,第一个参数是对象,第二个参数是方法的参数
                setCreateTime.invoke(entity,now);
                setUpdateTime.invoke(entity,now);


            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }else if(operationType==OperationType.UPDATE){//更新操作,2个字段
            try {
                //Method setUpdateTime = entity.getClass().getDeclaredMethod( LocalDateTime.class);





            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}
