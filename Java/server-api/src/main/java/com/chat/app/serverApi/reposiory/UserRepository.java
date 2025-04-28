package com.chat.app.serverApi.reposiory;

import com.chat.app.serverApi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
    List<User> findByUserNameContaining(String keyWord);
    boolean existsByUserName(String userName);
}
