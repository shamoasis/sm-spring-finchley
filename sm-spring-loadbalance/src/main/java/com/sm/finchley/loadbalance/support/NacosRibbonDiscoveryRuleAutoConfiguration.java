package com.sm.finchley.loadbalance.support;


import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient;
import com.sm.finchley.loadbalance.rule.NacosMetadataAwareRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author lmwl
 */
@Configuration
@AutoConfigureBefore(RibbonClientConfiguration.class)
@ConditionalOnClass(NacosDiscoveryClient.class)
@ConditionalOnProperty(value = "ribbon.filter.metadata.enabled", matchIfMissing = true)
@Slf4j
public class NacosRibbonDiscoveryRuleAutoConfiguration {
    public NacosRibbonDiscoveryRuleAutoConfiguration() {
        log.debug("NacosRibbonDiscoveryRuleAutoConfiguration");
    }

    @Bean
    @ConditionalOnMissingBean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public NacosMetadataAwareRule metadataAwareRuleNacos() {
        NacosMetadataAwareRule metadataAwareRule = new NacosMetadataAwareRule();
        log.debug("metadataAwareRuleNacos");
        return metadataAwareRule;
    }


}