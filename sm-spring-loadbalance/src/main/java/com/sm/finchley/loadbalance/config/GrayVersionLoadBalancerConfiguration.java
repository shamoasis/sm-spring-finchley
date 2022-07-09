package com.sm.finchley.loadbalance.config;

import com.sm.finchley.loadbalance.gateway.GrayVersionLoadBalancer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @project sm-spring-finchley
 * @description: 配置类不能添加@Configuration注解。 在网关启动类使用注解@LoadBalancerClient指定哪些服务使用自定义负载均衡算法
 * <p>
 * 通过@LoadBalancerClient(value = "auth-service", configuration = GrayVersionLoadBalancerConfiguration.class)，对于user-service启用自定义负载均衡算法;
 * <p>
 * 或通过@LoadBalancerClients(defaultConfiguration = GrayVersionLoadBalancerConfiguration.class)为所有服务启用自定义负载均衡算法。
 * @author: lm
 * @create: 2022-07-08
 */
public class GrayVersionLoadBalancerConfiguration {

    @Bean
    ReactorLoadBalancer<ServiceInstance> grayVersionLoadBalancer(Environment env, LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = env.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new GrayVersionLoadBalancer(loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
    }
}
