package com.example.demo;

import com.example.demo.domain.user.User;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@Slf4j
@ExtendWith(SpringExtension.class)
@DataJpaTest
class JpaTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    void saveUser() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = User
                .builder()
                .id("demo")
                .password(encoder.encode("1234"))
                .passwordExpiredDt(LocalDate.now().plusDays(User.PASSWORD_EXPIRED_TERM_DAYS))
                .name("tom")
                .age(20)
                .build();

        log.info(user.toString());

        userRepository.save(user);

        boolean saved = userRepository.existsById(user.getId());

        if (saved) {
            userRepository.findById("demo")
                    .ifPresent(saved_user -> {
                        log.info(saved_user.getUsername() + " successfully saved!");
                    });
        } else {
            log.info("fail to save...");
        }
    }
}
