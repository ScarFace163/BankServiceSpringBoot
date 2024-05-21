package EffectiveMobile.testTask.user.controller;

import EffectiveMobile.testTask.user.model.User;
import EffectiveMobile.testTask.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserServiceImpl userService;
    @PostMapping("/users/create")
    public ResponseEntity<User> createPublicUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        log.info("User created: {}", savedUser);
        return ResponseEntity.ok(savedUser);
    }
}