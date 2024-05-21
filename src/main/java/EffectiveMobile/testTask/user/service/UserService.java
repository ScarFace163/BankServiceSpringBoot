package EffectiveMobile.testTask.user.service;

import EffectiveMobile.testTask.user.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    List<User> findUsers(LocalDate birthDate, String phone, String fullName, String email);
}
