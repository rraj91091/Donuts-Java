package com.project.donuts.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Before("execution(* com.project.donuts.*.*.*(..)) " +
            "&& !execution(* com.project.donuts.beanConfig.*.*(..))")
    public void logMethodCallBeforeExecution(JoinPoint joinPoint) {
        logger.info("{} is called with arguments: {}", joinPoint, joinPoint.getArgs());
    }

    // executes regardless of whether method completes successfully or throws exception
    @After("execution(* com.project.donuts.*.*.*(..)) " +
            "&& !execution(* com.project.donuts.beanConfig.*.*(..))")
    public void logMethodCallAfterExecution(JoinPoint joinPoint) {
        logger.info("{} has executed", joinPoint);
    }

    // executes only after method executes successfully
    @AfterReturning(
            pointcut = "execution(* com.project.donuts.*.*.*(..)) " +
                    "&& !execution(* com.project.donuts.beanConfig.*.*(..))",
            returning = "resultValue")
    public void logMethodCallAfterSuccessfulExecution(JoinPoint joinPoint, Object resultValue) {
        logger.info("{} has returned with value: {}", joinPoint, resultValue);
    }

    @Around("execution(* com.project.donuts.pgp.*.*(..)) ")
    public Object findExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTimeMillis = System.currentTimeMillis();
        Object returnValue = proceedingJoinPoint.proceed();
        long stopTimeMillis = System.currentTimeMillis();

        long executionDuration = stopTimeMillis - startTimeMillis;
        logger.info("{} executed in {} ms", proceedingJoinPoint, executionDuration);

        return returnValue;
    }
}
