package com.sm.sc.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author liumeng
 * @project storage-service
 * @description
 */
@Data
public class OrderDto implements Serializable {
    private Integer id;

    private String userId;
    @NotBlank
    private String commodityCode;

    private Integer count;

    private Integer money;
}
