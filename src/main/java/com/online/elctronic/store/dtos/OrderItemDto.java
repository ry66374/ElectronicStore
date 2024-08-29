package com.online.elctronic.store.dtos;

import com.online.elctronic.store.entities.Order;
import com.online.elctronic.store.entities.Product;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderItemDto {

    private int orderItemId;
    private int quantity;
    private int totalPrice;
    private ProductDto product;
    private Order order;
}
