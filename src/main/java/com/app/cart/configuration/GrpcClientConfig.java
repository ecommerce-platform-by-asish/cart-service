package com.app.cart.configuration;

import com.app.pricing.grpc.PricingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

  @Value("${spring.grpc.client.channels.pricingService.address}")
  private String pricingServiceAddress;

  @Bean
  public PricingServiceGrpc.PricingServiceBlockingStub pricingServiceBlockingStub() {
    // Extract host and port from address (static://localhost:9090)
    String address = pricingServiceAddress.replace("static://", "");
    String host = address.split(":")[0];
    int port = Integer.parseInt(address.split(":")[1]);

    ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext()
        .build();

    return PricingServiceGrpc.newBlockingStub(channel);
  }
}
