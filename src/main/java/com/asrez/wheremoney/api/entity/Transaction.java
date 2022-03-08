package com.asrez.wheremoney.api.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @Column(name = "calculate_in_monthly")
    private Boolean calculateInMonthly;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;
    @OneToOne
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

}
