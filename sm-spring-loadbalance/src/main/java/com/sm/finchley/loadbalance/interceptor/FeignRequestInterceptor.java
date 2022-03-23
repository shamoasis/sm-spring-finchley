package com.sm.finchley.loadbalance.interceptor;

import com.sm.finchley.loadbalance.support.RibbonFilterContext;
import com.sm.finchley.loadbalance.support.RibbonFilterContextHolder;
import feign.Feign;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.sm.finchley.loadbalance.support.DefaultRibbonFilterContext.VERSION;


/**
 * @author lmwl
 */
@Component
@ConditionalOnClass(Feign.class)
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(feign.RequestTemplate template) {
        RibbonFilterContext currentContext = RibbonFilterContextHolder.getCurrentContext();

        String version = currentContext.getAttributes().get(VERSION);
        if (StringUtils.hasText(version)) {
            template.header(VERSION, version);
        }
    }
}