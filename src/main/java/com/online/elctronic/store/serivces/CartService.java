package com.online.elctronic.store.serivces;

import com.online.elctronic.store.dtos.AddItemToCartRequest;
import com.online.elctronic.store.dtos.CartDto;

public interface CartService {

    //add item to cart
    // create cart for the user if not available
    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    // remove item from cart
    void removeItemFromCart(String userId, int cartItem);

    //remove all item from cart
    void clearCart(String userId);

    CartDto getCartByUser(String userId);


}
