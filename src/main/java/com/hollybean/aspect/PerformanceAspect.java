package com.hollybean.aspect;

import org.apache.log4j.Logger;
import org.simpleframework.aop.annotation.Aspect;
import org.simpleframework.aop.annotation.Order;
import org.simpleframework.aop.aspect.DefaultAspect;
import org.simpleframework.ioc.annotation.Controller;

import java.lang.reflect.Method;

@Aspect(Controller.class)
@Order(1)
public class PerformanceAspect extends DefaultAspect {
    private static Logger logger = Logger.getLogger(PerformanceAspect.class);
    private long costTime;

    @Override
    public void before(Class<?> targetClass, Method method, Object[] args) throws Throwable {
        logger.warn("PerformanceAspect before aspect!");
        costTime = System.currentTimeMillis();
    }

    @Override
    public Object afterReturning(Class<?> targetClass, Method method, Object[] args, Object result) throws Throwable {
        logger.warn("PerformanceAspect afterReturning aspect!");
        long endTime = System.currentTimeMillis();
        costTime = endTime - costTime;
        return result;
    }

    @Override
    public void afterThrowing(Class<?> targetClass, Method method, Object[] args, Throwable t) throws Throwable {
        logger.warn("PerformanceAspect afterThrowing aspect!");
        long endTime = System.currentTimeMillis();
        costTime = endTime - costTime;
    }
}
