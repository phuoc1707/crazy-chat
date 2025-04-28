package com.chat.app.serverApi.controller;


import com.chat.app.serverApi.entity.User;
import com.chat.app.serverApi.exceptionHandler.ApiResponse;
import com.chat.app.serverApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:8080/*")
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> findById(@PathVariable Long id){
        try {
            User user = userService.getById(id);
            return  new ResponseEntity<>(ApiResponse.success(user), HttpStatus.OK);
        } catch (Exception exception){
            ApiResponse<User> errorResponse = ApiResponse.error(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
            return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<User>> addUser(@RequestBody User user){
        try {
            user.setCreatedAt(LocalDateTime.now());
            return new ResponseEntity<>(ApiResponse.success(userService.createUser(user)), HttpStatus.CREATED);
        } catch (RuntimeException exception){
            ApiResponse<User> errorResponse = ApiResponse.error(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
            return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @RequestBody User user){
        try {
            User existingUser = userService.getById(id);
            if(existingUser!=null){
                return ResponseEntity.notFound().build();
            }
            user.setUpdatedAt(LocalDateTime.now());
            return  new ResponseEntity<>(ApiResponse.success(userService.updateUser(user)), HttpStatus.CREATED);
        } catch (RuntimeException exception){
            ApiResponse<User> errorResponse = ApiResponse.error(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
            return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        User user = userService.findByUserName(username);
        return user != null ? new ResponseEntity<>(user, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
