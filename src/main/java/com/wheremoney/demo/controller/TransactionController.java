package com.wheremoney.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {


    @PostMapping
    public void addTransaction() {
        System.out.println();
    }
}
