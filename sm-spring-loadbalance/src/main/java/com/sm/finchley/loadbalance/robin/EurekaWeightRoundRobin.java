package com.sm.finchley.loadbalance.robin;


import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class EurekaWeightRoundRobin extends WeightRoundRobin {

    @Override
    public Map<String, String> getServerMetadata(Server server) {
        return ((DiscoveryEnabledServer) server).getInstanceInfo().getMetadata();
    }
}
