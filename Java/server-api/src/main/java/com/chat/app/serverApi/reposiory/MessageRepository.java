package com.chat.app.serverApi.reposiory;

import com.chat.app.serverApi.entity.Conversation;
import com.chat.app.serverApi.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findByConversationOrderByIdAsc(Conversation conversation);
}
