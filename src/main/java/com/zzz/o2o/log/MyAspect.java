package com.zzz.o2o.log;/*
package com.zzz.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component()
@Aspect
public class MyAspect {
    // 多个方法需要使用这个切入点表达式，定义为一个公用的
    @Pointcut("execution(* com.zzz.web.shopadmin.TestController.*(..))")
    public void myPointCut() {

    }



    // 这里注解里面的值为上面的方法名
    @Before("myPointCut()")
    public void myBefore(JoinPoint joinPoint) {
        System.out.println("前置通知 ： " + joinPoint.getSignature().getName());
    }




    //
    @Around("myPointCut()")
    public Object myAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("环绕通知执行方法前");
        // 手动执行目标方法
        Object obj = joinPoint.proceed();

        System.out.println("环绕通知执行方法后");
        return obj;
    }



    @AfterThrowing(value = "myPointCut()", throwing = "e")
    public void myAfterThrowing(JoinPoint joinPoint, Throwable e) {
        System.out.println("抛出异常通知 ： " + e.getMessage());
    }



    @After("myPointCut()")
    public void myAfter(JoinPoint joinPoint) {
        System.out.println("最终通知");
    }

}
*/
