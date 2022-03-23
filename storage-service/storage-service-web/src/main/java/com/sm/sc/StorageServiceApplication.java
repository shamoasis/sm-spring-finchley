package com.sm.sc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liumeng
 * @project storage-service
 * @description
 */

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
public class StorageServiceApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(StorageServiceApplication.class);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}
