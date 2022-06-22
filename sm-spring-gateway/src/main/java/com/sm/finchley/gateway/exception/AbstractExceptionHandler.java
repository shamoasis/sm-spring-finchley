package com.sm.finchley.gateway.exception;

import com.alibaba.fastjson.JSONObject;
import com.sm.finchley.loadbalance.utils.RSAUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;

/**
 * @author lmwl
 */
public abstract class AbstractExceptionHandler {
    protected JSONObject buildErrorMap(Throwable ex) {
        JSONObject json = new JSONObject();
        if (ex instanceof RSAUtils.RSAException || ex instanceof IllegalArgumentException) {
            json.put("code", HttpStatus.BAD_REQUEST.value());
            if (StringUtils.isNotBlank(ex.getMessage())) {
                json.put("msg", ex.getMessage());
            } else {
                json.put("msg", "无效的请求");
            }

        } else {
            json.put("code", HttpStatus.BAD_REQUEST.value());
            json.put("msg", "未知错误联系管理员");
        }
        return json;
    }
}
