package com.sm.finchley.gateway.filter;

import com.sm.finchley.loadbalance.support.RibbonFilterContext;
import com.sm.finchley.loadbalance.support.RibbonFilterContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lmwl
 */
@Component
@Slf4j
public class WeightRoundRibbonFilter implements GlobalFilter, Ordered {
    private static final String HEADER_VERSION = "version";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug(Thread.currentThread().getName());
        String version = exchange.getRequest().getHeaders().getFirst(HEADER_VERSION);
        if (StringUtils.isNotBlank(version)) {
            RibbonFilterContext currentContext = RibbonFilterContextHolder.getCurrentContext();
            currentContext.add(HEADER_VERSION, version);
        }
        Mono<Void> mono = chain.filter(exchange)
                .subscriberContext(ctx -> StringUtils.isBlank(version) ? ctx : ctx.put(HEADER_VERSION, version))
                //reactor  对所有请求都正常处理完成后加一个响应参数，或者是打印日志。
                .doFinally(signal -> RibbonFilterContextHolder.clearCurrentContext());
        return mono;
    }

    @Override
    public int getOrder() {
        return 10000;
    }
}
