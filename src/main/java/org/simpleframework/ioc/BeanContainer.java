package org.simpleframework.ioc;

import org.apache.log4j.Logger;
import org.simpleframework.aop.annotation.Aspect;
import org.simpleframework.ioc.annotation.Component;
import org.simpleframework.ioc.annotation.Controller;
import org.simpleframework.ioc.annotation.Repository;
import org.simpleframework.ioc.annotation.Service;
import org.simpleframework.util.ClassUtil;
import org.simpleframework.util.ValidateUtil;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean容器：存储bean的实例
 * 枚举单例实现
 */
public class BeanContainer {
    private static Logger logger = Logger.getLogger(BeanContainer.class);
    private BeanContainer() {}
    private enum BeanContainerHolder {
        INSTANCE;
        private BeanContainer beanContainer;
        BeanContainerHolder() {
            this.beanContainer = new BeanContainer();
        }
    }
    public static BeanContainer getInstance() {
        return BeanContainerHolder.INSTANCE.beanContainer;
    }
    // 定义需要注入的Bean标签
    private static final List<Class<? extends Annotation>> BEAN_ANNOTATION =
            Arrays.asList(Controller.class, Service.class, Repository.class, Component.class, Aspect.class);
    // 定义存储使用的map
    private Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();
    private boolean loaded = false;
    public boolean isLoaded() { return loaded; }

    // 同步锁防止并发记载
    public synchronized void loadBeans(String packageName) {
        // 只加载一次
        if (isLoaded()) {
            logger.warn("BeanContainer已被初始化！");
            return ;
        }
        // 1.扫描获取包体下所有class
        Set<Class<?>> classSet = ClassUtil.extractPackageClassSet(packageName);
        if (ValidateUtil.isEmpty(classSet)) {
            logger.warn("包体扫描获取Class集合为空：" + packageName);
            return ;
        }
        for (Class<?> clazz : classSet) {
            for (Class<? extends Annotation> annotation : BEAN_ANNOTATION) {
                if (clazz.isAnnotationPresent(annotation)) {
                    try {
                        Object bean = ClassUtil.getBean(clazz, true);
                        beanMap.put(clazz, bean);
                    } catch (Throwable t) {
                        logger.error("Bean实例加载失败 ", t);
                        return ;
                    }
                }
            }
        }
        // 设置加载标识
        loaded = true;
    }
    // 获取bean容器大小
    public int size() {
        return beanMap.size();
    }

    public Set<Class<?>> getClasses() {
        return beanMap.keySet();
    }

    public Object getBean(Class<?> clazz) {
        return beanMap.get(clazz);
    }

    public void addBean(Class<?> clazz, Object bean) {
        beanMap.put(clazz, bean);
    }

    public Set<Class<?>> getImplementClassSet(Class<?> superClass) {
        Set<Class<?>> classSet = getClasses();
        if (ValidateUtil.isEmpty(classSet)) {
            return null;
        }
        Set<Class<?>> result = new HashSet<>();
        for (Class<?> aClass : classSet) {
            if (superClass.isAssignableFrom(aClass)) {
                result.add(aClass);
            }
        }
        return result.size() == 0 ? null : result;
    }

    public Set<Class<?>> getClassByAnnotation(Class<? extends Annotation> annotation) {
        Set<Class<?>> classSet = getClasses();
        if (ValidateUtil.isEmpty(classSet)) {
            return null;
        }
        Set<Class<?>> result = new HashSet<>();
        for (Class<?> aClass : classSet) {
            if (aClass.isAnnotationPresent(annotation)) {
                result.add(aClass);
            }
        }
        return result.size() == 0 ? null : result;
    }
}
