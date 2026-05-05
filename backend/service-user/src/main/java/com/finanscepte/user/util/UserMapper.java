package com.finanscepte.user.util;

import com.finanscepte.user.dto.UserRequest;
import com.finanscepte.user.dto.UserResponse;
import com.finanscepte.user.model.User;
import com.finanscepte.user.model.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequest request) {
        return User.builder()
                .username(request.username())
                .email(request.email())
                .build();
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
    }

    public UserJpaEntity toJpaEntity(UserRequest request) {
        return UserJpaEntity.builder()
                .username(request.username())
                .email(request.email())
                .password("")
                .build();
    }

    public User fromJpaEntity(UserJpaEntity entity) {
        return User.builder()
                .id(String.valueOf(entity.getId()))
                .username(entity.getUsername())
                .email(entity.getEmail())
                .build();
    }
}
