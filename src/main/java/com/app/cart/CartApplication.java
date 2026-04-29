package com.app.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.grpc.client.ImportGrpcClients;

@SpringBootApplication
@ImportGrpcClients
public class CartApplication {
  public static void main(String[] args) {
    SpringApplication.run(CartApplication.class, args);
  }
}
