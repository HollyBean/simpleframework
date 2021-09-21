package com.hollybean.controller;

import com.hollybean.service.HelloService;
import org.simpleframework.inject.annotation.Autowired;
import org.simpleframework.ioc.annotation.Controller;

@Controller
public class HelloController {
    @Autowired(value = "BoyHelloService")
    private HelloService helloService;

    public void sayHello() {
        System.out.println(helloService.sayHello());
    }
}
