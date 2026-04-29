package com.app.cart.service;

import com.app.cart.entity.CartEntity;
import com.app.cart.entity.CartItemEntity;
import com.app.cart.mapper.CartMapper;
import com.app.cart.model.Cart;
import com.app.cart.model.CartItem;
import com.app.cart.repository.CartRepository;
import com.app.pricing.grpc.BatchPriceRequest;
import com.app.pricing.grpc.BatchPriceResponse;
import com.app.pricing.grpc.PricingServiceGrpc;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

  private final CartRepository cartRepository;
  private final CartMapper cartMapper;
  private final PricingServiceGrpc.PricingServiceBlockingStub pricingStub;

  @Transactional(readOnly = true)
  public Cart getCart(String userId) {
    CartEntity cartEntity =
        cartRepository
            .findByUserId(userId)
            .orElseGet(() -> CartEntity.builder().userId(userId).items(new ArrayList<>()).build());

    Cart cartDto = cartMapper.toDto(cartEntity);

    // Enrich with prices if there are items
    if (!cartDto.getItems().isEmpty()) {
      enrichWithPrices(cartDto);
    }

    return cartDto;
  }

  private void enrichWithPrices(Cart cart) {
    try {
      List<String> productIds =
          cart.getItems().stream().map(CartItem::getProductId).collect(Collectors.toList());

      BatchPriceRequest request =
          BatchPriceRequest.newBuilder().addAllProductIds(productIds).build();

      BatchPriceResponse response = pricingStub.getBatchPrices(request);
      Map<String, Double> prices = response.getPricesMap();

      cart.getItems()
          .forEach(
              item -> {
                Double price = prices.get(item.getProductId());
                if (price != null) {
                  item.setPrice(price);
                }
              });

      double total =
          cart.getItems().stream()
              .filter(item -> item.getPrice() != null)
              .mapToDouble(item -> item.getPrice() * item.getQuantity())
              .sum();

      cart.setTotalPrice(total);
      log.info("Enriched cart for user {} with total price {}", cart.getUserId(), total);

    } catch (Exception e) {
      log.error("Failed to enrich cart with prices via gRPC", e);
    }
  }

  @Transactional
  public void addItem(String userId, CartItem item) {
    CartEntity cartEntity =
        cartRepository
            .findByUserId(userId)
            .orElseGet(
                () ->
                    cartRepository.save(
                        CartEntity.builder().userId(userId).items(new ArrayList<>()).build()));

    Optional<CartItemEntity> existingItem =
        cartEntity.getItems().stream()
            .filter(i -> i.getProductId().equals(item.getProductId()))
            .findFirst();

    if (existingItem.isPresent()) {
      existingItem.get().setQuantity(item.getQuantity());
    } else {
      cartEntity.addItem(cartMapper.toItemEntity(item));
    }

    cartRepository.save(cartEntity);
    log.info("Added/Updated item {} in cart for user {}", item.getProductId(), userId);
  }

  @Transactional
  public void removeItem(String userId, String productId) {
    cartRepository
        .findByUserId(userId)
        .ifPresent(
            cart -> {
              cart.getItems().removeIf(item -> item.getProductId().equals(productId));
              cartRepository.save(cart);
              log.info("Removed item {} from cart for user {}", productId, userId);
            });
  }

  @Transactional
  public void clearCart(String userId) {
    cartRepository
        .findByUserId(userId)
        .ifPresent(
            cart -> {
              cart.clear();
              cartRepository.save(cart);
              log.info("Cleared cart for user {}", userId);
            });
  }
}
