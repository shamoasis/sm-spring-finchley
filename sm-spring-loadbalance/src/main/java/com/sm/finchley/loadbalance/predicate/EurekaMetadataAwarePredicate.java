package com.sm.finchley.loadbalance.predicate;


import com.google.common.base.Optional;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import com.sm.finchley.loadbalance.robin.EurekaWeightRoundRobin;
import com.sm.finchley.loadbalance.robin.WeightRoundRobin;
import com.sm.finchley.loadbalance.support.RibbonFilterContext;
import com.sm.finchley.loadbalance.support.RibbonFilterContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lmwl
 */
@Slf4j
public class EurekaMetadataAwarePredicate extends DiscoveryEnabledPredicate {

    WeightRoundRobin weightRoundRobin = new EurekaWeightRoundRobin();

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean apply(Server server) {

        final RibbonFilterContext context = RibbonFilterContextHolder.getCurrentContext();
        final Set<Map.Entry<String, String>> attributes = Collections.unmodifiableSet(context.getAttributes().entrySet());
        final Map<String, String> metadata = ((DiscoveryEnabledServer) server).getInstanceInfo().getMetadata();
        return metadata.entrySet().containsAll(attributes);
    }

    @Override
    public Optional<Server> chooseRoundRobinAfterFiltering(List<Server> servers, Object loadBalancerKey) {
        List<Server> eligible = getEligibleServers(servers, loadBalancerKey);
        if (eligible.size() == 0) {
            if (ObjectUtils.isEmpty(servers)) {
                return Optional.absent();
            } else {
                log.debug("choose default servers");
                return Optional.of(weightRoundRobin.choose(servers));
            }
        }
        return Optional.of(weightRoundRobin.choose(eligible));
    }
}