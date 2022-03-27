package com.sm.finchley.loadbalance.rule;


import com.sm.finchley.loadbalance.predicate.EurekaMetadataAwarePredicate;
import com.sm.finchley.loadbalance.robin.EurekaWeightRoundRobin;

/**
 * @author lmwl
 */
public class EurekaMetadataAwareRule extends AbsctractDiscoveryEnabledRule {

    public EurekaMetadataAwareRule() {
        super(new EurekaMetadataAwarePredicate(new EurekaWeightRoundRobin()));
    }


}