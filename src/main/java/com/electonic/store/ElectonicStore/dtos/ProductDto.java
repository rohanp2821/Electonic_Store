package com.electonic.store.ElectonicStore.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private String productId;
    @NotBlank(message = "Please enter title of the product !!")
    @Size(min = 3, message = "Title must be have minimum 3 characters !!")
    private String title;
    @NotBlank(message = "Please enter title of the product !!")
    @Size(min = 5, message = "Description must be have minimum 3 characters !!")
    private String description;
    private int quantity;
    @NotBlank(message = "Please enter title of the product !!")
    private int price;
    @NotBlank(message = "Please enter title of the product !!")
    private int discountedPrice;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImage;

}
