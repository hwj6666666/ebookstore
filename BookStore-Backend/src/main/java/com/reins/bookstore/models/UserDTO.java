package com.reins.bookstore.models;

import com.reins.bookstore.entity.User;
import com.reins.bookstore.entity.UserAuth;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    String username;
    String nickname;
    Long balance;
    String avatar;
    String introduction;

    public static UserDTO fromEntity(User user) {
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
        return new UserDTO(
                username,
                user.getNickname(),
                user.getBalance(),
                user.getAvatar(),
                user.getIntroduction()
        );
    }
}
