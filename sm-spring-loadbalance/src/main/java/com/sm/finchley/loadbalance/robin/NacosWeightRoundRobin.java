package com.sm.finchley.loadbalance.robin;


import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class NacosWeightRoundRobin extends WeightRoundRobin {

    @Override
    public Map<String, String> getServerMetadata(Server server) {
        return ((NacosServer) server).getMetadata();
    }


}
