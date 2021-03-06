package com.sm.finchley.loadbalance.rule;


import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.AvailabilityPredicate;
import com.netflix.loadbalancer.CompositePredicate;
import com.netflix.loadbalancer.PredicateBasedRule;
import com.sm.finchley.loadbalance.predicate.DiscoveryEnabledPredicate;
import org.springframework.util.Assert;

public abstract class AbsctractDiscoveryEnabledRule extends PredicateBasedRule {

    private final DiscoveryEnabledPredicate predicate;


    public AbsctractDiscoveryEnabledRule(DiscoveryEnabledPredicate discoveryEnabledPredicate) {
        Assert.notNull(discoveryEnabledPredicate, "Parameter 'discoveryEnabledPredicate' can't be null");
//        this.predicate = createCompositePredicate(discoveryEnabledPredicate, new AvailabilityPredicate(this, null));
        this.predicate = discoveryEnabledPredicate;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractServerPredicate getPredicate() {
        return predicate;
    }

    /**
     * Creates the composite predicate with fallback strategies.
     *
     * @param discoveryEnabledPredicate the discovery service predicate
     * @param availabilityPredicate     the availability predicate
     * @return the composite predicate
     */
    private CompositePredicate createCompositePredicate(DiscoveryEnabledPredicate discoveryEnabledPredicate, AvailabilityPredicate availabilityPredicate) {
        return CompositePredicate.withPredicates(discoveryEnabledPredicate, availabilityPredicate)
                .build();
    }


}