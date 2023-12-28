package com.example.aop;

import com.example.util.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.NoSuchElementException;

@Component
@Aspect
@Slf4j
public class MyAspect {



    @Around("Pointcuts.allFindMethods()")
    public Object aroundFindMethods(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        if(methodSignature.getName().equals("findAll")) {
            log.info("Try to find all entities in crud service");
        } else if (methodSignature.getName().equals("findOne")) {
            log.info("Try to find one entity int crud service");
        }

        Object result;
        try {
            result = joinPoint.proceed();
            log.info("Operation successful");
        } catch (NoSuchElementException e) {
            throw e;
        }catch (RuntimeException e){
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Something went wrong(", e.getCause());
        }

        return result;
    }

    @Around("Pointcuts.saveMethods()")
    public Object aroundSaveMethods(ProceedingJoinPoint joinPoint) {
        log.info("Try to save entity in crud service");

        try {
            log.info("Saving entity...");
            return joinPoint.proceed();
        }catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            return new IllegalArgumentException("Wrong owner id");
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Something went wrong(", e.getCause());
        }
    }

    @Around("Pointcuts.updateMethods()")
    public Object aroundUpdateMethods(ProceedingJoinPoint joinPoint) {
        log.info("Try to update entity in crud service");

        try {
            log.info("Updating entity");
            return joinPoint.proceed();
        } catch (IllegalArgumentException e ){
            throw e;
        }catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            return new IllegalArgumentException("Wrong owner id");
        }catch (RuntimeException e){
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Something went wrong(", e.getCause());
        }
    }

    @Around("Pointcuts.deleteMethods()")
    public Object aroundDeleteMethods(ProceedingJoinPoint joinPoint) {
        log.info("Try to delete entity in crud service");
        try {
            log.info("Deleting entity");
            return joinPoint.proceed();
        }catch (RuntimeException e){
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Something went wrong(", e.getCause());
        }
    }

    @Around("Pointcuts.fileService()")
    public Object aroundFileServiceMethods(ProceedingJoinPoint joinPoint) {
        log.info("Operation ander file");
        try{
            return joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Sorry something went wrong", e.getCause());
        }
    }
}
