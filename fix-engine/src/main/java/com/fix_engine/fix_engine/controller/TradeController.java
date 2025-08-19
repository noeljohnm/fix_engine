package com.fix_engine.fix_engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fix_engine.fix_engine.client.FixClient;

import quickfix.SessionNotFound;

@RestController
@RequestMapping("/trade")
public class TradeController {

    @Autowired
    private FixClient fixClient;

    @PostMapping("/order")
    public String sendOrder() throws SessionNotFound {
        fixClient.sendOrder();
        return "Order sent!";
    }
}