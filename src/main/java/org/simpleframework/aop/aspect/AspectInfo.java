package org.simpleframework.aop.aspect;

public class AspectInfo {
    private int orderIndex;
    private DefaultAspect aspect;

    public int getOrderIndex() {
        return orderIndex;
    }

    public DefaultAspect getAspect() {
        return aspect;
    }

    public AspectInfo(int orderIndex, DefaultAspect aspect) {
        this.orderIndex = orderIndex;
        this.aspect = aspect;
    }
}
