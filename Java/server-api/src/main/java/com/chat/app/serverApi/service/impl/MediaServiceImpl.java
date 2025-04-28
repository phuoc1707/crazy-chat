package com.chat.app.serverApi.service.impl;

import com.chat.app.serverApi.entity.Media;
import com.chat.app.serverApi.entity.Message;
import com.chat.app.serverApi.reposiory.MediaRepository;
import com.chat.app.serverApi.service.MediaService;
import com.chat.app.serverApi.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MediaServiceImpl implements MediaService {

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    @Lazy
    MessageService messageService;

    @Override
    public Optional<Media> getMediaById(Long id) {
        return mediaRepository.findById(id);
    }

    @Override
    public List<Media> getMediaByMessage(Long messageId) {
        Optional<Message> messageOptional = messageService.getMessageById(messageId);
        return messageOptional.map(Message::getMedias).orElse(List.of());
    }

    @Override
    @Transactional
    public Media saveMedia(Message message, String url) {
        if (message != null ) {
            Media media = Media.builder()
                    .message(message)
                    .url(url)
                    .build();
            return mediaRepository.save(media);
        }
        throw new RuntimeException("Message not already exists!!!");
    }

    @Override
    @Transactional
    public void deleteMedia(Long id) {
        mediaRepository.deleteById(id);
    }
}
