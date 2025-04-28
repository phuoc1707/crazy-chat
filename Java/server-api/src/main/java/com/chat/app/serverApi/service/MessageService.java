package com.chat.app.serverApi.service;

import com.chat.app.serverApi.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MessageService {
    Optional<Message> getMessageById(Long id);
    List<Message> getMessagesByConversation(Long conversationId);
    Message saveMessage(Long senderId, Long conversationId, String content,  Set<String> urls);
    void deleteMessage(Long id);
}
