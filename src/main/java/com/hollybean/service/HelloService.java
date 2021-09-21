package com.hollybean.service;

import com.hollybean.dao.HelloDao;
import org.simpleframework.ioc.annotation.Service;

@Service
public class HelloService {
    private HelloDao helloDao;

    public String sayHello() {
        return helloDao.sayHello();
    }
}
