package com.app.cart.controller;

import com.app.cart.model.Cart;
import com.app.cart.model.CartItem;
import com.app.cart.service.CartService;
import com.app.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"", "/"})
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @GetMapping
  public Cart getCart(@AuthenticationPrincipal String userId) {
    return cartService.getCart(userId);
  }

  @PostMapping("/items")
  public void addItem(
      @AuthenticationPrincipal String userId, @RequestBody CartItem item) {
    cartService.addItem(userId, item);
  }

  @DeleteMapping("/items/{productId}")
  public void removeItem(
      @AuthenticationPrincipal String userId, @PathVariable String productId) {
    cartService.removeItem(userId, productId);
  }

  @DeleteMapping
  public void clearCart(@AuthenticationPrincipal String userId) {
    cartService.clearCart(userId);
  }
}
