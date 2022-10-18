package com.api.gateway.balancer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class UrlPathHashLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    public String serviceId;
    public ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    public UrlPathHashLoadBalancer(
            ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
            String serviceId) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    /**
     * Selecting which server should handle the request
     * @param request - an input request
     * @return
     */
    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        if (serviceInstanceListSupplierProvider != null) {
            ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                    .getIfAvailable(NoopServiceInstanceListSupplier::new);
            return supplier.get().next().map((instances) -> getInstanceResponse(request, instances));
        }
        return null;
    }


    /**
     * Custom load balancer implementation for server stickiness using source path
     * @param request
     * @param instances
     * @return
     */
    private Response<ServiceInstance> getInstanceResponse(
            Request request,
            List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            log.warn("No servers available for service: " + this.serviceId);
            return new EmptyResponse();
        }

        String sourcePath = (String) request.getContext();
        int hash = Math.abs(sourcePath.hashCode());
        int pos = hash % instances.size();
        ServiceInstance instance = instances.get(pos);
        return new DefaultResponse(instance);
    }
}
