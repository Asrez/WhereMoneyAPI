package com.asrez.wheremoney.api;

import com.asrez.wheremoney.api.config.JwtConfig;
import com.asrez.wheremoney.api.entity.User;
import com.asrez.wheremoney.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;


@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    UserRepository users;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtConfig jwtConfig;

    @Override
    public void run(String... args) throws Exception {
        String username = jwtConfig.getUsername();
        String password = jwtConfig.getPassword();
        if (!this.users.findByUsername(username).isEmpty())
            return;

        this.users.save(User.builder()
                .username(username)
                .password(this.passwordEncoder.encode(password))
                .name("Admin")
                .createdDate(LocalDateTime.now())
                .familyName("Admin")
                .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                .build()
        );
    }
}
