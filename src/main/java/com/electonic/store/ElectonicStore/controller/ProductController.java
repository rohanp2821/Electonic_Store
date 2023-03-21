package com.electonic.store.ElectonicStore.controller;

import com.electonic.store.ElectonicStore.dtos.ApiResponceMessage;
import com.electonic.store.ElectonicStore.dtos.PageableResponse;
import com.electonic.store.ElectonicStore.dtos.ProductDto;
import com.electonic.store.ElectonicStore.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    // Create
    @PostMapping
    public ResponseEntity<ProductDto> createProduct( @RequestBody ProductDto productDto) {
        ProductDto product = productService.createProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }
    // Update
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct (
            @Valid @RequestBody ProductDto productDto,
            @PathVariable ("id") String id) {

        ProductDto productDto1 = productService.updateProduct(productDto, id);
        return new ResponseEntity<>(productDto1, HttpStatus.OK);

    }
    @DeleteMapping
    public ResponseEntity<ApiResponceMessage> deleteProduct(String id) {

        productService.deleteProduct(id);
        ApiResponceMessage apiResponceMessage = ApiResponceMessage.builder()
                .message("Deleted product successfully !!")
                .success(true)
                .status(HttpStatus.OK).build();

        return new ResponseEntity<>(apiResponceMessage, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProduct (
            @RequestParam (value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam (value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam (value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam (value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {

        PageableResponse<ProductDto> allProduct = productService.getAllProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProduct,HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getproductById (@PathVariable ("id") String id) {
        ProductDto singleProduct = productService.getSingleProduct(id);
        return new ResponseEntity<>(singleProduct, HttpStatus.OK);

    }

    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> gerLiveProduct (
            @RequestParam (value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam (value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam (value = "sortBy", defaultValue = "asc", required = false) String sortBy,
            @RequestParam (value = "sortDir", defaultValue = "title", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> productDtoPageableResponse = productService.liveProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDtoPageableResponse, HttpStatus.OK);

    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageableResponse<ProductDto>> searchByKeyword(
            @PathVariable ("keyword") String keyword,
            @RequestParam (value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam (value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam (value = "sortBy", defaultValue = "asc", required = false) String sortBy,
            @RequestParam (value = "sortDir", defaultValue = "title", required = false) String sortDir
    )  {

        PageableResponse<ProductDto> productDtoPageableResponse = productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDtoPageableResponse, HttpStatus.OK);
    }


}
