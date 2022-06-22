package com.sm.finchley.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sm.finchley.gateway.dto.MyCachedBodyOutputMessage;
import com.sm.finchley.loadbalance.utils.RSAUtils;
import com.sm.finchley.loadbalance.utils.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author lmwl
 */
@Configuration
@Component
@AllArgsConstructor
public class DecryptRequestFilter implements GlobalFilter, Ordered {
    private static final String ERROR_MESSAGE = "拒绝服务";
    private final StringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1 获取时间戳
        Long dateTimestamp = getDateTimestamp(exchange.getRequest().getHeaders());
        //2 获取RequestId
        String requestId = getRequestId(exchange.getRequest().getHeaders());
        //3 获取签名
        String sign = getSign(exchange.getRequest().getHeaders());
        //4 如果是登录不校验Token
        String requestUrl = exchange.getRequest().getPath().value();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        if (!pathMatcher.match("/user/login", requestUrl)) {
            String token = exchange.getRequest().getHeaders().getFirst(UserConstant.TOKEN);
            Claims claim = TokenUtils.getClaim(token);
            if (StringUtils.isBlank(token) || claim == null) {
                return FilterUtils.invalidToken(exchange);
            }
        }
        //5 修改请求参数,并获取请求参数
        Map<String, Object> paramMap;
        try {
            paramMap = updateRequestParam(exchange);
        } catch (Exception e) {
            return FilterUtils.invalidUrl(exchange);
        }
        //6 获取请求体,修改请求体
        ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(body -> {
            String encrypt = RSAUtils.decrypt(body, RSAConstant.PRIVATE_KEY);
            JSONObject jsonObject = JSON.parseObject(encrypt);
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                paramMap.put(entry.getKey(), entry.getValue());
            }
            checkSign(sign, dateTimestamp, requestId, paramMap);
            return Mono.just(encrypt);
        });

        //创建BodyInserter修改请求体

        BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        //创建CachedBodyOutputMessage并且把请求param加入,初始化校验信息
        MyCachedBodyOutputMessage outputMessage = new MyCachedBodyOutputMessage(exchange, headers);
        outputMessage.initial(paramMap, requestId, sign, dateTimestamp);
        return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
            ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                public Flux<DataBuffer> getBody() {
                    Flux<DataBuffer> body = outputMessage.getBody();
                    if (body.equals(Flux.empty())) {
                        //验证签名
                        checkSign(outputMessage.getSign(), outputMessage.getDateTimestamp(), outputMessage.getRequestId(), outputMessage.getParamMap());
                    }
                    return outputMessage.getBody();
                }
            };
            return chain.filter(exchange.mutate().request(decorator).build());
        }));

    }

    public void checkSign(String sign, Long dateTimestamp, String requestId, Map<String, Object> paramMap) {
        String str = JSON.toJSONString(paramMap) + requestId + dateTimestamp;
        String tempSign = RSAUtils.MD5(str);
        if (!tempSign.equals(sign)) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
    }

    /**
     * 修改前端传的参数
     */
    private Map<String, Object> updateRequestParam(ServerWebExchange exchange) throws NoSuchFieldException, IllegalAccessException {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String query = uri.getQuery();
        if (StringUtils.isNotBlank(query) && query.contains("param")) {
            String[] split = query.split("=");
            String param = RSAUtils.decrypt(split[1], RSAConstant.PRIVATE_KEY);
            Field targetQuery = uri.getClass().getDeclaredField("query");
            targetQuery.setAccessible(true);
            targetQuery.set(uri, param);
            return getParamMap(param);
        }
        return new TreeMap<>();
    }


    private Map<String, Object> getParamMap(String param) {
        Map<String, Object> map = new TreeMap<>();
        String[] split = param.split("&");
        for (String str : split) {
            String[] params = str.split("=");
            map.put(params[0], params[1]);
        }
        return map;
    }


    private String getSign(HttpHeaders headers) {
        List<String> list = headers.get("sign");
        if (CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        return list.get(0);
    }

    private Long getDateTimestamp(HttpHeaders httpHeaders) {
        List<String> list = httpHeaders.get("timestamp");
        if (CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        long timestamp = Long.parseLong(list.get(0));
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        //有效时长为5分钟
        if (Math.abs(currentTimeMillis - timestamp) > 300) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        return timestamp;
    }

    private String getRequestId(HttpHeaders headers) {
        List<String> list = headers.get("requestId");
        if (CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        String requestId = list.get(0);
        //如果requestId存在redis中直接返回
        //String temp = redisTemplate.opsForValue().get(requestId);
        //if (StringUtils.isNotBlank(temp)) {
        //    throw new IllegalArgumentException(ERROR_MESSAGE);
        //}
        //redisTemplate.opsForValue().set(requestId, requestId, 5, TimeUnit.MINUTES);
        return requestId;
    }

    @Override
    public int getOrder() {
        return 80;
    }

    public static class UserConstant {
        public static final String TOKEN = "Authorization";
    }

    public static final class RSAConstant {
        public static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIW1OUvrUiogZ359EtSEnQyOyoVcUmzAKiEnjmbnVa9vEM9E/eCWVbRYKGTvgPkkQ6kwNJajgLtF+gaUFE1buRDqpga6RhnmOBinOhPT6Cneif3p9BcTJAnKy/3GJM8h2ZJddVWPUcA4nDb1FvPEhUpRLPM9e8S1dFO0ILX7CQAlAgMBAAECgYBC4amtbiKFa/wY61tV7pfYRjzLhKi+OUlZmD3E/4Z+4KGZ7DrJ8qkgMtDR3HO5LAikQrare1HTW2d7juqw32ascu+uDObf4yrYNKin+ZDLUYvIDfLhThPxnZJwQ/trdtfxO3VM//XbwZacmwYbAsYW/3QPUXwwOPAgbC2oth8kqQJBANKLyXcdjZx4cwJVl7xNeC847su8y6bPpcBASsaQloCIPiNBIg1h76dpfEGIQBYWJWbBsxtHe/MhOmz7fNFDS2sCQQCiktYZR0dZNH4eNX329LoRuBiltpr9tf36rVOlKr1GSHkLYEHF2qtyXV2mdrY8ZWpvuo3qm1oSLaqmop2rN9avAkBHk85B+IIUF77BpGeZVJzvMOO9z8lMRHuNCE5jgvQnbinxwkrZUdovh+T+QlvHJnBApslFFOBGn51FP5oHamFRAkEAmwZmPsinkrrpoKjlqz6GyCrC5hKRDWoj/IyXfKKaxpCJTH3HeoIghvfdO8Vr1X/n1Q8SESt+4mLFngznSMQAZQJBAJx07bCFYbA2IocfFV5LTEYTIiUeKdue2NP2yWqZ/+tB5H7jNwQTJmX1mn0W/sZm4+nJM7SjfETpNZhH49+rV6U=";
    }
}
