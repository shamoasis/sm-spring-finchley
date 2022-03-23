package com.sm.sc.service;

import com.sm.sc.dto.StorageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
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
@Slf4j
public class StorageServiceImpl implements StorageService, InitializingBean {

    private static final Map<String, StorageDto> storages = new ConcurrentHashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public int reduceStock(String commodityCode, int count) {
        log.info("reduce stock begin , commodityCode:{},count:{}", commodityCode, count);
        if (!storages.containsKey(commodityCode)) {
            throw new RuntimeException("commodityCode doesn't exists.");
        }
        StorageDto cur = storages.get(commodityCode);
        if (cur.getCount() < count) {
            throw new RuntimeException("insufficient stock");
        }
        cur.setCount(cur.getCount() - count);
        log.info("reduce stock end , commodityCode:{},afterCount:{}", commodityCode, cur.getCount());
        return cur.getCount();
    }

    @Override
    public int initStock(String commodityCode, int count) {
        if (!storages.containsKey(commodityCode)) {
            StorageDto item = new StorageDto();
            item.setCommodityCode(commodityCode);
            item.setId(idGenerator.getAndIncrement());
            item.setCount(count);
            storages.put(commodityCode, item);
        }
        StorageDto cur = storages.get(commodityCode);
        cur.setCount(count);
        return cur.getCount();
    }

    @Override
    public List<StorageDto> selectStockByCode(String commodityCode) {
        return storages.values().stream()
                .filter(d -> d.getCommodityCode().equals(commodityCode))
                .collect(Collectors.toList());

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initStock("order", 10);
    }
}
