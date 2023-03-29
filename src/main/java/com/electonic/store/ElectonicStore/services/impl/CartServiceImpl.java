package com.electonic.store.ElectonicStore.services.impl;

import com.electonic.store.ElectonicStore.dtos.AddItemToCartRequest;
import com.electonic.store.ElectonicStore.dtos.CartDto;
import com.electonic.store.ElectonicStore.entities.Cart;
import com.electonic.store.ElectonicStore.entities.CartItem;
import com.electonic.store.ElectonicStore.entities.Product;
import com.electonic.store.ElectonicStore.entities.User;
import com.electonic.store.ElectonicStore.exception.BadApiRequest;
import com.electonic.store.ElectonicStore.exception.ResourceNotFoundException;
import com.electonic.store.ElectonicStore.repositories.CartItemsRepository;
import com.electonic.store.ElectonicStore.repositories.CartRepository;
import com.electonic.store.ElectonicStore.repositories.ProductRepository;
import com.electonic.store.ElectonicStore.repositories.UserRepository;
import com.electonic.store.ElectonicStore.services.CartService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Autowired
    private ModelMapper mapper;

    private Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

        String productId = request.getProductId();
        int quantity = request.getQuantity();

        if (quantity <= 0) {
            throw new BadApiRequest("Requested quantity is not valid !!");
        }

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with this ID !!"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with this ID !!"));
        Cart cart= null;

        try {
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException e) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();
        items = items.stream().map(item -> {

            if (item.getProduct().getProductId().equals(productId)) {
                // items already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity*product.getDiscountedPrice());
                logger.info("Total Quantity : {}", item.getQuantity());
                logger.info("Total Price : {}", item.getTotalPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());
//        cart.setItems(updatedItems);
        if (!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product).build();
            boolean add = cart.getItems().add(cartItem);
            logger.info("Total Quantity : {}", cartItem.getQuantity());
            logger.info("Total Price : {}", cartItem.getTotalPrice());
            logger.info("Status : {}", add);

        }
        cart.setUser(user);
        Cart save = cartRepository.save(cart);
        logger.info("Save entity : {}", save);

        return mapper.map(save,CartDto.class);

    }

    @Override
    public void deleteItemFromCart(String userId, int cartItemId) {
        CartItem cartItem = cartItemsRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart not found with this id !!"));
        cartItemsRepository.delete(cartItem);
    }

    @Override
    public void deleteAllItemsFromCart(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with this ID !!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found !!"));
        cart.getItems().clear();

        cartRepository.save(cart);

    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with this id !!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found !!"));
        return mapper.map(cart, CartDto.class);
    }
}
