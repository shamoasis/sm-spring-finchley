package com.sm.finchley.loadbalance.gateway;

import com.sm.finchley.loadbalance.support.DefaultGrayFilterContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;
import org.springframework.cloud.loadbalancer.core.DelegatingServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @project sm-spring-finchley
 * @description: 自定义服务实例筛选逻辑
 * 第二种实现方法，通过实现ServiceInstanceListSupplier来自定义服务筛选逻辑，我们可以直接继承DelegatingServiceInstanceListSupplier来实现。
 * 参考：org.springframework.cloud.loadbalancer.core.ZonePreferenceServiceInstanceListSupplier
 * 通过@LoadBalancerClient(value = "auth-service", configuration = VersionServiceInstanceListSupplierConfiguration.class)，对于auth-service启用自定义负载均衡算法;
 * <p>
 * 或通过@LoadBalancerClients(defaultConfiguration = VersionServiceInstanceListSupplierConfiguration.class)为所有服务启用自定义负载均衡算法。
 * @author: lm
 * @create: 2022-07-08
 */
@Slf4j
public class GrayVersionServiceInstanceListSupplier extends DelegatingServiceInstanceListSupplier {


    public GrayVersionServiceInstanceListSupplier(ServiceInstanceListSupplier delegate) {
        super(delegate);
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return delegate.get();
    }

    @Override
    public Flux<List<ServiceInstance>> get(Request request) {
        return delegate.get(request).map(instances -> filteredByVersion(instances, getVersion(request.getContext())));
    }

    /**
     * filter instance by requestVersion
     */
    private List<ServiceInstance> filteredByVersion(List<ServiceInstance> instances, String requestVersion) {
        log.info("request version is {}", requestVersion);
        if (!StringUtils.hasText(requestVersion)) {
            return instances;
        }

        List<ServiceInstance> filteredInstances = instances.stream()
                .filter(instance -> requestVersion.equalsIgnoreCase(instance.getMetadata().getOrDefault("version", "")))
                .collect(Collectors.toList());

        if (filteredInstances.size() > 0) {
            return filteredInstances;
        }

        return instances;
    }

    private String getVersion(Object requestContext) {
        if (requestContext == null) {
            return null;
        }
        String version = null;
        if (requestContext instanceof RequestDataContext) {
            version = getVersionFromHeader((RequestDataContext) requestContext);
        }
        return version;
    }

    /**
     * get version from header
     *
     * @author javadaily
     */
    private String getVersionFromHeader(RequestDataContext context) {
        if (context.getClientRequest() != null) {
            HttpHeaders headers = context.getClientRequest().getHeaders();
            if (headers != null) {
                //could extract to the properties
                return headers.getFirst(DefaultGrayFilterContext.VERSION);
            }
        }
        return null;
    }

}
