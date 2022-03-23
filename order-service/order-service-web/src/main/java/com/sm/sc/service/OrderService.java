package com.sm.sc.service;

import com.sm.sc.dto.OrderDto;

import java.util.List;

public interface OrderService {
    Integer createOrder(OrderDto order);

    List<OrderDto> getByCommodityCode(String commodityCode);
}
