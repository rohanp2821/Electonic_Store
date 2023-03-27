package com.electonic.store.ElectonicStore.dtos;


import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {


    private String cartId;
    private Date createdAt;
    private UserDto user;
    private int totalPrice;
    private int totalQuantity;
    // Mapping cart items
    private List<CartItemDto> items = new ArrayList<>();


}
