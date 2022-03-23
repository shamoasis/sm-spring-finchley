package com.sm.finchley.loadbalance.predicate;


import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;
import com.netflix.loadbalancer.Server;
import com.sm.finchley.loadbalance.robin.NacosWeightRoundRobin;
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
public class NacosMetadataAwarePredicate extends DiscoveryEnabledPredicate {

    WeightRoundRobin weightRoundRobin = new NacosWeightRoundRobin();

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean apply(Server server) {

//        Mono.subscriberContext()
//                .map(ctx -> (String)ctx.get(VERSION))
//                .subscribe(e->{
//                    System.out.println(e);
//                });
//

        System.out.println(Thread.currentThread().getName());
        final RibbonFilterContext context = RibbonFilterContextHolder.getCurrentContext();
        final Set<Map.Entry<String, String>> attributes = Collections.unmodifiableSet(context.getAttributes().entrySet());
        final Map<String, String> metadata = ((NacosServer) server).getMetadata();

        boolean b = metadata.entrySet().containsAll(attributes);
        if (!b) {
            log.debug("不满足条件, 需要的meta信息:{},存在的meta信息:{}", JSON.toJSONString(attributes), JSON.toJSONString(metadata));
        }
        return b;
    }

    @Override
    public Optional<Server> chooseRoundRobinAfterFiltering(List<Server> servers, Object loadBalancerKey) {
        List<Server> eligible = getEligibleServers(servers, loadBalancerKey);
        if (eligible.size() == 0) {
            if (ObjectUtils.isEmpty(servers)) {
                return Optional.absent();
            } else {
                log.debug("choose default servers");
                Optional.of(weightRoundRobin.choose(servers));
            }
        }
        return Optional.of(weightRoundRobin.choose(eligible));
    }


}