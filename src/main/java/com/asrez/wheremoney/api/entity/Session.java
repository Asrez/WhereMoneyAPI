package com.asrez.wheremoney.api.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "user_id")
    private long userId;
    private String token;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "logged_in")
    private boolean loggedIn;

}
