package com.chat.app.serverApi.service;

import com.chat.app.serverApi.entity.User;

import java.util.List;

public interface UserService {
    List<User>  getAll();

   User getById(Long id);

    User findByUserName(String userName);
    User updateUser (User user);
    List<User> findByUserNameContaining(String keyword);
    User createUser(User user);
    void deleteUser(Long id);
    void setUserOnlineStatus(Long userId, boolean online);
}
