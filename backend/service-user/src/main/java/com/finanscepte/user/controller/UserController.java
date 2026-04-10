package com.finanscepte.user.controller;

import com.finanscepte.user.dto.UserRequest;
import com.finanscepte.user.dto.UserResponse;
import com.finanscepte.user.service.UserService;
import com.finanscepte.user.util.UserMapper;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserRequest request) {
        return userMapper.toResponse(userService.create(userMapper.toEntity(request)));
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable String id) {
        return userMapper.toResponse(userService.getById(id));
    }

    @GetMapping
    public List<UserResponse> getAll() {
        return userService.getAll().stream().map(userMapper::toResponse).toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        userService.delete(id);
    }
}
