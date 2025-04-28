package com.chat.app.serverApi.service.impl;


import com.chat.app.serverApi.entity.User;
import com.chat.app.serverApi.reposiory.UserRepository;
import com.chat.app.serverApi.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        if(!userRepository.existsByUserName(user.getUserName())){
            throw new RuntimeException("User: " + user.getUserName() + " not already exists");
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> findByUserNameContaining(String keyword) {
        return userRepository.findByUserNameContaining(keyword);
    }

    @Override
    @Transactional
    public User createUser(User user) {
        if(userRepository.existsByUserName(user.getUserName())){
            throw new RuntimeException("User: " + user.getUserName() + " already exists");
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void setUserOnlineStatus(Long userId, boolean online) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setOnline(online);
            userRepository.save(user);
        });
    }
}
