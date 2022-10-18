### Gateway
* Acts as the API gateway as well as loadBalancer for [User](https://github.com/mhdzaid/User) as well [Location-Writer](https://github.com/mhdzaid/location-writer).
* The strategy used is RoundRobbin which is already given for Spring Cloud.
* Another custom implementation for server stickiness is also written which works similar to URL hash load balancing but dependent on the number of instances.
* You can add your custom strategy in the `application.yaml` file
```
spring:
    gateway:
      routes:
        - id: location-writer
          uri: lb://location-writer-client
          predicates:
```
Using `lb://` you can use the default strategy which is Round Robbin, to use the custom URL hash loadBalancing replace it with `urlhash`;
```
spring:
    gateway:
      routes:
        - id: location-writer
          uri: urlhash://location-writer-client
          predicates:
```