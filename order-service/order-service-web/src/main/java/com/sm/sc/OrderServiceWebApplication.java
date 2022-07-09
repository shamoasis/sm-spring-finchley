package com.sm.sc;

import com.sm.finchley.loadbalance.config.GrayVersionLoadBalancerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients
@EnableAsync
@LoadBalancerClients(defaultConfiguration = {GrayVersionLoadBalancerConfiguration.class})
public class OrderServiceWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceWebApplication.class, args);
    }

}
