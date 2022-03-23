package com.sm.finchley.loadbalance.robin;


import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class EurekaWeightRoundRobin extends WeightRoundRobin {

    @Override
    public com.netflix.loadbalancer.Server choose(List<? extends com.netflix.loadbalancer.Server> servers) {
        return super.getServer(init(servers));
    }


    //EurekaNotificationServerListUpdater
    public synchronized List<Server> init(List<? extends com.netflix.loadbalancer.Server> servers) {

        List<Server> serverList = servers.stream().map(e -> {
            Map<String, String> metadata = ((DiscoveryEnabledServer) e).getInstanceInfo().getMetadata();
            String weight = metadata.get("weight");
            log.debug("port:{},weight:{}", e.getPort(), ((DiscoveryEnabledServer) e).getInstanceInfo().getMetadata().get("weight"));
            return new Server(e, StringUtils.isEmpty(weight) ? 1 : Integer.valueOf(weight));
        }).collect(Collectors.toList());

        boolean update = !super.servers.containsAll(serverList);

        if (update) {
            maxWeight = greatestWeight(serverList);
            gcdWeight = greatestCommonDivisor(serverList);
            serverCount = serverList.size();
            super.servers = serverList;
        }
        return serverList;
    }


}
