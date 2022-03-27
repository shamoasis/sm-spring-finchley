package com.sm.finchley.loadbalance.gateway;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * @author lmwl
 */
public class GrayRoundClientFilter extends LoadBalancerClientFilter {
    private final GrayRibbonLoadBalancerClient loadBalancer;

    public GrayRoundClientFilter(GrayRibbonLoadBalancerClient loadBalancer) {
        super(loadBalancer);
        this.loadBalancer = loadBalancer;
    }

    @Override
    protected ServiceInstance choose(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        return this.loadBalancer.choose(((URI) exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)).getHost(), headers);
    }
}
