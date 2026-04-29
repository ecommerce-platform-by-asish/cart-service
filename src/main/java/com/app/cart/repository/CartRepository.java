package com.app.cart.repository;

import com.app.cart.entity.CartEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, UUID> {
  Optional<CartEntity> findByUserId(String userId);
}
