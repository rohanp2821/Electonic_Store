package com.electonic.store.ElectonicStore.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "product_ID")
    private String productId;
    @Column(name = "product_title", length = 1000)
    private String title;
    @Column(name = "product_description", length = 10000)
    private String description;
    private int quantity;
    private int price;
    private int discountedPrice;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImage;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;


}
