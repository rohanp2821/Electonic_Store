package com.electonic.store.ElectonicStore.services.impl;

import com.electonic.store.ElectonicStore.dtos.ApiResponceMessage;
import com.electonic.store.ElectonicStore.dtos.PageableResponse;
import com.electonic.store.ElectonicStore.dtos.ProductDto;
import com.electonic.store.ElectonicStore.entities.Category;
import com.electonic.store.ElectonicStore.entities.Product;
import com.electonic.store.ElectonicStore.exception.ResourceNotFoundException;
import com.electonic.store.ElectonicStore.helper.Helper;
import com.electonic.store.ElectonicStore.repositories.CategoryRepository;
import com.electonic.store.ElectonicStore.repositories.ProductRepository;
import com.electonic.store.ElectonicStore.services.ProductService;
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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ModelMapper mapper;

    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Value("${product.image-path}")
    private String imagePath;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        // Generate Random ID
        String productId = UUID.randomUUID().toString();
        // Generate Current Date and time
        Date currentDate = new Date();
        productDto.setAddedDate(currentDate);
        productDto.setProductId(productId);

        Product product = mapper.map(productDto, Product.class);
        Product save = productRepository.save(product);
        return mapper.map(save, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product Not Found with this id !!"));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setProductImage(productDto.getProductImage());

        Product save = productRepository.save(product);

        return mapper.map(save, ProductDto.class);
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with this ID !!"));

        String fullPath = imagePath + product.getProductImage();

        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException exception) {
            logger.info("No such user image found by this id");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        productRepository.delete(product);
    }

    @Override
    public PageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
        Page<Product> all = productRepository.findAll(pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(all, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public ProductDto getSingleProduct(String productId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product is not found with this id !!"));

        return mapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> liveProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))  ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber-1,pageSize,sort);
        Page<Product> byLiveTrue = productRepository.findByLiveTrue(pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(byLiveTrue, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> searchProductByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDir) {
      Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
      Pageable pageable = PageRequest.of(pageNumber-1,pageSize, sort);
      Page<Product> titleContaining = productRepository.findByTitleContaining(pageable, keyword);
      PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(titleContaining, ProductDto.class);
      return pageableResponse;
    }

    @Override
    public ProductDto createProductWithCategory(ProductDto productDto, String categoryId) {

        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        Product product = mapper.map(productDto, Product.class);
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not Found with this ID !!"));
        product.setCategory(category);
        Product save = productRepository.save(product);

        return mapper.map(save, ProductDto.class);

    }

    @Override
    public ProductDto updateCategoryOfProduct(String productId, String categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with this ID !!"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with this ID"));
        product.setCategory(category);
        Product save = productRepository.save(product);
        return mapper.map(save,ProductDto.class);

    }

    @Override
    public PageableResponse<ProductDto> getAllCategoryProduct(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with this ID !!"));
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending()) ;
        Pageable pageable = PageRequest.of(pageNumber-1,pageSize, sort);
        Page<Product> byCategory = productRepository.findByCategory(pageable,category);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(byCategory, ProductDto.class);
        return pageableResponse;
    }

}
