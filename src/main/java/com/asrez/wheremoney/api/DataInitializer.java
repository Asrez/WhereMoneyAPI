package com.asrez.wheremoney.api;

import com.asrez.wheremoney.api.config.JwtConfig;
import com.asrez.wheremoney.api.entity.Type;
import com.asrez.wheremoney.api.entity.User;
import com.asrez.wheremoney.api.repository.TypeRepository;
import com.asrez.wheremoney.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    UserRepository users;
    @Autowired
    TypeRepository typeRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtConfig jwtConfig;

    @Override
    public void run(String... args) throws Exception {
        addAdmin();
        addTypes();
    }

    private void addAdmin() {
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

    private void addTypes() {
        List<Type> types = new ArrayList<>();
        types.add(new Type().builder()
                .name("EDUCATION")
                .iconName("Icon_Education")
                .build());
        types.add(new Type().builder()
                .name("ENTERTAINMENT")
                .iconName("Icon_Entertainment")
                .build());
        types.add(new Type().builder()
                .name("OTHERS")
                .iconName("Icon_Others")
                .build());
        types.add(new Type().builder()
                .name("WORK")
                .iconName("Icon_Work")
                .build());
        types.add(new Type().builder()
                .name("CLOTHING")
                .iconName("Icon_Clothing")
                .build());

        typeRepository.saveAll(types);
    }
}
