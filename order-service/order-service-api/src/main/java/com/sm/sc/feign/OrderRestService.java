package com.sm.sc.feign;

import com.sm.sc.dto.OrderDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liumeng
 * @project storage-service
 * @description
 */
@RequestMapping("/order")
public interface OrderRestService {
    @PostMapping()
    Integer createOrder(@RequestBody OrderDto order);

    @GetMapping("/{commodityCode}")
    List<OrderDto> selectOrderByCode(@PathVariable("commodityCode") String commodityCode);

}
