
package com.zzz.o2o.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Aspect
@Component
public class LogAspect {
// 定义切点
private static Logger logger = LoggerFactory.getLogger(LogAspect.class);
    @Pointcut(value = "@annotation(LogShowParams)")
    private void pointcut() {
    }

// 定义环绕通知


@Around(value = "pointcut()&&@annotation(logShowParams)")
public Object around(ProceedingJoinPoint point, LogShowParams logShowParams) {
        // 请求的URL
        String requestUrl = logShowParams.requestUrl();
         //拦截的类名
        Class clazz = point.getTarget().getClass();
        //拦截的方法
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        // 输出参数
        logger.info("执行了 类:" + clazz + " 方法:" + method + " 自定义请求地址:" + requestUrl);
        //System.out.println("执行了 类:" + clazz + " 方法:" + method + " 自定义请求地址:" + requestUrl);
        Object[] args = point.getArgs();
        //System.out.println( "参数：");
        logger.info("参数：");
        for(Object a: args){
            logger.info(a.toString());
           // System.out.print(a);
        }
        try {
            return point.proceed(); //执行程序
        } catch (Throwable throwable) {
        throwable.printStackTrace();
        return throwable.getMessage();
        }
    }



    //  返回之后的通知
    @AfterReturning(value = "pointcut() && @annotation(logShowParams)", returning = "result")
    public Object afterReturning(JoinPoint joinPoint, LogShowParams logShowParams, Object result) {
        //System.out.println("执行结果：" + result);
        logger.info("执行结果：" + result);
        return result;
}

// 抛出异常后的通知

 /*   @AfterThrowing(value = "pointcut() && @annotation(logShowParams)", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, LogShowParams logShowParams, Exception ex) {
        System.out.println("请求：" + logShowParams.requestUrl() + " 出现异常" + ex.getMessage());
    }*/
}


