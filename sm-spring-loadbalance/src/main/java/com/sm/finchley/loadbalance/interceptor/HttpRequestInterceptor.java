package com.sm.finchley.loadbalance.interceptor;

import com.sm.finchley.loadbalance.support.GrayFilterContext;
import com.sm.finchley.loadbalance.support.GrayFilterContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

import static com.sm.finchley.loadbalance.support.DefaultGrayFilterContext.VERSION;

/**
 * @author lmwl
 */
public class HttpRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Enumeration<String> headers = request.getHeaders("version");
        if (headers.hasMoreElements()) {
            GrayFilterContext currentContext = GrayFilterContextHolder.getCurrentContext();
            currentContext.add(VERSION, headers.nextElement());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        GrayFilterContextHolder.clearCurrentContext();
    }
}