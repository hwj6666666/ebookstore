package com.reins.bookstore.models;

import com.reins.bookstore.entity.User;
import com.reins.bookstore.entity.UserAuth;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartialUserDTO {
    String username;
    String nickname;
    String avatar;
    String introduction;

    public static PartialUserDTO fromEntity(User user) {
        if (user == null) {
            return null;
        }
        UserAuth userAuth = user.getUserAuth();
        String username;
        if (userAuth == null) {
            username = "UNKNOWN";
        } else {
            username = userAuth.getUsername();
        }
        return new PartialUserDTO(
                username,
                user.getNickname(),
                user.getAvatar(),
                user.getIntroduction()
        );
    }
}
