package com.electonic.store.ElectonicStore.services;

import com.electonic.store.ElectonicStore.dtos.ApiResponceMessage;
import com.electonic.store.ElectonicStore.dtos.PageableResponse;
import com.electonic.store.ElectonicStore.dtos.ProductDto;
import com.electonic.store.ElectonicStore.entities.Category;
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

    // Create product with category ID
    ProductDto createProductWithCategory (ProductDto productDto, String categoryId);


    // Update Category with reference of product ID
    ProductDto updateCategoryOfProduct (String productId, String categoryId);

    // Get All category product
    PageableResponse<ProductDto> getAllCategoryProduct(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);

    // Other Method


}
