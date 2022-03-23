package com.sm.sc.controller;

import com.sm.sc.feign.HelloService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liumeng
 * @project storage-service
 * @description
 */
@RestController
public class HelloServiceController implements HelloService {
    @Override
    public String hello(@RequestParam String name) {
        return "hello " + name;
    }
}
