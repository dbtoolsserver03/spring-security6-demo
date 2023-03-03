package com.example.demo.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "demo_user")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_account_id", nullable = false, unique = true)
    private String accountId;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "user_age")
    private int age;
}
