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
                    Class<?> classType = field.getType();
                    Object targetObject = beanContainer.getBean(clazz);
                    Object bean = beanContainer.getBean(classType);
                    if (bean == null) {
                        throw new RuntimeException("获取不到注入实例:" + classType.getSimpleName());
                    }
                    setFieldValue(field, targetObject, bean, true);
                }
            }
        }
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
