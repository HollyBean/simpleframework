package org.simpleframework.aop;

import org.simpleframework.aop.annotation.Aspect;
import org.simpleframework.aop.annotation.Order;
import org.simpleframework.aop.aspect.AspectInfo;
import org.simpleframework.aop.aspect.DefaultAspect;
import org.simpleframework.ioc.BeanContainer;
import org.simpleframework.util.ValidateUtil;

import java.lang.annotation.Annotation;
import java.util.*;

public class AspectWeaver {
    private BeanContainer beanContainer;
    public AspectWeaver() {
        this.beanContainer = BeanContainer.getInstance();
    }

    public void doWeaver() {
        Set<Class<?>> aspectClassSet = beanContainer.getClassByAnnotation(Aspect.class);
        if (ValidateUtil.isEmpty(aspectClassSet)) {
            return ;
        }
        Map<Class<? extends Annotation>, List<AspectInfo>> aspectInfoMap = new HashMap<>();
        categorizeAspect(aspectInfoMap, aspectClassSet);
        for (Class<? extends Annotation> weaveAnnotation : aspectInfoMap.keySet()) {
            Set<Class<?>> weaveClassSet = beanContainer.getClassByAnnotation(weaveAnnotation);
            if (ValidateUtil.isEmpty(weaveClassSet)) {
                continue;
            }
            for (Class<?> clazz : weaveClassSet) {
                AspectInfoListExecutor executor = new AspectInfoListExecutor(clazz, aspectInfoMap.get(weaveAnnotation));
                Object proxy = ProxyCreator.createProxy(clazz, executor);
                beanContainer.addBean(clazz, proxy);
            }
        }
    }

    private void categorizeAspect(Map<Class<? extends Annotation>, List<AspectInfo>> aspectInfoMap, Set<Class<?>> aspectClassSet) {
        for (Class<?> aspectClass : aspectClassSet) {
            if (!VerifyAspect(aspectClass)) {
                continue;
            }
            Aspect aspect = aspectClass.getAnnotation(Aspect.class);
            Order order = aspectClass.getAnnotation(Order.class);
            DefaultAspect defaultAspect = (DefaultAspect) beanContainer.getBean(aspectClass);
            AspectInfo aspectInfo = new AspectInfo(order.value(), defaultAspect);
            if (aspectInfoMap.containsKey(aspect.value())) {
                List<AspectInfo> aspectInfoList = aspectInfoMap.get(aspect.value());
                aspectInfoList.add(aspectInfo);
            } else {
                List<AspectInfo> aspectInfoList = new ArrayList<>();
                aspectInfoList.add(aspectInfo);
                aspectInfoMap.put(aspect.value(), aspectInfoList);
            }
        }
    }

    private boolean VerifyAspect(Class<?> aspectClass) {
        return aspectClass.isAnnotationPresent(Aspect.class) &&
                aspectClass.isAnnotationPresent(Order.class) &&
                DefaultAspect.class.isAssignableFrom(aspectClass) &&
                aspectClass.getAnnotation(Aspect.class).value() != Aspect.class;
    }
}
