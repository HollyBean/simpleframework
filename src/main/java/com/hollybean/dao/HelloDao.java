package com.hollybean.dao;

import org.simpleframework.ioc.annotation.Repository;

@Repository
public class HelloDao {
    public String sayHello() {
        return "Hello Spring";
    }
}
