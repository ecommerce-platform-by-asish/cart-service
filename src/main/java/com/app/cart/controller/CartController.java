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
  public ResponseEntity<ApiResponse<Cart>> getCart(@AuthenticationPrincipal String userId) {
    return ApiResponse.ok(cartService.getCart(userId)).toEntity();
  }

  @PostMapping("/items")
  public ResponseEntity<ApiResponse<Void>> addItem(
      @AuthenticationPrincipal String userId, @RequestBody CartItem item) {
    cartService.addItem(userId, item);
    return ApiResponse.ok((Void) null, "Item added to cart").toEntity();
  }

  @DeleteMapping("/items/{productId}")
  public ResponseEntity<ApiResponse<Void>> removeItem(
      @AuthenticationPrincipal String userId, @PathVariable String productId) {
    cartService.removeItem(userId, productId);
    return ApiResponse.ok((Void) null, "Item removed from cart").toEntity();
  }

  @DeleteMapping
  public ResponseEntity<ApiResponse<Void>> clearCart(@AuthenticationPrincipal String userId) {
    cartService.clearCart(userId);
    return ApiResponse.ok((Void) null, "Cart cleared").toEntity();
  }
}
