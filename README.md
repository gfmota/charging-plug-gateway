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
* RabbitMQ as asynchronous messaging broker
* Caffeine to cache methods
* Spring Boot Actuator to provide metrics data
* Prometheus to store metrics data
* Grafana to display metrics data

## Monitoring

To monitor the application you may run the docker-compose file 
```
docker-compose up
```
And then you can access Prometheus at `localhost:9090` and make custom queries with the data from Actuator.
Or you can access Grafana at `localhost:3000`, credentials `admin`/`admin`, to use the dashboards with graphs and displays.

The Grafana dashboard used to track what this experiment intends is defined at `./grafana-dash.json`