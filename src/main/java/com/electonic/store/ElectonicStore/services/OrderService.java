package com.electonic.store.ElectonicStore.services;

import com.electonic.store.ElectonicStore.dtos.OrderDto;
import com.electonic.store.ElectonicStore.dtos.PageableResponse;

public interface OrderService {

    // Create order
    OrderDto createOrder(OrderDto orderDto, String userId);
    // Get order of user
    PageableResponse<OrderDto> getOrderByUser (String userId, int pageNumber, int pageSize, String sortBy, String sortDir);
    // Remove order
    void removeOrder (String orderId);
    // Get orders
    PageableResponse<OrderDto> getAllOrders (int pageNumber, int pageSize, String sortBy, String sortDir);
    // update order(Payment Status, Order Amount(YES/NO), Delivery Date)
}
