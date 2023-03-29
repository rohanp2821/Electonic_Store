package com.electonic.store.ElectonicStore.services.impl;

import com.electonic.store.ElectonicStore.dtos.OrderDto;
import com.electonic.store.ElectonicStore.dtos.PageableResponse;
import com.electonic.store.ElectonicStore.entities.*;
import com.electonic.store.ElectonicStore.exception.BadApiRequest;
import com.electonic.store.ElectonicStore.exception.ResourceNotFoundException;
import com.electonic.store.ElectonicStore.helper.Helper;
import com.electonic.store.ElectonicStore.repositories.CartRepository;
import com.electonic.store.ElectonicStore.repositories.OrderItemRepository;
import com.electonic.store.ElectonicStore.repositories.OrderRepository;
import com.electonic.store.ElectonicStore.repositories.UserRepository;
import com.electonic.store.ElectonicStore.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ModelMapper mapper;
    private Date deliveryDate (Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,days);
        return calendar.getTime();
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto, String userId) {
        // fetch user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not find with this ID !!"));
        // fetch cart
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found with this user !!"));
        List<CartItem> cartItems = cart.getItems();
        if (cartItems.size()<=0) {
            throw new BadApiRequest("Invalid number of item in cart");
        }
        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingAddress(orderDto.getBillingAddress())
                .billingPhone(orderDto.getBillingPhone())
                .orderDate(new Date())
                .deliveryDate(deliveryDate(new Date(),5))
                .orderStatus(orderDto.getOrderStatus())
                .paymentStatus(orderDto.getPaymentStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user).build();

        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);

        List<OrderItems> orderItems = cartItems.stream().map(cartItem -> {
            OrderItems build = OrderItems.builder()
                    .product(cartItem.getProduct())
                    .order(order)
                    .totalPrice(cartItem.getTotalPrice()* cartItem.getProduct().getDiscountedPrice())
                    .quantity(cartItem.getQuantity()).build();
            orderAmount.set(orderAmount.get()+ build.getTotalPrice());
            return build;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        cart.getItems().clear();
        cartRepository.save(cart);

        Order saveOrder = orderRepository.save(order);
        return mapper.map(saveOrder,OrderDto.class);
    }

    @Override
    public PageableResponse<OrderDto> getOrderByUser(String userId, int pageNumber, int pageSize, String sortBy, String sortDir) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with this ID !!"));
        Sort sort = (sortDir.equalsIgnoreCase("dsc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending()) ;
        Pageable pageable = PageRequest.of(pageNumber-1,pageSize,sort);
        Page<Order> orders= orderRepository.findByUser(user, pageable);
        PageableResponse<OrderDto> pageableResponse = Helper.getPageableResponse(orders, OrderDto.class);

        return pageableResponse;
    }

    @Override
    public void removeOrder(String orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("order not found !!"));
        orderRepository.delete(order);

    }

    @Override
    public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("dsc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending()) ;
        Pageable pageable = PageRequest.of(pageNumber-1,pageSize,sort);
        Page<Order> orders = orderRepository.findAll(pageable);
        PageableResponse<OrderDto> pageableResponse = Helper.getPageableResponse(orders, OrderDto.class);
        return pageableResponse;
    }
}
