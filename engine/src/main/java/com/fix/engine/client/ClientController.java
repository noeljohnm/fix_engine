package com.fix.engine.client;

import com.fix.engine.model.Order;
import com.fix.engine.service.OrderStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import quickfix.SessionNotFound;

import java.util.Collection;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired private FixClient client;
    @Autowired private OrderStore store;

    @PostMapping("/order")
    public String sendOrder(@RequestBody Order order) throws SessionNotFound {
        order.setStatus("NEW");
        store.addOrder(order);
        client.sendOrder(order);
        return "Order submitted with ID: " + order.getId();
    }

    @GetMapping("/orders")
    public Collection<Order> getOrders() {
        return store.getAllOrders();
    }
}