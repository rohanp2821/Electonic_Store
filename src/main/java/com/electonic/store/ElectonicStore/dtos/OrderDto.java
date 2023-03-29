package com.electonic.store.ElectonicStore.dtos;
import com.electonic.store.ElectonicStore.entities.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private String orderId;
    private String orderStatus="PENDING";
    // Payment status
    // Not-Paid, Paid
    // False -> Not-Paid, True -> Paid
    private Boolean paymentStatus= false;
    private int orderAmount;
    @NotBlank(message = "Billing address is required !!")
    private String billingAddress;
    @NotBlank(message = "Phone number is required for Billing !!")
    private String billingPhone;
    @NotBlank(message = "Name is required for Billing !!")
    private String billingName;
    private Date orderDate= new Date();
    private Date deliveryDate;
    private User user;
    private List<OrderItemDto> orderItems = new ArrayList<>();


}
