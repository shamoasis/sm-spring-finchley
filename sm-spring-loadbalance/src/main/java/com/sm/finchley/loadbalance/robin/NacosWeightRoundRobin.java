package com.sm.finchley.loadbalance.robin;


import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class NacosWeightRoundRobin extends WeightRoundRobin {

    @Override
    public com.netflix.loadbalancer.Server choose(List<? extends com.netflix.loadbalancer.Server> servers) {
        return super.getServer(init(servers));
    }

    public synchronized List<Server> init(List<? extends com.netflix.loadbalancer.Server> servers) {
        List<Server> serverList = servers.stream().map(e -> {
            Map<String, String> metadata = ((NacosServer) e).getMetadata();
            String weight = metadata.get("weight");
            log.debug("port:{},weight:{}", e.getPort(), ((DiscoveryEnabledServer) e).getInstanceInfo().getMetadata().get("weight"));
            return new Server(e, StringUtils.isBlank(weight) ? 1 : Integer.valueOf(weight));
        }).collect(Collectors.toList());

        boolean update = super.servers.containsAll(serverList);

        if (!update) {
            maxWeight = greatestWeight(serverList);
            gcdWeight = greatestCommonDivisor(serverList);
            serverCount = serverList.size();
            super.servers = serverList;
        }
        return serverList;
    }


}
