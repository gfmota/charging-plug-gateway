# Charging Plug Gateway

This project contains several implementations of data gateways of charging plug station on Europe. 
The data source is [Open Data Hub Mobility API](https://mobility.api.opendatahub.com/v2/apispec)
 and is retrieved via HTTP requests.

The master branch contains only code to retrieve data from the API
and domain classes. Each implementation is in a separate branch and you can
see more details of each on [Pull Requests](https://github.com/gfmota/charging-plug-gateway/pulls).

This is a Spring Boot application using:
* Feign Client to write simple HTTP clients
* Spring WebClient to write non-blocking concurrent HTTP clients
* Spring Boot Actuator to monitor the application
* RabbitMQ as asynchronous messaging broker