package com.sm.sc.feign;

import com.sm.sc.dto.StorageDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liumeng
 * @project storage-service
 * @description
 */
@RequestMapping("/stock")
public interface StorageRestService {
    @PostMapping("/stockCount/{commodityCode}/{count}")
    int reduceStock(@PathVariable("commodityCode") String commodityCode, @PathVariable("count") Integer count);

    @PutMapping("/stockCount/{commodityCode}/{count}")
    int initStock(@PathVariable("commodityCode") String commodityCode, @PathVariable("count") Integer count);

    @GetMapping("/{commodityCode}")
    List<StorageDto> selectStockByCode(@PathVariable("commodityCode") String commodityCode);
}
