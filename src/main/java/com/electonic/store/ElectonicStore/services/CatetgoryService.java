package com.electonic.store.ElectonicStore.services;

import com.electonic.store.ElectonicStore.dtos.CategoryDto;
import com.electonic.store.ElectonicStore.dtos.PageableResponse;

import java.util.List;

public interface CatetgoryService {

    // create
    CategoryDto createCategory (CategoryDto categoryDto);

    // update
    CategoryDto updateCategory (CategoryDto categoryDto, String id);

    // delete
    void deleteCategory (String id);

    // Get All
    PageableResponse<CategoryDto> getAllCategory (int pageNumber, int pageSize, String sortBy, String sortDir);

    // Get single category details
    CategoryDto getSingleCategoryById (String id);

    // Search
    List<CategoryDto> searchByKeyword (String keyword);

    // Delete All
    void deleteAll ();
}
