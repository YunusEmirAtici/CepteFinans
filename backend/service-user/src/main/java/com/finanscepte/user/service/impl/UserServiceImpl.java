package com.finanscepte.user.service.impl;

import com.finanscepte.user.exception.ResourceNotFoundException;
import com.finanscepte.user.model.User;
import com.finanscepte.user.repository.UserMongoRepository;
import com.finanscepte.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMongoRepository userMongoRepository;

    @Override
    public User create(User entity) {
        return userMongoRepository.save(entity);
    }

    @Override
    public User getById(String id) {
        return userMongoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    @Override
    public List<User> getAll() {
        return userMongoRepository.findAll();
    }

    @Override
    public void delete(String id) {
        userMongoRepository.deleteById(id);
    }
}
