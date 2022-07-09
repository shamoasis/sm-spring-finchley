package com.sm.finchley.loadbalance.interceptor;

import com.sm.finchley.loadbalance.support.GrayFilterContext;
import com.sm.finchley.loadbalance.support.GrayFilterContextHolder;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.util.StringUtils;

import java.io.IOException;

import static com.sm.finchley.loadbalance.support.DefaultGrayFilterContext.VERSION;


/**
 * @author lmwl
 */
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
        GrayFilterContext currentContext = GrayFilterContextHolder.getCurrentContext();
        String version = currentContext.getAttributes().get(VERSION);
        if (!StringUtils.hasText(version)) {
            requestWrapper.getHeaders().add(VERSION, version);
        }
        return execution.execute(requestWrapper, body);
    }
}