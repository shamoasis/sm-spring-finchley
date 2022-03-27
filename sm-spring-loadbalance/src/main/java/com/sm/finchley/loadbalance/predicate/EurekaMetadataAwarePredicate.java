package com.sm.finchley.loadbalance.predicate;


import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import com.sm.finchley.loadbalance.robin.WeightRoundRobin;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author lmwl
 */
@Slf4j
public class EurekaMetadataAwarePredicate extends DiscoveryEnabledPredicate {
    public EurekaMetadataAwarePredicate(WeightRoundRobin weightRoundRobin) {
        super(weightRoundRobin);
    }

    ///**
    // * {@inheritDoc}
    // */
    //@Override
    //protected boolean apply(Server server, HttpHeaders headers) {
    //    final Map<String, String> metadata = getServerMetadata(server);
    //    String version = metadata.get(WeightRoundRobin.VERSION);
    //    String target = headers.getFirst(WeightRoundRobin.VERSION);
    //
    //    return apply(target, version);
    //}

    @Override
    public Map<String, String> getServerMetadata(Server server) {
        return ((DiscoveryEnabledServer) server).getInstanceInfo().getMetadata();
    }
}