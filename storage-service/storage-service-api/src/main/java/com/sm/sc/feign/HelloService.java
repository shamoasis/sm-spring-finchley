package com.sm.sc.feign;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liumeng
 * @project storage-service
 * @description
 */
@RequestMapping("/storage")
public interface HelloService {
    @GetMapping("/hi")
    String hello(String name);
}
