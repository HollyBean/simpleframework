package com.hollybean.service.impl;

import com.hollybean.dao.HelloDao;
import com.hollybean.service.HelloService;
import org.simpleframework.inject.annotation.Autowired;
import org.simpleframework.ioc.annotation.Service;

@Service
public class GirlHelloService implements HelloService {
    @Autowired
    private HelloDao helloDao;
    @Override
    public String sayHello() {
        return "Girl:" + helloDao.sayHello();
    }
}
