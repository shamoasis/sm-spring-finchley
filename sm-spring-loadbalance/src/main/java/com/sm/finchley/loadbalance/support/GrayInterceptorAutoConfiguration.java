package com.sm.finchley.loadbalance.support;

import com.sm.finchley.loadbalance.interceptor.FeignRequestInterceptor;
import com.sm.finchley.loadbalance.interceptor.HttpRequestInterceptor;
import com.sm.finchley.loadbalance.interceptor.RestTemplateInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.openfeign.loadbalancer.FeignLoadBalancerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @project sm-spring-finchley
 * @description:
 * @author: lm
 * @create: 2022-07-08
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class GrayInterceptorAutoConfiguration {
    @Bean
    @ConditionalOnClass(FeignLoadBalancerAutoConfiguration.class)
    public FeignRequestInterceptor feignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new HttpRequestInterceptor());
            }
        };
    }

    @Bean
    @ConditionalOnClass(RestTemplate.class)
    public RestTemplateInterceptor restTemplateInterceptor() {
        return new RestTemplateInterceptor();
    }
}
