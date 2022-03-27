package com.sm.finchley.loadbalance.support;

import com.sm.finchley.loadbalance.gateway.GrayRibbonLoadBalancerClient;
import com.sm.finchley.loadbalance.gateway.GrayRoundClientFilter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lmwl
 */
@Configuration
@AutoConfigureAfter({RibbonAutoConfiguration.class})
@AutoConfigureBefore({GatewayLoadBalancerClientAutoConfiguration.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class GrayGatewayFilterAutoConfiguration {

    @Bean
    public GrayRibbonLoadBalancerClient loadBalancerClient(SpringClientFactory springClientFactory) {
        return new GrayRibbonLoadBalancerClient(springClientFactory);
    }

    @Bean
    @ConditionalOnClass(GlobalFilter.class)
    @ConditionalOnBean(GrayRibbonLoadBalancerClient.class)
    public GrayRoundClientFilter GrayRoundClientFilter(GrayRibbonLoadBalancerClient grayRibbonLoadBalancerClient) {
        return new GrayRoundClientFilter(grayRibbonLoadBalancerClient);
    }
}
