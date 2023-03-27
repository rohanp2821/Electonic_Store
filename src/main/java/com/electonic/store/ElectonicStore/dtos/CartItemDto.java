package com.electonic.store.ElectonicStore.dtos;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private int cartId;

    private ProductDto product;

    private int quantity;
    private int totalPrice;

}
