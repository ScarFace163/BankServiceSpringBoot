package EffectiveMobile.testTask.user.service;

import EffectiveMobile.testTask.user.model.User;
import EffectiveMobile.testTask.user.request.EditRequest;
import EffectiveMobile.testTask.user.request.EmailDeleteRequest;
import EffectiveMobile.testTask.user.request.PhoneDeleteRequest;
import EffectiveMobile.testTask.user.request.TransferRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
  public User createUser(User user);

  public List<User> findUsersByFilter(
      LocalDate birthDate, String phone, String fullName, String email);

  public UserDetailsService userDetailService();

  public User findByUsername(String username);

  public void editUserData(EditRequest editRequest);

  public void addUserContacts(EditRequest request);

  public void deleteUserPhone(PhoneDeleteRequest request);

  public void deleteUserEmail(EmailDeleteRequest request);

  public void transferMoney(TransferRequest request);
}
