package com.hollybean.service;

import com.hollybean.dao.HelloDao;

public class HelloService {
    private HelloDao helloDao;

    public String sayHello() {
        return helloDao.sayHello();
    }
}
