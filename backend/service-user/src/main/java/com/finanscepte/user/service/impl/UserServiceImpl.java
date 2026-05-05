package com.finanscepte.user.service.impl;

import com.finanscepte.user.model.User;
import com.finanscepte.user.service.DualUserService;
import com.finanscepte.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final DualUserService dualUserService;

    @Override
    public User create(User entity) {
        return dualUserService.create(entity);
    }

    @Override
    public User getById(String id) {
        return dualUserService.getById(id);
    }

    @Override
    public List<User> getAll() {
        return dualUserService.getAll();
    }

    @Override
    public void delete(String id) {
        dualUserService.delete(id);
    }
}
