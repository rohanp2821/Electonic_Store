package com.electonic.store.ElectonicStore.services;

import com.electonic.store.ElectonicStore.dtos.AddItemToCartRequest;
import com.electonic.store.ElectonicStore.dtos.CartDto;

public interface CartService {

    // Add items to cart
    // case1: When is user not available : we create cart and add product
    // case2: When user is available add items to the cart

    CartDto addItemToCart (String userId, AddItemToCartRequest request);

    // delete item from cart
    void deleteItemFromCart (String userId, int cartItems);

    // delete all items from cart
    void deleteAllItemsFromCart (String userId);

    //fetch cart
    CartDto getCartByUser (String userId);
}
