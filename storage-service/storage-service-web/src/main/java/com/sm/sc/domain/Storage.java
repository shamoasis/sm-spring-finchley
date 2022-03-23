package com.sm.sc.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Storage {
    private Integer id;

    private String commoditycode;

    private Integer count;

    private Date createtime;

    private Date updatetime;
}