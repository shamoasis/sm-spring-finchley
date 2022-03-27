package com.sm.finchley.loadbalance.predicate;


import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.netflix.loadbalancer.Server;
import com.sm.finchley.loadbalance.robin.WeightRoundRobin;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author lmwl
 */
@Slf4j
public class NacosMetadataAwarePredicate extends DiscoveryEnabledPredicate {
    public NacosMetadataAwarePredicate(WeightRoundRobin weightRoundRobin) {
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

    //@Override
    //public Optional<Server> chooseRoundRobinAfterFiltering(List<Server> servers, Object loadBalancerKey) {
    //    List<Server> eligible = getEligibleServers(servers, loadBalancerKey);
    //    if (eligible.size() == 0) {
    //        if (ObjectUtils.isEmpty(servers)) {
    //            return Optional.absent();
    //        } else {
    //            log.debug("choose default servers");
    //            Optional.of(weightRoundRobin.choose(servers));
    //        }
    //    }
    //    return Optional.of(weightRoundRobin.choose(eligible));
    //}

    @Override
    protected Map<String, String> getServerMetadata(Server server) {
        return ((NacosServer) server).getMetadata();
    }
}