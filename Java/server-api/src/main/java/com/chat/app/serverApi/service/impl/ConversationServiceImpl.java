package com.chat.app.serverApi.service.impl;

import com.chat.app.serverApi.entity.Conversation;
import com.chat.app.serverApi.entity.User;
import com.chat.app.serverApi.reposiory.ConversationRepository;
import com.chat.app.serverApi.reposiory.UserRepository;
import com.chat.app.serverApi.service.ConversationService;
import com.chat.app.serverApi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl  implements ConversationService {


    @Autowired
    ConversationRepository conversationRepository;

    @Autowired
    UserService userService;

    @Override
    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }

    @Override
    public Conversation getConversationById(Long id) {
        return conversationRepository.findById(id).get();
    }

    @Override
    public List<Conversation> getPublicConversations() {
        return conversationRepository.findByIsPrivateFalse();
    }

    @Override
    @Transactional
    public Conversation createConversation(String name, boolean isPrivate, Set<Long> memberIds, Long createBy) {
        Set<User> members = userService.getAll()
                .stream().filter(user -> memberIds.contains(user.getId())).collect(Collectors.toSet());
        User userCreate = userService.getById(createBy);
        System.out.println(members.size());
        if(userCreate != null ){
            Conversation conversation=  Conversation.builder().name(name)
                    .isPrivate(members.size() == 2? true : false)
                    .members(members)
                    .createBy(userCreate)
                    .createdAt(LocalDateTime.now())
                    .build();
            return conversationRepository.save(conversation);
        }
        throw new  RuntimeException("cannot create!!!");

    }

    @Override
    @Transactional
    public Conversation updateConversation(Conversation conversation) {
        conversation.setUpdatedAt(LocalDateTime.now());
        return conversationRepository.save(conversation);
    }

    @Override
    @Transactional
    public void deleteConversation(Long id) {
        conversationRepository.deleteById(id);
    }

    @Override
    public Conversation addUserToConversation(Long conversationId, Long userId) {
        Optional<Conversation> conversationOptional = conversationRepository.findById(conversationId);
        User user = userService.getById(userId);

        if (conversationOptional.isPresent() && user != null) {
            Conversation conversation = conversationOptional.get();
            conversation.getMembers().add(user);
            conversation.setPrivate(conversation.getMembers().size() == 2 ? true : false);
            conversation.setUpdatedAt(LocalDateTime.now());
            return conversationRepository.save(conversation);
        }
        throw new  RuntimeException("cannot added!!!");
    }

    @Override
    public Conversation removeUserFromConversation(Long conversationId, Long userId) {
        Optional<Conversation> conversationOptional = conversationRepository.findById(conversationId);
        User user = userService.getById(userId);

        if (conversationOptional.isPresent() && user != null) {
            Conversation conversation = conversationOptional.get();
            conversation.setUpdatedAt(LocalDateTime.now());
            conversation.getMembers().remove(user);
            return conversationRepository.save(conversation);
        }

        throw new  RuntimeException("cannot remove!!!");
    }

    @Override
    public List<Conversation> getConversationsByUserId(Long userId) {
        return conversationRepository.findConversationsByMembersId(userId);
    }
}
