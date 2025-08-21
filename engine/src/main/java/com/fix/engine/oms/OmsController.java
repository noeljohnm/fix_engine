package com.fix.engine.oms;

import com.fix.engine.model.Order;
import com.fix.engine.service.OrderStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/oms")
public class OmsController {

    @Autowired private OrderStore store;

    @GetMapping("/orders")
    public Collection<Order> getOrders() { return store.getAllOrders(); }

    @PostMapping("/action")
    public String takeAction(@RequestParam Long orderId, @RequestParam String action) {
        Order order = store.getOrder(orderId);
        if (order == null) return "Order not found";

        order.setStatus(action.toUpperCase());
        store.updateOrder(order);
        return "Order " + orderId + " updated to " + action;
    }
}
