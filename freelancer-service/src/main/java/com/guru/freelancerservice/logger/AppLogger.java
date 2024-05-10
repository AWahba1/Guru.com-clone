package com.guru.freelancerservice.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class AppLogger {

    private static final Logger log = LoggerFactory.getLogger(AppLogger.class);

    @Pointcut("execution(* com.guru.freelancerservice.services.*.*(..))")
    public void serviceMethods() {
    }

    @Pointcut("execution(* com.guru.freelancerservice.controllers.*.*(..))")
    public void controllerMethods() {
    }

    @Pointcut("execution(* com.guru.freelancerservice.repositories.*.*(..))")
    public void repositoryMethods() {
    }

    @Around("serviceMethods() || controllerMethods() || repositoryMethods()")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        ObjectMapper mapper = new ObjectMapper();
        String argsString = mapper.writeValueAsString(args);
        log.info("Method {}.{} called with arguments: {}", className, methodName, argsString);
        Object result;
        try{
            result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info("Method {}.{} returned (Response): {} in {} ms", className, methodName, mapper.writeValueAsString(result), elapsedTime);
        }
        catch (Exception e){
            log.error("Method {}.{} threw an exception: {} ", className, methodName, e.getMessage());
            throw e;
        }
        return result;
    }

}
