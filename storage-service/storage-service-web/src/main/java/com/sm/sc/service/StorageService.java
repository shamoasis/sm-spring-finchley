package com.sm.sc.service;

import com.sm.sc.dto.StorageDto;

import java.util.List;

public interface StorageService {
    int initStock(String commodityCode, int count);

    int reduceStock(String commodityCode, int count);

    List<StorageDto> selectStockByCode(String commodityCode);
}
