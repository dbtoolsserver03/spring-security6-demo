package com.example.demo.security;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;


@Data
@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auth_name", nullable = false)
    private String name;

    @Column(name = "auth_enabled", nullable = false)
    private Boolean enabled;

    @Override
    public String getAuthority() {
        return this.name;
    }
}
