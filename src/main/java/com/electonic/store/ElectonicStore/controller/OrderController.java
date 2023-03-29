package com.electonic.store.ElectonicStore.controller;

import com.electonic.store.ElectonicStore.dtos.ApiResponceMessage;
import com.electonic.store.ElectonicStore.dtos.OrderDto;
import com.electonic.store.ElectonicStore.dtos.PageableResponse;
import com.electonic.store.ElectonicStore.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{userId}")
    public ResponseEntity<OrderDto> createOrder (
            @RequestBody OrderDto orderDto,
            @PathVariable ("userId") String userId
    ) {
        OrderDto order = orderService.createOrder(orderDto, userId);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<PageableResponse<OrderDto>> getOrderByUser (
            @PathVariable ("userId") String userId,
            @RequestParam (value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam (value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam (value = "sortBy", defaultValue = "orderId", required = false) String sortBy,
            @RequestParam (value = "sortDir", defaultValue = "asc", required = false) String sortDir
            ) {
        PageableResponse<OrderDto> orderByUser = orderService.getOrderByUser(userId, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(orderByUser, HttpStatus.OK);
    }
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponceMessage> removeOrder (@PathVariable ("orderId") String orderId) {
        orderService.removeOrder(orderId);
        ApiResponceMessage apiResponceMessage = ApiResponceMessage.builder()
                .message("Order Removed Successfully !!")
                .success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(apiResponceMessage, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrder (
            @RequestParam (value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam (value = "pageSize", defaultValue =  "5", required = false) int pageSize,
            @RequestParam (value = "sortBy", defaultValue = "orderId", required = false) String sortBy,
            @RequestParam (value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<OrderDto> allOrders = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allOrders, HttpStatus.OK);
    }
}
