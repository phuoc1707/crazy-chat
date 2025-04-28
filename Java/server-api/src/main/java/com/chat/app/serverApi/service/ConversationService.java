package com.chat.app.serverApi.service;

import com.chat.app.serverApi.entity.Conversation;

import java.util.List;
import java.util.Set;

public interface ConversationService {
    List<Conversation> getAllConversations();
    Conversation getConversationById(Long id);
    List<Conversation> getPublicConversations();
    Conversation createConversation(String name, boolean isPrivate, Set<Long> memberIds, Long createBy);
    Conversation updateConversation(Conversation conversation);
    void deleteConversation(Long id);
    Conversation addUserToConversation(Long conversationId, Long userId);
    Conversation removeUserFromConversation(Long conversationId, Long userId);
    List<Conversation> getConversationsByUserId(Long userId);
}
