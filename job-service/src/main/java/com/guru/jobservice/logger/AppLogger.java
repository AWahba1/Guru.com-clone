package com.guru.jobservice.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect
@Component
public class AppLogger {

    private static final Logger log = LoggerFactory.getLogger(AppLogger.class);
    private static final String LOG_FILE_PATH = "JobAppLogs.txt";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Pointcut("execution(* com.guru.jobservice.services.*.*(..))")
    public void serviceMethods() {
    }

    @Pointcut("execution(* com.guru.jobservice.controllers.*.*(..))")
    public void controllerMethods() {
    }

    @Pointcut("execution(* com.guru.jobservice.repositories.*.*(..))")
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
        logToFile(className, methodName, "Called with arguments: " + argsString);

        Object result;
        try {
            result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info("Method {}.{} returned (Response): {} in {} ms", className, methodName, mapper.writeValueAsString(result), elapsedTime);
            logToFile(className, methodName, "Returned (Response): " + mapper.writeValueAsString(result) + " in " + elapsedTime + " ms");
        } catch (Exception e) {
            log.error("Method {}.{} threw an exception: {} ", className, methodName, e.getMessage());
            logToFile(className, methodName, "Threw an exception: " + e.getMessage());
            throw e;
        }
        return result;
    }

    private void logToFile(String className, String methodName, String logMessage) {
        try (FileWriter writer = new FileWriter(LOG_FILE_PATH, true)) {
            writer.write(String.format("[%s] %s.%s - %s\n", getCurrentTimeStamp(), className, methodName, logMessage));
            writer.flush();
        } catch (IOException e) {
            log.error("Error writing to log file: {}", e.getMessage());
        }
    }

    private String getCurrentTimeStamp() {
        return dateFormat.format(new Date());
    }
}
