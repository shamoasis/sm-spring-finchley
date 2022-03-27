package com.sm.finchley.loadbalance.interceptor;

import com.sm.finchley.loadbalance.support.RibbonFilterContext;
import com.sm.finchley.loadbalance.support.RibbonFilterContextHolder;
import feign.RequestInterceptor;
import org.springframework.util.StringUtils;

import static com.sm.finchley.loadbalance.support.DefaultRibbonFilterContext.VERSION;


/**
 * @author lmwl
 */
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