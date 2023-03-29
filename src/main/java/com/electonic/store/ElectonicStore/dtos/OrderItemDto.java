package com.electonic.store.ElectonicStore.dtos;

import com.electonic.store.ElectonicStore.entities.Order;
import com.electonic.store.ElectonicStore.entities.Product;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {

    private int orderItemId;
    private int quantity;
    private int totalPrice;
    private ProductDto product;

}
