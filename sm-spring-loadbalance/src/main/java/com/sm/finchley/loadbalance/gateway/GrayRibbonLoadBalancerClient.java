package com.sm.finchley.loadbalance.gateway;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.ribbon.*;

/**
 * @author lmwl
 */
public class GrayRibbonLoadBalancerClient extends RibbonLoadBalancerClient {
    private SpringClientFactory clientFactory;

    public GrayRibbonLoadBalancerClient(SpringClientFactory clientFactory) {
        super(clientFactory);
        this.clientFactory = clientFactory;
    }

    @Override
    public ServiceInstance choose(String serviceId) {
        return this.choose(serviceId, (Object) null);
    }

    public ServiceInstance choose(String serviceId, Object hint) {
        Server server = this.getServer(this.getLoadBalancer(serviceId), hint);
        return server == null ? null : new RibbonLoadBalancerClient.RibbonServer(serviceId, server, this.isSecure(server, serviceId), this.serverIntrospector(serviceId).getMetadata(server));
    }

    protected Server getServer(ILoadBalancer loadBalancer, Object hint) {
        return loadBalancer == null ? null : loadBalancer.chooseServer(hint != null ? hint : "default");
    }

    private boolean isSecure(Server server, String serviceId) {
        IClientConfig config = this.clientFactory.getClientConfig(serviceId);
        ServerIntrospector serverIntrospector = this.serverIntrospector(serviceId);
        return RibbonUtils.isSecure(config, serverIntrospector, server);
    }

    private ServerIntrospector serverIntrospector(String serviceId) {
        ServerIntrospector serverIntrospector = (ServerIntrospector) this.clientFactory.getInstance(serviceId, ServerIntrospector.class);
        if (serverIntrospector == null) {
            serverIntrospector = new DefaultServerIntrospector();
        }

        return (ServerIntrospector) serverIntrospector;
    }
}
