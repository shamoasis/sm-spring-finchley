package com.sm.finchley.gateway.filter;

import lombok.Data;

/**
 * @author lmwl
 */
@Data
public class Res {
    private Integer code;
    private String message;
    private Object data;
}
