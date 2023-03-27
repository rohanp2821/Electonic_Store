package com.electonic.store.ElectonicStore.controller;

import com.electonic.store.ElectonicStore.dtos.AddItemToCartRequest;
import com.electonic.store.ElectonicStore.dtos.ApiResponceMessage;
import com.electonic.store.ElectonicStore.dtos.CartDto;
import com.electonic.store.ElectonicStore.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemTOCart (
            @PathVariable ("userId") String userId,
            @RequestBody AddItemToCartRequest request){

        CartDto cartDto = cartService.addItemToCart(userId, request);

        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/item/{cartItemId}")
    public ResponseEntity<ApiResponceMessage> deleteItemFromCart (
            @PathVariable ("userId") String userId,
            @PathVariable ("cartItemId") int cartItemId
    ) {

        cartService.deleteItemFromCart(userId,cartItemId);
        ApiResponceMessage apiResponceMessage = ApiResponceMessage.builder()
                .message("Deleted item Successfully !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(apiResponceMessage, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponceMessage> deleteAllItemFromCart (@PathVariable ("userId") String userId) {

        cartService.deleteAllItemsFromCart(userId);
        ApiResponceMessage apiResponceMessage = ApiResponceMessage.builder()
                .message("Deleted All items !!")
                .success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(apiResponceMessage,HttpStatus.OK);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUser (@PathVariable ("userId") String userId) {
        CartDto cartByUser = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartByUser, HttpStatus.OK);

    }



}
