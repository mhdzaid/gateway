package com.api.gateway.config;

import com.api.gateway.filter.UrlPathHashLoadBalancerClientFilter;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
// Value for the LoadBalancerClient should be the application id in the application.yml file
@LoadBalancerClient(value = "TO_BE_ADDED",configuration = UrlPathHashLoadBalancerClientConfiguration.class)
public class UrlPathHashConfiguration {

    /**
     * Registering custom UrlPathHashLoadBalancer
     * @param clientFactory
     * @param properties
     * @return
     */
    @Bean
    public UrlPathHashLoadBalancerClientFilter ipHashLoadBalancerClientFilter(
            LoadBalancerClientFactory clientFactory, GatewayLoadBalancerProperties properties) {
        return new UrlPathHashLoadBalancerClientFilter(clientFactory, properties);
    }
}