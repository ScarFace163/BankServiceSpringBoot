package EffectiveMobile.testTask.user.service;

import EffectiveMobile.testTask.user.model.User;
import EffectiveMobile.testTask.user.request.EditRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
  User createUser(User user);

  List<User> findUsersByFilter(LocalDate birthDate, String phone, String fullName, String email);

  UserDetailsService userDetailService();

  public User findByUsername(String username);
  public void editUserData(EditRequest editRequest);

  void addUserContacts(EditRequest request);
}
