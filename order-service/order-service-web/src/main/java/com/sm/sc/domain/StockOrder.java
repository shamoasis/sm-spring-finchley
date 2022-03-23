package com.sm.sc.domain;

import lombok.Data;

import java.util.Date;

@Data
public class StockOrder {
    private Integer id;

    private String userId;

    private String commodityCode;

    private Integer count;

    private Integer money;

    private Date createTime;

    private Date updatetime;
}