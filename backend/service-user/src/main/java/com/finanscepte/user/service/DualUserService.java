package com.finanscepte.user.service;

import com.finanscepte.common.exception.ResourceNotFoundException;
import com.finanscepte.user.model.User;
import com.finanscepte.user.model.UserJpaEntity;
import com.finanscepte.user.repository.UserJpaRepository;
import com.finanscepte.user.repository.UserMongoRepository;
import com.finanscepte.user.util.UserMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DualUserService {

    private final UserMongoRepository userMongoRepository;
    private final UserMapper userMapper;

    @Autowired(required = false)
    private UserJpaRepository userJpaRepository;

    public User create(User entity) {
        if (userJpaRepository != null) {
            UserJpaEntity jpaEntity = UserJpaEntity.builder()
                    .username(entity.getUsername())
                    .email(entity.getEmail())
                    .password("")
                    .build();
            UserJpaEntity saved = userJpaRepository.save(jpaEntity);
            return userMapper.fromJpaEntity(saved);
        }
        return userMongoRepository.save(entity);
    }

    public User getById(String id) {
        if (userJpaRepository != null) {
            UserJpaEntity entity = userJpaRepository.findById(Long.valueOf(id))
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
            return userMapper.fromJpaEntity(entity);
        }
        return userMongoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    public List<User> getAll() {
        if (userJpaRepository != null) {
            return userJpaRepository.findAll().stream()
                    .map(userMapper::fromJpaEntity)
                    .collect(Collectors.toList());
        }
        return userMongoRepository.findAll();
    }

    public void delete(String id) {
        if (userJpaRepository != null) {
            userJpaRepository.deleteById(Long.valueOf(id));
            return;
        }
        userMongoRepository.deleteById(id);
    }
}
