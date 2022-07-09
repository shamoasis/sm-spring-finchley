package com.sm.finchley.loadbalance.gateway;


import com.sm.finchley.loadbalance.support.DefaultGrayFilterContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author lmwl
 * 第一种实现方式，通过自定义负载均衡策略来实现。
 */
@Slf4j
public class GrayVersionLoadBalancer implements ReactorServiceInstanceLoadBalancer {
    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    private String serviceId;
    private final AtomicInteger position;

    public GrayVersionLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        this(serviceInstanceListSupplierProvider, serviceId, new Random().nextInt(100));
    }

    public GrayVersionLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId, int seedPosition) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.serviceId = serviceId;
        this.position = new AtomicInteger(seedPosition);
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map(serviceInstances -> processInstanceResponse(serviceInstances, request));
    }

    Response<ServiceInstance> processInstanceResponse(List<ServiceInstance> instances, Request request) {

        // 注册中心无可用实例 抛出异常
        if (ObjectUtils.isEmpty(instances)) {
            log.warn("No instance available {}", serviceId);
            return new EmptyResponse();
        }

        DefaultRequestContext requestContext = (DefaultRequestContext) request.getContext();
        RequestData clientRequest = (RequestData) requestContext.getClientRequest();
        HttpHeaders headers = clientRequest.getHeaders();

        String reqVersion = headers.getFirst(DefaultGrayFilterContext.VERSION);
        if (!StringUtils.hasText(reqVersion)) {
            return processRibbonInstanceResponse(instances);
        }
        log.info("request header version : {}", reqVersion);
        List<ServiceInstance> serviceInstances = instances.stream()
                .filter(instance -> reqVersion.equals(instance.getMetadata().get("version")))
                .collect(Collectors.toList());

        if (serviceInstances.size() > 0) {
            return processRibbonInstanceResponse(serviceInstances);
        } else {
            return processRibbonInstanceResponse(instances);
        }
    }

    /**
     * 负载均衡器
     * 参考 org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer#getInstanceResponse
     *
     * @author javadaily
     */
    private Response<ServiceInstance> processRibbonInstanceResponse(List<ServiceInstance> instances) {
        int pos = Math.abs(this.position.incrementAndGet());
        ServiceInstance instance = instances.get(pos % instances.size());
        log.info("service:{},host:{},port:{}", instance.getServiceId(), instance.getHost(), instance.getPort());
        return new DefaultResponse(instance);
    }

}
