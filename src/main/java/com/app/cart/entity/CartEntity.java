package com.app.cart.entity;

import com.app.common.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "carts")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CartEntity extends BaseEntity {

  @Column(name = "user_id", unique = true, nullable = false)
  private String userId;

  @Builder.Default
  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CartItemEntity> items = new ArrayList<>();

  public void addItem(CartItemEntity item) {
    items.add(item);
    item.setCart(this);
  }

  public void removeItem(CartItemEntity item) {
    items.remove(item);
    item.setCart(null);
  }

  public void clear() {
    items.clear();
  }
}
