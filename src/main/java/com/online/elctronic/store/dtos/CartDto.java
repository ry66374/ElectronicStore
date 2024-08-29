package com.online.elctronic.store.dtos;

import com.online.elctronic.store.entities.CartItem;
import com.online.elctronic.store.entities.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto{

    private String cartId;

    private Date createdAt;

    private UserDto userDto;

    private List<CartItemDto> items = new ArrayList<>();
}
