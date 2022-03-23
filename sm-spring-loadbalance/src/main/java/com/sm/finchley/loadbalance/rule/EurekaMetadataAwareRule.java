package com.sm.finchley.loadbalance.rule;


import com.sm.finchley.loadbalance.predicate.EurekaMetadataAwarePredicate;

/**
 * @author lmwl
 */
public class EurekaMetadataAwareRule extends DiscoveryEnabledRule {

    public EurekaMetadataAwareRule() {
        super(new EurekaMetadataAwarePredicate());
    }


}