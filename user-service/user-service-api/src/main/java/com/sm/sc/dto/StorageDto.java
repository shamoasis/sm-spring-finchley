package com.sm.sc.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liumeng
 * @project storage-service
 * @description
 */
@Data
public class StorageDto implements Serializable {
    private Integer id;

    private String commodityCode;

    private Integer count;
}
