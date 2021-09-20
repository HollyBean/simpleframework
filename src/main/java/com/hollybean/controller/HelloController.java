package com.hollybean.controller;

import com.hollybean.service.HelloService;

public class HelloController {
    private HelloService helloService;

    public void sayHello() {
        System.out.println(helloService.sayHello());
    }
}
