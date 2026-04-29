package com.app.cart.mapper;

import com.app.cart.entity.CartEntity;
import com.app.cart.entity.CartItemEntity;
import com.app.cart.model.Cart;
import com.app.cart.model.CartItem;
import com.app.common.mapper.BaseMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapperConfig.class)
public interface CartMapper {

  Cart toDto(CartEntity entity);

  CartEntity toEntity(Cart dto);

  CartItem toItemDto(CartItemEntity entity);

  CartItemEntity toItemEntity(CartItem dto);
}
