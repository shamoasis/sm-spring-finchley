package com.sm.sc.controller;

import com.sm.sc.dto.StorageDto;
import com.sm.sc.feign.StorageRestService;
import com.sm.sc.service.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liumeng
 * @project storage-service
 * @description
 */
@RestController
@AllArgsConstructor
public class StorageController implements StorageRestService {
    private final StorageService storageService;

    @Override
    public int reduceStock(@PathVariable String commodityCode, @PathVariable Integer count) {
        return storageService.reduceStock(commodityCode, count);
    }

    @Override
    public int initStock(@PathVariable String commodityCode, @PathVariable Integer count) {
        return storageService.initStock(commodityCode, count);
    }

    @Override
    public List<StorageDto> selectStockByCode(@PathVariable String commodityCode) {
        return storageService.selectStockByCode(commodityCode);
    }
}
