package com.example.demo.service;

import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserAuthority;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.UserAuthorityRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.user.Authority;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private PasswordEncoder passwordEncoder;

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public Optional<User> getUser(String id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void signUp(User user) {
        LocalDateTime now = LocalDateTime.now();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPasswordExpiredDt(now.toLocalDate().plusDays(User.PASSWORD_EXPIRED_TERM_DAYS));
        user.setExpired(false);
        user.setLocked(false);
        user.setCreatedDt(now);

        userRepository.save(user);
        provideAuthorityForUser(user);
    }

    private void provideAuthorityForUser(User user) {
        Authority authority = authorityRepository.getUserAuthority();
        UserAuthority userAuthority = UserAuthority.builder()
                .userId(user.getId())
                .authority(authority)
                .createdDt(LocalDateTime.now())
                .build();

        userAuthorityRepository.save(userAuthority);
    }
}
