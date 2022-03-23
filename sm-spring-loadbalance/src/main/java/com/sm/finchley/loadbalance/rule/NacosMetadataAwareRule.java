package com.sm.finchley.loadbalance.rule;


import com.sm.finchley.loadbalance.predicate.NacosMetadataAwarePredicate;

/**
 * @author lmwl
 */
public class NacosMetadataAwareRule extends DiscoveryEnabledRule {

    public NacosMetadataAwareRule() {
        super(new NacosMetadataAwarePredicate());
    }
}