package com.sm.finchley.loadbalance.predicate;

import com.google.common.base.Optional;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.Server;
import com.sm.finchley.loadbalance.robin.WeightRoundRobin;
import com.sm.finchley.loadbalance.support.RibbonFilterContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.sm.finchley.loadbalance.support.DefaultRibbonFilterContext.VERSION;


/**
 * @author lmwl
 */
@Slf4j
public abstract class DiscoveryEnabledPredicate extends AbstractServerPredicate {
    WeightRoundRobin weightRoundRobin;

    public DiscoveryEnabledPredicate(WeightRoundRobin weightRoundRobin) {
        this.weightRoundRobin = weightRoundRobin;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean apply(@Nullable PredicateKey input) {
        if (input == null) {
            return false;
        }
        if (input.getLoadBalancerKey() != null) {
            return input.getLoadBalancerKey() instanceof HttpHeaders && apply(input.getServer(), (HttpHeaders) input.getLoadBalancerKey());
        } else {
            HttpHeaders headers = new HttpHeaders();
            String curVersion = RibbonFilterContextHolder.getCurrentContext().get(VERSION);
            if (StringUtils.hasText(curVersion)) {
                headers.add(VERSION, curVersion);
            }
            return apply(input.getServer(), headers);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected boolean apply(Server server, HttpHeaders headers) {
        final Map<String, String> metadata = getServerMetadata(server);
        String version = metadata.get(VERSION);
        String target = headers.getFirst(VERSION);

        // 判断请求中是否有版本,
        if (StringUtils.hasText(target)) {
            //请求中有版本则返回版本相等或者默认的
            return target.equals(version) || StringUtils.isEmpty(version);
        } else {
            //请求中没有版本则返回版本无版本标记的
            return StringUtils.isEmpty(version);
        }
    }

    @Override
    public Optional<Server> chooseRoundRobinAfterFiltering(List<Server> servers, Object loadBalancerKey) {
        List<Server> eligible = getEligibleServers(servers, loadBalancerKey);
        if (eligible.size() == 0) {
            if (ObjectUtils.isEmpty(servers)) {
                return Optional.absent();
            } else {
                List<Server> defaultServers = getDefaultServers(servers, loadBalancerKey);
                if (defaultServers.isEmpty()) {
                    return Optional.absent();
                }
                log.debug("choose default servers");
                return Optional.of(weightRoundRobin.choose(defaultServers));
            }
        }
        return Optional.of(weightRoundRobin.choose(eligible));
    }

    protected List<Server> getDefaultServers(List<Server> servers, Object loadBalancerKey) {
        List<Server> res = new ArrayList<>();
        String target;
        if (loadBalancerKey != null && loadBalancerKey instanceof HttpHeaders) {
            target = ((HttpHeaders) loadBalancerKey).getFirst(VERSION);
        } else {
            target = RibbonFilterContextHolder.getCurrentContext().get(VERSION);
        }
        for (Server server : servers) {
            final Map<String, String> metadata = getServerMetadata(server);
            String version = metadata.get(VERSION);

            if (StringUtils.isEmpty(target) && StringUtils.isEmpty(version)) {
                res.add(server);
            } else if (StringUtils.hasText(target) && StringUtils.isEmpty(version)) {
                res.add(server);
            }
        }
        return res;
    }

    protected abstract Map<String, String> getServerMetadata(Server server);

    ///**
    // * Returns whether the specific {@link com.sm.finchley.loadbalance.robin.WeightRoundRobin.WeightServer} matches this predicate.
    // * @param server the discovered server
    // * @param headers 请求头
    // * @return whether the server matches the predicate
    // */
    //protected abstract boolean apply(Server server, HttpHeaders headers);


}