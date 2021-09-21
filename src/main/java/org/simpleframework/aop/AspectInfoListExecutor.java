package org.simpleframework.aop;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.simpleframework.aop.aspect.AspectInfo;
import org.simpleframework.util.ValidateUtil;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AspectInfoListExecutor implements MethodInterceptor {
    private Class<?> targetClass;
    private List<AspectInfo> sortedAspectInfoList;

    public AspectInfoListExecutor(Class<?> targetClass, List<AspectInfo> aspectInfoList) {
        this.targetClass = targetClass;
        this.sortedAspectInfoList = sortAspectInfoList(aspectInfoList);
    }

    private List<AspectInfo> sortAspectInfoList(List<AspectInfo> aspectInfoList) {
        Collections.sort(aspectInfoList, new Comparator<AspectInfo>() {
            @Override
            public int compare(AspectInfo o1, AspectInfo o2) {
                return o1.getOrderIndex() - o2.getOrderIndex();
            }
        });
        return aspectInfoList;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        if (ValidateUtil.isEmpty(sortedAspectInfoList)) {
            result = methodProxy.invokeSuper(o, args);
            return result;
        }
        invokeBeforeAdvice(method, args);
        try {
            result = methodProxy.invokeSuper(o, args);
            result = invokeAfterReturningAdvice(method, args, result);
        } catch (Throwable t) {
            invokeAfterThrowingAdvice(method, args, t);
        }
        return result;
    }

    private void invokeAfterThrowingAdvice(Method method, Object[] args, Throwable t) throws Throwable {
        for (int i = sortedAspectInfoList.size() - 1; i >= 0; i--) {
            sortedAspectInfoList.get(i).getAspect().afterReturning(targetClass, method, args, t);
        }
    }

    private Object invokeAfterReturningAdvice(Method method, Object[] args, Object result) throws Throwable {
        for (int i = sortedAspectInfoList.size() - 1; i >= 0; i--) {
            result = sortedAspectInfoList.get(i).getAspect().afterReturning(targetClass, method, args, result);
        }
        return result;
    }

    private void invokeBeforeAdvice(Method method, Object[] args) throws Throwable {
        for (AspectInfo aspectInfo : sortedAspectInfoList) {
            aspectInfo.getAspect().before(targetClass, method, args);
        }
    }


}
