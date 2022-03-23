package com.sm.sc.feign;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lmwl
 */
@FeignClient(name = "storage-service")
public interface StorageRestFeign extends StorageRestService {
}
