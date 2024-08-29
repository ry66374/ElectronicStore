package com.online.elctronic.store.repositories;

import com.online.elctronic.store.entities.Cart;
import com.online.elctronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {

    Optional<Cart> findByUser(User user);
}
