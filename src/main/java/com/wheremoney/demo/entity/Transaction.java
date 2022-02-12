package com.wheremoney.demo.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "user_id")
    private long userId;
    private Long price;
    private String source;
    private String destination;
    @Column(name = "account_number")
    private String accountNumber;
    private String description;
    @Column(name = "is_income")
    private Boolean isIncome;

}
