package com.electonic.store.ElectonicStore.services;

import com.electonic.store.ElectonicStore.dtos.ApiResponceMessage;
import com.electonic.store.ElectonicStore.dtos.PageableResponse;
import com.electonic.store.ElectonicStore.dtos.ProductDto;
import org.springframework.data.annotation.CreatedBy;

import java.util.List;

public interface ProductService {


    // Create
    ProductDto createProduct (ProductDto productDto);

    // Update
    ProductDto updateProduct(ProductDto productDto , String productId);

    // Delete
    void deleteProduct(String productId);

    // Get All
    PageableResponse<ProductDto> getAllProduct (int pageNumber, int pageSize, String sortBy, String sortDir);

    // Get Single
    ProductDto getSingleProduct(String productId);

    // Get All : Live
    PageableResponse<ProductDto> liveProduct(int pageNumber, int pageSize, String sortBy, String sortDir);

    // Search Product
    PageableResponse<ProductDto> searchProductByKeyword (String keyword, int pageNumber, int pageSize, String sortBy, String sortDir);
    // Other Method


}
