package com.sm.finchley.loadbalance.support;


import com.sm.finchley.loadbalance.rule.EurekaMetadataAwareRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author lmwl
 */
@Configuration
@ConditionalOnClass(EurekaDiscoveryClient.class)
@AutoConfigureBefore(RibbonClientConfiguration.class)
@ConditionalOnProperty(value = "ribbon.filter.metadata.enabled", matchIfMissing = true)
@Slf4j
public class EurekaRibbonDiscoveryRuleAutoConfiguration {
    public EurekaRibbonDiscoveryRuleAutoConfiguration() {
        log.debug("自定义metaInfo信息指定路由以及权重负载均衡");
    }

    @Bean
    @ConditionalOnClass(EurekaDiscoveryClient.class)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public EurekaMetadataAwareRule metadataAwareRuleEureka() {
        EurekaMetadataAwareRule metadataAwareRule = new EurekaMetadataAwareRule();
        log.debug("metadataAwareRuleEureka");
        return metadataAwareRule;
    }

}