package com.app.cart.controller;

import com.app.cart.model.Cart;
import com.app.cart.model.CartItem;
import com.app.cart.service.CartService;
import com.app.common.context.UserContext;
import lombok.RequiredArgsConstructor;
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
  public Cart getCart() {
    return cartService.getCart(UserContext.USER_ID.get());
  }

  @PostMapping("/items")
  public void addItem(@RequestBody CartItem item) {
    cartService.addItem(UserContext.USER_ID.get(), item);
  }

  @DeleteMapping("/items/{productId}")
  public void removeItem(@PathVariable String productId) {
    cartService.removeItem(UserContext.USER_ID.get(), productId);
  }

  @DeleteMapping
  public void clearCart() {
    cartService.clearCart(UserContext.USER_ID.get());
  }
}
