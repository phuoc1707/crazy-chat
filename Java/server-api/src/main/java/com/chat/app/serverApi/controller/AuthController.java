package com.chat.app.serverApi.controller;

import com.chat.app.serverApi.dto.AuthRequest;
import com.chat.app.serverApi.dto.AuthResponse;
import com.chat.app.serverApi.dto.LoginResponse;
import com.chat.app.serverApi.entity.Role;
import com.chat.app.serverApi.entity.User;
import com.chat.app.serverApi.exceptionHandler.ApiResponse;
import com.chat.app.serverApi.service.UserService;
import com.chat.app.serverApi.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    final UserService userService;

    @Autowired
    final PasswordEncoder passwordEncoder;

    @Autowired
    final AuthenticationManager authenticationManager;

    @Autowired
    final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userService.findByUserName(user.getUserName()) != null) {
            ApiResponse<User> errorResponse = ApiResponse.error("Error: Username is already taken!", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        User userRegister= User.builder()
                .avatar(user.getAvatar())
                .userName(user.getUserName())
                .password(passwordEncoder.encode(user.getPassword()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(LocalDateTime.now())
                .conversations(null)
                .role(Role.USER)
                .build();
        try {
            return new ResponseEntity<>(ApiResponse.success(userService.createUser(userRegister)), HttpStatus.CREATED);
        } catch (RuntimeException exception){
            ApiResponse<User> errorResponse = ApiResponse.error(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
            return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication.getName());

        User user = userService.findByUserName(authentication.getName());

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                user.getUserName(),
                user.getAvatar(),
                user.getLastName(),
                user.getFirstName()
        );

        return ResponseEntity.ok(new LoginResponse(jwt, userInfo));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String oldToken) {
        if (oldToken.startsWith("Bearer ")) {
            oldToken = oldToken.substring(7);
        }
        String newToken = jwtUtils.refreshToken(oldToken);
        return ResponseEntity.ok(new AuthResponse(newToken));
    }
}
