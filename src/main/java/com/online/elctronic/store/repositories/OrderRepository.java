package com.online.elctronic.store.repositories;

import com.online.elctronic.store.entities.Order;
import com.online.elctronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUser(User user);

}
