package com.chat.app.serverApi.service;

import com.chat.app.serverApi.entity.Media;
import com.chat.app.serverApi.entity.Message;

import java.util.List;
import java.util.Optional;

public interface MediaService {
    Optional<Media> getMediaById(Long id) ;
    List<Media> getMediaByMessage(Long messageId);
    Media saveMedia(Message message, String url);
    void deleteMedia(Long id);
}
