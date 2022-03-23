package com.sm.finchley.gateway.config;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import reactor.core.publisher.Mono;

/**
 * @author lmwl
 */
//@Service
@AllArgsConstructor
public class DynamicRouteService implements ApplicationEventPublisherAware {
    public static final String SUCCESS = "success";
    private final RouteDefinitionWriter routeDefinitionWriter;
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public String add(RouteDefinition definition) {
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        notifyChanged();
        return SUCCESS;
    }

    public String update(RouteDefinition definition) {
        try {
            this.routeDefinitionWriter.delete(Mono.just(definition.getId())).subscribe();

        } catch (Exception e) {
            return String.format("update fail,routeId:%s", definition.getId());
        }
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            notifyChanged();
            return SUCCESS;

        } catch (Exception e) {
            return "update roure fail";
        }
    }

    public String delete(String id) {
        try {
            this.routeDefinitionWriter.delete(Mono.just(id));
            notifyChanged();
            return SUCCESS;
        } catch (Exception e) {
            return "delete fail";
        }
    }

    private void notifyChanged() {
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }
}
