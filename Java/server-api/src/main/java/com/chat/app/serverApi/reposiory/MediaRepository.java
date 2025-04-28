package com.chat.app.serverApi.reposiory;

import com.chat.app.serverApi.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository  extends JpaRepository<Media, Long> {
}
