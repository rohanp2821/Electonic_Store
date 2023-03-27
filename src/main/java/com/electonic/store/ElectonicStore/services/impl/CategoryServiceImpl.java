package com.electonic.store.ElectonicStore.services.impl;

import com.electonic.store.ElectonicStore.dtos.CategoryDto;
import com.electonic.store.ElectonicStore.dtos.PageableResponse;
import com.electonic.store.ElectonicStore.entities.Category;
import com.electonic.store.ElectonicStore.exception.ResourceNotFoundException;
import com.electonic.store.ElectonicStore.helper.Helper;
import com.electonic.store.ElectonicStore.repositories.CategoryRepository;
import com.electonic.store.ElectonicStore.services.CatetgoryService;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CatetgoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;
    @Value("$(category.cover.image-path)")
    private String imagePath;


    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
//    private CategoryDto entityToDto (Category category) {
//
//        CategoryDto categoryDto = CategoryDto.builder()
//                .categoryId(category.getCategoryId())
//                .title(category.getTitle())
//                .description(category.getDescription())
//                .coverImage(category.getCoverImage()).build();
//        return categoryDto;
//    }
//
//    private Category dtoToEntity (CategoryDto categoryDto) {
//
//        Category category = Category.builder()
//                .categoryId(categoryDto.getCategoryId())
//                .title(categoryDto.getTitle())
//                .description(categoryDto.getDescription())
//                .coverImage(categoryDto.getCoverImage()).build();
//        return category;
//    }



    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
//        Category category = dtoToEntity(categoryDto);
        Category category = mapper.map(categoryDto, Category.class);

        Category save = categoryRepository.save(category);


        CategoryDto map = mapper.map(save, CategoryDto.class);
        return map;
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String id) {
        //get category of given id
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id !!"));
        //update category details
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory = categoryRepository.save(category);
        return mapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void deleteCategory(String id) {
        //get category of given id
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id !!"));

        String fullPath = imagePath + category.getCoverImage();

        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException exception) {
            logger.info("No such user image found by this id");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        categoryRepository.delete(category);

    }

    @Override
    public PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);
        return pageableResponse;
    }

    @Override
    public CategoryDto getSingleCategoryById(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id !!"));
        return mapper.map(category, CategoryDto.class);
    }

    @Override
    public  List<CategoryDto> searchByKeyword(String keyword) {

        List<Category> containing = categoryRepository.findByTitleContaining(keyword);
        List<CategoryDto> collect = containing.stream().map((category) -> mapper.map(category, CategoryDto.class)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public void deleteAll() {
        categoryRepository.deleteAll();
    }


}
