package com.reins.bookstore.models;

import com.reins.bookstore.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    Long id;
    Long userId;
    String username;
    String avatar;
    String content;
    String reply;
    Integer like;
    Boolean liked;
    Timestamp createdAt;

    public static CommentDTO from(Comment comment, Boolean liked) {
        Comment reply = comment.getReply();
        return new CommentDTO(
                comment.getId(),
                comment.getUser().getId(),
                comment.getUser().getNickname(),
                comment.getUser().getAvatar(),
                comment.getContent(),
                (reply != null) ? reply.getUser().getNickname() : null,
                comment.getLike(),
                liked,
                comment.getCreatedAt()
        );
    }
}
