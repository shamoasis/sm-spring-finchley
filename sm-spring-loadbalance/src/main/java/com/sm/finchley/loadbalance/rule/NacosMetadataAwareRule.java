package com.sm.finchley.loadbalance.rule;


import com.sm.finchley.loadbalance.predicate.NacosMetadataAwarePredicate;
import com.sm.finchley.loadbalance.robin.NacosWeightRoundRobin;

/**
 * @author lmwl
 */
public class NacosMetadataAwareRule extends AbsctractDiscoveryEnabledRule {

    public NacosMetadataAwareRule() {
        super(new NacosMetadataAwarePredicate(new NacosWeightRoundRobin()));
    }
}