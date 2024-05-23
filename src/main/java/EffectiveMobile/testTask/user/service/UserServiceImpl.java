package EffectiveMobile.testTask.user.service;

import EffectiveMobile.testTask.security.JwtService;
import EffectiveMobile.testTask.user.model.BankAccount;
import EffectiveMobile.testTask.user.model.User;
import EffectiveMobile.testTask.user.repository.UserRepository;
import EffectiveMobile.testTask.user.request.EditRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
  private final UserRepository userRepository;
  private final JwtService jwtService;

  @Override
  public User createUser(User user) {
    BankAccount bankAccount = user.getBankAccount();
    bankAccount.setUser(user);
    bankAccount.setBalance(bankAccount.getStartBalance());
    return userRepository.save(user);
  }

  @Override
  public List<User> findUsersByFilter(
      LocalDate birthDate, String phone, String fullName, String email) {
    List<User> serchResultListOfUsers = userRepository.findAll();
    int startSizeOfList = serchResultListOfUsers.size();
    if (phone != null) {
      Optional<User> findedUserOptional = userRepository.findByPhone(phone);
      if (findedUserOptional.isPresent()) {
        User findedUser = findedUserOptional.get();
        serchResultListOfUsers = new ArrayList<>();
        serchResultListOfUsers.add(findedUser);
      } else {
        return Collections.emptyList();
      }
    }
    if (email != null) {
      Optional<User> findedUserOptional = userRepository.findByEmail(email);
      if (findedUserOptional.isPresent()) {
        User findedUser = findedUserOptional.get();
        if (!serchResultListOfUsers.contains(findedUser)) {
          return Collections.emptyList();
        } else {
          serchResultListOfUsers = new ArrayList<>();
          serchResultListOfUsers.add(findedUser);
        }
      } else {
        return Collections.emptyList();
      }
    }
    if (birthDate != null) {
      Optional<List<User>> findedUsersOptional = userRepository.findByDate(birthDate);
      if (findedUsersOptional.isPresent()) {
        List<User> findedUsers = findedUsersOptional.get();
        if (serchResultListOfUsers.size() == startSizeOfList) {
          serchResultListOfUsers = new ArrayList<>(findedUsers);
        } else if (!findedUsers.contains(serchResultListOfUsers.get(0))) {
          return Collections.emptyList();
        }
      } else {
        return Collections.emptyList();
      }
    }
    if (fullName != null) {
      Optional<List<User>> findedUsersOptional = userRepository.findByName(fullName);
      if (findedUsersOptional.isPresent()) {
        List<User> findedUsers = findedUsersOptional.get();
        if (serchResultListOfUsers.size() == startSizeOfList) {
          serchResultListOfUsers = new ArrayList<>(findedUsers);
        } else {
          if (serchResultListOfUsers.size() == 1) {
            if (!findedUsers.contains(serchResultListOfUsers.get(0))) {
              return Collections.emptyList();
            } else {
              serchResultListOfUsers = new ArrayList<>(findedUsers);
            }
          } else {
            Set<User> setOfUsers = new HashSet<>(findedUsers);
            List<User> result = new ArrayList<>();
            for (User user : serchResultListOfUsers) {
              if (setOfUsers.contains(user)) {
                result.add(user);
              }
            }
            return result;
          }
        }
      } else {
        return Collections.emptyList();
      }
    }
    return serchResultListOfUsers;
  }

  @Override
  public UserDetailsService userDetailService() {
    return this::findByUsername;
  }

  @Override
  public User findByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("Пользотваель не найден"));
  }

  @Override
  public void editUserData(EditRequest editRequest) {
    User user = (User) jwtService.getCurrentUser();
    if(editRequest.getEmail()==null && editRequest.getPhone()==null){
      throw new IllegalArgumentException("Email and phone are null");
    }
    if(editRequest.getEmail().isEmpty() && editRequest.getPhone().isEmpty()){
      throw new IllegalArgumentException("Email and phone are null");
    }
    log.info("Current user: {}", user);
    user.setEmail(editRequest.getEmail());
    user.setPhone(editRequest.getPhone());
    userRepository.save(user);
    log.info("Saved user: {}", user);
  }

  @Override
  public void addUserContacts(EditRequest request) {
    User user = (User) jwtService.getCurrentUser();
    log.info("Current user: {}", user);
    if (request.getPhone() != null) {
      user.setPhone(
          Stream.concat(user.getPhone().stream(), request.getPhone().stream())
              .collect(Collectors.toList()));
      }
    if (request.getEmail() != null) {
      user.setEmail(
          Stream.concat(user.getEmail().stream(), request.getEmail().stream())
              .collect(Collectors.toList()));
    }
    userRepository.save(user);
    log.info("Saved user: {}", user);
  }
}
