package EffectiveMobile.testTask.user.controller;

import EffectiveMobile.testTask.user.model.User;
import EffectiveMobile.testTask.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;


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
    @GetMapping("/users/find")
    public ResponseEntity<List<User>> findUsersByFilter(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email){
        List<User> users = userService.findUsers(date,phone,fullName,email);
        log.info("Find {} users", users.size());
        log.info("Users found: {}", users);
        return ResponseEntity.ok(users);
    }
}