package com.sm.sc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author lmwl
 */
@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {
    @Override
    @Async
    public Integer notify(String user, String message) {
        log.info("notify begin, user: {} received message: {}", user, message);
        logHeader();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error("notify interrupted.");
            e.printStackTrace();
        }
        log.info("notify end, user: {} received message: {}", user, message);
        return 0;
    }

    private void logHeader() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 获取头部信息
            Enumeration<String> headerNames = request.getHeaderNames();
            // 把头部信息里面的信息装进去requestTemplate
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);

                    log.info("request: {}, header:{},value:{}", request.getRequestURI(), name, values);
                }
            } else {
                log.error("request attribute is empty.");
            }
        }
    }
}
