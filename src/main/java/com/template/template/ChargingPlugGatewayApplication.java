package com.template.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class  ChargingPlugGatewayApplication {
  public static void main(final String[] args) {
    SpringApplication.run(ChargingPlugGatewayApplication.class, args);
  }
}
