package com.electonic.store.ElectonicStore.controller;

import com.electonic.store.ElectonicStore.dtos.*;
import com.electonic.store.ElectonicStore.services.FileService;
import com.electonic.store.ElectonicStore.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Value("${product.image-path}")
    private String imagePath;
    @Autowired
    private FileService fileService;
    private Logger logger = LoggerFactory.getLogger(ProductController.class);
    // Create
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
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
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponceMessage> deleteProduct(@PathVariable String id) {

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
            @RequestParam (value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam (value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> productDtoPageableResponse = productService.liveProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDtoPageableResponse, HttpStatus.OK);

    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageableResponse<ProductDto>> searchByKeyword(
            @PathVariable ("keyword") String keyword,
            @RequestParam (value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam (value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam (value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam (value = "sortDir", defaultValue = "asc", required = false) String sortDir
    )  {

        PageableResponse<ProductDto> productDtoPageableResponse = productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDtoPageableResponse, HttpStatus.OK);
    }

    @PostMapping("/image/{id}")
    public ResponseEntity<ImageResponse> uploadImage(
            @PathVariable ("id") String id,
            @RequestParam ("image") MultipartFile file
            ) throws IOException {
        String uploadFile = fileService.uploadFile(file, imagePath);
        logger.info("Upload path : {}", uploadFile);
        ProductDto singleProduct = productService.getSingleProduct(id);
        singleProduct.setProductImage(uploadFile);
        productService.updateProduct(singleProduct, id);

        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(uploadFile)
                .message("Uploaded image Successfully !!")
                .success(true)
                .status(HttpStatus.ACCEPTED).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.OK);

    }

    @GetMapping(value = "/image/{id}")
    public void serveUserImage(@PathVariable String id, HttpServletResponse response) throws IOException {
        ProductDto singleProduct = productService.getSingleProduct(id);
        InputStream resource = fileService.getResource(imagePath, singleProduct.getProductImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }



}
