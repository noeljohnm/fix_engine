package com.fix.engine.service;

import com.fix.engine.model.Order;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderStore {
    private ConcurrentHashMap<Long, Order> orders = new ConcurrentHashMap<>();
    private AtomicLong idGenerator = new AtomicLong(1);

    public Order addOrder(Order order) {
        long id = idGenerator.getAndIncrement();
        order.setId(id);
        orders.put(id, order);
        return order;
    }

    public Collection<Order> getAllOrders() {
        return orders.values();
    }

    public Order getOrder(Long id) {
        return orders.get(id);
    }

    public void updateOrder(Order order) {
        orders.put(order.getId(), order);
    }
}