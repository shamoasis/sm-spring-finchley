package com.sm.finchley.loadbalance.config;

import com.sm.finchley.loadbalance.gateway.GrayVersionServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @project sm-spring-finchley
 * @description:
 * @author: lm
 * @create: 2022-07-08
 */
public class GrayVersionServiceInstanceListSupplierConfiguration {
    @Bean
    ServiceInstanceListSupplier serviceInstanceListSupplier(ConfigurableApplicationContext context) {
        ServiceInstanceListSupplier delegate = ServiceInstanceListSupplier.builder()
                .withDiscoveryClient()
                .withCaching()
                .build(context);

        return new GrayVersionServiceInstanceListSupplier(delegate);
    }
}
