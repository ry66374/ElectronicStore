//package com.online.elctronic.store.serivces.impl;
//
//import com.online.elctronic.store.dtos.AddItemToCartRequest;
//import com.online.elctronic.store.dtos.CartDto;
//import com.online.elctronic.store.entities.Cart;
//import com.online.elctronic.store.entities.CartItem;
//import com.online.elctronic.store.entities.Product;
//import com.online.elctronic.store.entities.User;
//import com.online.elctronic.store.exceptions.ResourceNotFoundException;
//import com.online.elctronic.store.repositories.CartRepository;
//import com.online.elctronic.store.repositories.ProductRepository;
//import com.online.elctronic.store.repositories.UserRepository;
//import com.online.elctronic.store.serivces.CartService;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Date;
//import java.util.List;
//import java.util.NoSuchElementException;
//
//public class CartServiceImpl implements CartService {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private CartRepository cartRepository;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    @Override
//    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
//
//        int quantity = request.getQuantity();
//        String productId = request.getProductId();
//
//        //fetch the product
//        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product is not found"));
//        //Fetch the User from the db
//        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User is not found"));
//        Cart cart = null;
//
//        try{
//            cart = cartRepository.findByUser(user).get();
//        }catch (NoSuchElementException e){
//            cart = new Cart();
//            cart.setCreatedAt(new Date());
//        }
//
//        //perform cart operations
//     //   List<CartItem> items = cart.getItems();
//
//        CartItem cartItem = CartItem.builder()
//                .quantity(quantity)
//                .totalPrice(quantity*product.getPrice())
//                .cart(cart).product(product)
//                .build();
//
//        cart.getItems().add(cartItem);
//        cart.setUser(user);
//        Cart updatedCart = cartRepository.save(cart);
//        return modelMapper.map(updatedCart, CartDto.class);
//    }
//
//    @Override
//    public void removeItemFromCart(String userId, int cartItem) {
//
//    }
//
//    @Override
//    public void clearCart(String userId) {
//
//    }
//}

package com.online.elctronic.store.serivces.impl;

import com.online.elctronic.store.dtos.AddItemToCartRequest;
import com.online.elctronic.store.dtos.CartDto;
import com.online.elctronic.store.entities.Cart;
import com.online.elctronic.store.entities.CartItem;
import com.online.elctronic.store.entities.Product;
import com.online.elctronic.store.entities.User;
import com.online.elctronic.store.exceptions.BadApiRequest;
import com.online.elctronic.store.exceptions.ResourceNotFoundException;
import com.online.elctronic.store.repositories.CartItemRepository;
import com.online.elctronic.store.repositories.CartRepository;
import com.online.elctronic.store.repositories.ProductRepository;
import com.online.elctronic.store.repositories.UserRepository;
import com.online.elctronic.store.serivces.CartService;
import org.modelmapper.ModelMapper;
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
    private ModelMapper mapper;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

        int quantity = request.getQuantity();
        String productId = request.getProductId();

        if (quantity <= 0) {
            throw new BadApiRequest("Requested quantity is not valid !!");
        }

        //fetch the product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found in database !!"));
        //fetch the user from db
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));

        Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException e) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }

        //perform cart operations
        //if cart items already present; then update
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();
        items = items.stream().map(item -> {

            if (item.getProduct().getId().equals(productId)) {
                //item already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

//        cart.setItems(updatedItems);

        //create items
        if (!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);
        }


        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return mapper.map(updatedCart, CartDto.class);


    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        //conditions
        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFoundException("Cart Item not found !!"));
        cartItemRepository.delete(cartItem1);

    }

    @Override
    public void clearCart(String userId) {
        //fetch the user from db
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found !!"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found !!"));

        return mapper.map(cart, CartDto.class);
    }
}

