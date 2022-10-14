package com.api.gateway.config;

import com.api.gateway.filter.UrlPathHashLoadBalancerClientFilter;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@LoadBalancerClient(value = "location-client",configuration = UrlPathHashLoadBalancerClientConfiguration.class)
public class UrlPathHashConfiguration {

    @Bean
    public UrlPathHashLoadBalancerClientFilter ipHashLoadBalancerClientFilter(
            LoadBalancerClientFactory clientFactory, GatewayLoadBalancerProperties properties) {
        return new UrlPathHashLoadBalancerClientFilter(clientFactory, properties);
    }
}