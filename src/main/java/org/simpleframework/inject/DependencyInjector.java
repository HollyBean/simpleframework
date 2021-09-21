package org.simpleframework.inject;

import org.apache.log4j.Logger;
import org.simpleframework.inject.annotation.Autowired;
import org.simpleframework.ioc.BeanContainer;
import org.simpleframework.util.ValidateUtil;

import java.lang.reflect.Field;
import java.util.Set;

public class DependencyInjector {
    private static Logger logger = Logger.getLogger(DependencyInjector.class);
    private BeanContainer beanContainer;
    public DependencyInjector() {
        this.beanContainer = BeanContainer.getInstance();
    }

    public void doInject() {
        // 1.获取容器中所有的Class
        Set<Class<?>> classSet = beanContainer.getClasses();
        if (ValidateUtil.isEmpty(classSet)) {
            return ;
        }
        // 2.遍历class集合，查看是否类属性字段被标记Autowired的
        for (Class<?> clazz : classSet) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    String autowiredValue = autowired.value();
                    Class<?> classType = field.getType();
                    Object targetObject = beanContainer.getBean(clazz);
                    Object bean = getClassInstance(classType, autowiredValue);
                    if (bean == null) {
                        throw new RuntimeException("获取不到注入实例:" + classType.getSimpleName());
                    }
                    setFieldValue(field, targetObject, bean, true);
                }
            }
        }
    }

    private Object getClassInstance(Class<?> classType, String autowiredValue) {
        Object bean = beanContainer.getBean(classType);
        if (bean == null) {
            Set<Class<?>> implementClassSet = beanContainer.getImplementClassSet(classType);
            if (!ValidateUtil.isEmpty(implementClassSet)) {
                if (ValidateUtil.isEmpty(autowiredValue)) {
                    if (implementClassSet.size() == 1) {
                        return implementClassSet.iterator().next();
                    } else {
                        throw new RuntimeException("具备多个实现类且未指定注入实例：" + classType.getSimpleName());
                    }
                } else {
                    for (Class<?> implementClass : implementClassSet) {
                        if (implementClass.getSimpleName().equals(autowiredValue)) {
                            return beanContainer.getBean(implementClass);
                        }
                    }
                }
            }
        }
        return bean;
    }

    private void setFieldValue(Field field, Object targetObject, Object bean, boolean accessible) {
        field.setAccessible(accessible);
        try {
            field.set(targetObject, bean);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
