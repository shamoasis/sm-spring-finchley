package com.sm.finchley.loadbalance.predicate;

import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.Server;
import org.springframework.lang.Nullable;


/**
 * @author lmwl
 */
public abstract class DiscoveryEnabledPredicate extends AbstractServerPredicate {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean apply(@Nullable PredicateKey input) {
        return input != null && apply(input.getServer());
    }

    /**
     * @param server the discovered server
     * @return whether the server matches the predicate
     */
    protected abstract boolean apply(Server server);


}