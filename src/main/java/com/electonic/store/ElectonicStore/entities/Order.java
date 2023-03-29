package com.electonic.store.ElectonicStore.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table (name = "orders")
public class Order {
    @Id
    private String orderId;
    // Pending, Dispatch, Delivered
    // we can use enum
    private String orderStatus;
    // Payment status
    // Not-Paid, Paid
    // False -> Not-Paid, True -> Paid
    private Boolean paymentStatus;
    private int orderAmount;
    @Column(length = 1000)
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderDate;
    private Date deliveryDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User user;
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItems> orderItems = new ArrayList<>();

}
