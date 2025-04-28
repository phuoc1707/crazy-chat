package com.chat.app.serverApi.service.impl;

import com.chat.app.serverApi.entity.Conversation;
import com.chat.app.serverApi.entity.Media;
import com.chat.app.serverApi.entity.Message;
import com.chat.app.serverApi.entity.User;
import com.chat.app.serverApi.reposiory.MessageRepository;
import com.chat.app.serverApi.service.ConversationService;
import com.chat.app.serverApi.service.MediaService;
import com.chat.app.serverApi.service.MessageService;
import com.chat.app.serverApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    UserService userService;

    @Autowired
    ConversationService conversationService;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    @Lazy
    MediaService mediaService;

    @Override
    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> getMessagesByConversation(Long conversationId) {
        Optional<Conversation> conversation= Optional.ofNullable(conversationService.getConversationById(conversationId));
        return conversation.map(messageRepository::findByConversationOrderByIdAsc).orElse(List.of());

    }

    @Override
    @Transactional
    public Message saveMessage(Long senderId, Long conversationId, String content, Set<String> urls) {
        System.out.println("conversationId: "+ conversationId +" ---------- senderId: " +senderId +"--------urls"+ urls.toString());
        User user = userService.getById(senderId);
        Conversation conversation = conversationService.getConversationById(conversationId);
        if(!conversation.getMembers().contains(user))
            throw new RuntimeException("User not belong to this conversation!!!");

        if(user != null && conversation != null){
            Message message = messageRepository.save(Message.builder()
                    .sender(user)
                    .conversation(conversation)
                    .message(content)
                    .createdAt(LocalDateTime.now())
                    .build());

//            urls.stream().forEach(s -> mediaService.saveMedia(message, s));
//            conversation.getMessages().add(message);
            Message newMessage =messageRepository.save(message);
            conversationService.updateConversation(conversation);
            return  newMessage;
        }
        throw new RuntimeException("User: " + senderId + " or conversation " +conversationId + " not already exists!!!");
    }

    @Override
    @Transactional
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}
