package com.example.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* com.example.services.CrudService.find*(..))")
    public void allFindMethods() {}

    @Pointcut("execution(* com.example.services.CrudService.save*(..))")
    public void saveMethods() {}

    @Pointcut("execution(* com.example.services.CrudService.update*(..))")
    public void updateMethods() {}

    @Pointcut("execution(* com.example.services.CrudService.delete*(..))")
    public void deleteMethods() {}
}
