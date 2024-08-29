package com.online.elctronic.store.serivces;


import com.online.elctronic.store.dtos.CreateOrderRequest;
import com.online.elctronic.store.dtos.OrderDto;
import com.online.elctronic.store.dtos.OrderUpdateRequest;
import com.online.elctronic.store.dtos.PageableResponse;

import java.util.List;

public interface OrderService {


    //create order
    OrderDto createOrder(CreateOrderRequest orderDto);

    //remove order
    void removeOrder(String orderId);

    //get order for user
    List<OrderDto> getOrdersOfUser(String userId);

    //get orders
    PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

    //other method related order
    OrderDto updateOrder(String orderId, OrderUpdateRequest request);

}
