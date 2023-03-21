package com.electonic.store.ElectonicStore.controller;


import com.electonic.store.ElectonicStore.dtos.*;
import com.electonic.store.ElectonicStore.repositories.CategoryRepository;
import com.electonic.store.ElectonicStore.services.CatetgoryService;
import com.electonic.store.ElectonicStore.services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CatetgoryService catetgoryService;

    @Autowired
    private FileService fileService;

    @Value("${category.cover.image-path}")
    private String imagePath;

    // Create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory (@Valid @RequestBody CategoryDto categoryDto) {

        CategoryDto category = catetgoryService.createCategory(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);

    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory (
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable ("id") String id
    )   {

        CategoryDto categoryDto1 = catetgoryService.updateCategory(categoryDto, id);
        return new ResponseEntity<>(categoryDto1, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponceMessage> deleteCategory(@PathVariable ("id") String id) {

        catetgoryService.deleteCategory(id);
        ApiResponceMessage apiResponceMessage = ApiResponceMessage.builder().message("Deleted category Successfully!!").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(apiResponceMessage, HttpStatus.OK);
    }
    // Get All
    @GetMapping
    public ResponseEntity<PageableResponse> getAllCategories (
            @RequestParam (value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam (value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam (value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam (value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {

        PageableResponse<CategoryDto> allCategory = catetgoryService.getAllCategory(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allCategory, HttpStatus.OK);
    }
    // Get Single
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable ("id") String id) {
        CategoryDto singleCategoryById = catetgoryService.getSingleCategoryById(id);
        return new ResponseEntity<>(singleCategoryById, HttpStatus.OK);
    }

    @GetMapping("/keyword/{keyword}")
    public ResponseEntity<List<CategoryDto>> findByKeyword (@PathVariable ("keyword") String keyword) {
        List<CategoryDto> categoryDtos = catetgoryService.searchByKeyword(keyword);
        return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponceMessage> deleteAll () {
        catetgoryService.deleteAll();
        ApiResponceMessage build = ApiResponceMessage.builder()
                .message("All Categories deleted !!")
                .success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(build,HttpStatus.OK);

    }
    @PostMapping("/image-upload/{id}")
    public ResponseEntity<ImageResponse> uploadImage(
            @PathVariable ("id") String id,
            @RequestParam ("image")MultipartFile image
            ) throws IOException {

        String uploadFile = fileService.uploadFile(image, imagePath);
        CategoryDto singleCategoryById = catetgoryService.getSingleCategoryById(id);
        singleCategoryById.setCoverImage(uploadFile);
        catetgoryService.updateCategory(singleCategoryById,id);

        ImageResponse response = ImageResponse.builder()
                .imageName(uploadFile)
                .message("Image upload Successfully !!")
                .success(true)
                .status(HttpStatus.OK).build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/image/{id}")
    public void serveUserImage(@PathVariable String id, HttpServletResponse response) throws IOException {
        CategoryDto singleCategoryById = catetgoryService.getSingleCategoryById(id);
        InputStream resource = fileService.getResource(imagePath, singleCategoryById.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }
}
