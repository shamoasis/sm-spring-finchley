package com.sm.sc.service;

import com.alibaba.fastjson.JSON;
import com.sm.sc.dto.OrderDto;
import com.sm.sc.feign.StorageRestFeign;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author liumeng
 * @project storage-service
 * @description
 */
@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private static final Map<Integer, OrderDto> orders = new ConcurrentHashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    private final StorageRestFeign storageRestFeign;
    private final NotifyService notifyService;

    @Override
    public Integer createOrder(OrderDto order) {
        log.info("order create begin");
        order.setId(idGenerator.getAndIncrement());
        orders.put(order.getId(), order);
        storageRestFeign.reduceStock(order.getCommodityCode(), order.getCount());
        notifyService.notify(order.getUserId(), JSON.toJSONString(order));
        log.info("order create end");
        return order.getId();
    }

    @Override
    public List<OrderDto> getByCommodityCode(String commodityCode) {

        return orders.values().stream().filter(d -> d.getCommodityCode().equals(commodityCode))
                .collect(Collectors.toList());
    }
}
