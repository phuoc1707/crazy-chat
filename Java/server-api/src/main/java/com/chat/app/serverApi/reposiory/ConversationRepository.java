package com.chat.app.serverApi.reposiory;

import com.chat.app.serverApi.entity.Conversation;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByIsPrivateFalse();

//    @Query("SELECT c FROM Conversation c JOIN c.members u WHERE u.id = :userId")
//    List<Conversation> findAllByUserId(@Param("userId") Long userId);

    // Sử dụng JPA derived query (Spring Data JPA sẽ tự động tạo query dựa trên tên phương thức)
    List<Conversation> findConversationsByMembersId(Long userId);

    // Hoặc bạn có thể sử dụng @Query để viết query tùy chỉnh (ví dụ dùng JPQL)
    // @Query("SELECT c FROM Conversation c JOIN c.members m WHERE m.id = :userId")
    // List<Conversation> findConversationsByUserIdJPQL(@Param("userId") Long userId);
}
