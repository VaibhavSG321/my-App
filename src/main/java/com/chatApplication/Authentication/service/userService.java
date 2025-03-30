package com.chatApplication.Authentication.service;

import com.chatApplication.Authentication.exceptions.ApiResponse;
import com.chatApplication.Authentication.exceptions.InvalidCredentialsException;
import com.chatApplication.Authentication.exceptions.UserAlreadyExistsException;
import com.chatApplication.Authentication.exceptions.UserNotFoundException;
import com.chatApplication.Authentication.model.Users;
import com.chatApplication.Authentication.repo.UserRepository;
import com.chatApplication.Authentication.repo.userRedisRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class userService {

    @Autowired
    private jwtService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepository repo;

    @Autowired
    private userRedisRepository userRedisRepository;

    private static final Logger logger= LoggerFactory.getLogger(userService.class);
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Transactional
    public ResponseEntity<ApiResponse> register(Users user) {
        if (repo.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username is already taken, please try with another one");
        }

        String uniqueID = UUID.randomUUID().toString();
        String role = user.getRole().toUpperCase();
        user.setRole(role);
        user.setUniqueID(uniqueID);
        user.setPassword(encoder.encode(user.getPassword()));

        Users savedUser = repo.save(user);

        ApiResponse response = new ApiResponse(true, "User registered successfully", savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    public ResponseEntity<ApiResponse> verify(Users user) {
        try {

            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            Users authenticatedUser = userRedisRepository.getUserFromRedis(user.getUsername());
            if (authenticatedUser == null) {
                authenticatedUser = repo.findByUsername(user.getUsername());
                if (authenticatedUser == null) {
                    throw new UserNotFoundException("User not found");
                }

                Users userToCache = new Users(authenticatedUser.getUsername(), authenticatedUser.getRole(), authenticatedUser.getUniqueID());
                userRedisRepository.saveUserToRedis(userToCache);
            }

            if ("ROLE_ADMIN".equals(authenticatedUser.getRole())) {
                logger.info("Hello Admin");
            } else {
                logger.info("Hello User");
            }

            String jwtToken = jwtService.generateToken(authenticatedUser.getUsername(), authenticatedUser.getUniqueID());
            String sessionId = UUID.randomUUID().toString();

            userRedisRepository.saveSessionToRedis(authenticatedUser.getUsername(), sessionId);

            return ResponseEntity.ok(new ApiResponse(true, "Login successful", jwtToken));

        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid username or password");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred"));
        }
    }

    public void logout(String token) {
        if(jwtService.isTokenExpired(token)){
            throw new IllegalArgumentException("Invalid or expired token");
        }
        long expirationTime = jwtService.getRemainingTokenTime(token);
        String username = jwtService.extractUserName(token);
        userRedisRepository.blacklistToken(token, expirationTime);
        userRedisRepository.deleteSessionFromRedis(username);
    }

    public List<Users> retrive() {
        return repo.findAll();
    }
}
