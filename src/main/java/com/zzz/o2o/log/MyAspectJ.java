package com.zzz.o2o.log;/*package com.zzz.log;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MyAspectJ {

    @Before(value="execution(* com.zzz.web..*(..))")
    public void myBefore(JoinPoint joinPoint) {
        System.out.println("前置增强。。。"+joinPoint);

    }
    @AfterReturning(value="execution(* com.zzz.web..*(..))",returning="reval")
    public void myAfterReturning(JoinPoint joinPoint,Object reval) {
        System.out.println("后置增强！！！,返回值"+reval);

    }
    @Around(value="execution(* com.zzz.web..*(..))")
    public void myAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("1.环绕增强前");
        Object res = joinPoint.proceed();
        System.out.println("2.环绕增强后"+res);
    }
}
*/