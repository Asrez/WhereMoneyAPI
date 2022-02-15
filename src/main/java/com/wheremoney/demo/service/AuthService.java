package com.wheremoney.demo.service;

import com.wheremoney.demo.config.SessionConfig;
import com.wheremoney.demo.entity.Session;
import com.wheremoney.demo.entity.User;
import com.wheremoney.demo.exception.ApiRequestException;
import com.wheremoney.demo.repository.SessionRepository;
import com.wheremoney.demo.repository.UserRepository;
import com.wheremoney.demo.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SessionConfig sessionConfig;

    @Autowired
    public AuthService(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, SessionRepository sessionRepository, UserRepository userRepository, SessionConfig sessionConfig) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.sessionConfig = sessionConfig;
    }

    public Map<Object, Object> signIn(String user, String password) {
        try {
            String username = user;
            if (isBanned(user))
                throw new ApiRequestException("Banned until " + getBannedDate(user), "Banned");

            var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            System.out.println(isBanned(user));
            String token = jwtTokenProvider.createToken(authentication);
            addSession(user, token, true);
            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return model;
        } catch (AuthenticationException e) {
            addSession(user, null, false);
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public Map<Object, Object> currentUser(UserDetails userDetails) {
        Map<Object, Object> model = new HashMap<>();
        model.put("username", userDetails.getUsername());
        model.put("roles", userDetails.getAuthorities()
                .stream()
                .map(a -> ((GrantedAuthority) a).getAuthority())
                .collect(toList())
        );

        return model;
    }

    private void addSession(String user, String token, boolean success) {
        Session session = new Session();
        Optional<User> userOptional = userRepository.findByUsername(user);
        if (userOptional.isPresent())
            session.setUserId(userOptional.get().getId());
        else
            session.setUserId(-1L);

        session.setCreatedDate(LocalDateTime.now());
        session.setToken(token);
        session.setLoggedIn(success);
        sessionRepository.save(session);
    }

    private boolean isBanned(String user) {
        Optional<User> userOptional = userRepository.findByUsername(user);
        if (userOptional.isPresent()) {
            int loginAttempts = sessionRepository.failedSignInAttempts(userOptional.get().getId());
            if (loginAttempts > 1 && loginAttempts % sessionConfig.getMaxLoginAttempts() == 0) {
                Session session = sessionRepository.lastUserSession(userOptional.get().getId()).get();
                if (session.isLoggedIn())
                    return false;
                if (LocalDateTime.now().isBefore(session.getCreatedDate().plusHours(sessionConfig.getBanForInHours())))
                    return true;
            }
        }

        return false;
    }

    private LocalDateTime getBannedDate(String user) {
        Optional<User> userOptional = userRepository.findByUsername(user);
        return sessionRepository.lastUserSession(userOptional.get().getId()).get().getCreatedDate().plusHours(sessionConfig.getBanForInHours());
    }
}
