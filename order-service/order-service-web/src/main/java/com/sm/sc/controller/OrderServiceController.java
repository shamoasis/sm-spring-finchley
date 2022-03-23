package com.sm.sc.controller;

import com.sm.sc.dto.OrderDto;
import com.sm.sc.feign.OrderRestService;
import com.sm.sc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liumeng
 * @project storage-service
 * @description
 */

@RestController
@Validated
public class OrderServiceController implements OrderRestService {
    @Autowired
    private OrderService orderService;

    @Override
    public Integer createOrder(@RequestBody OrderDto order) {
        return orderService.createOrder(order);
    }

    @Override
    public List<OrderDto> selectOrderByCode(@PathVariable String commodityCode) {
        return orderService.getByCommodityCode(commodityCode);
    }
}
