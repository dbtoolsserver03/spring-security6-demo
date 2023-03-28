package com.example.demo.security.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Getter @Setter
@RequiredArgsConstructor
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
