package com.hollybean.service;

import com.hollybean.dao.HelloDao;
import org.simpleframework.inject.annotation.Autowired;
import org.simpleframework.ioc.annotation.Service;

@Service
public class HelloService {
    @Autowired
    private HelloDao helloDao;

    public String sayHello() {
        return helloDao.sayHello();
    }
}
