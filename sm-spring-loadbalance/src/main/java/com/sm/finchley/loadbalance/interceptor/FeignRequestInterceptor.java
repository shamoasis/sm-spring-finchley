package com.sm.finchley.loadbalance.interceptor;

import com.sm.finchley.loadbalance.support.GrayFilterContext;
import com.sm.finchley.loadbalance.support.GrayFilterContextHolder;
import feign.RequestInterceptor;
import org.springframework.util.StringUtils;

import static com.sm.finchley.loadbalance.support.DefaultGrayFilterContext.VERSION;


/**
 * @author lmwl
 */
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(feign.RequestTemplate template) {
        GrayFilterContext currentContext = GrayFilterContextHolder.getCurrentContext();

        String version = currentContext.getAttributes().get(VERSION);
        if (StringUtils.hasText(version)) {
            template.header(VERSION, version);
        }
    }
}