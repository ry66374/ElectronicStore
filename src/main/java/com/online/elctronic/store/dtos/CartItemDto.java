package com.online.elctronic.store.dtos;

import com.online.elctronic.store.entities.Cart;
import com.online.elctronic.store.entities.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {


    private int cartItemId;

    private ProductDto productDto;

    private  int quantity;
    private  int totalPrice;

}
