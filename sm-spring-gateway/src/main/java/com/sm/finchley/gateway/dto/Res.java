package com.sm.finchley.gateway.dto;

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
