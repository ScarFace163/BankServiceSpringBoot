package EffectiveMobile.testTask.user.service;

import EffectiveMobile.testTask.security.JwtService;
import EffectiveMobile.testTask.user.model.BankAccount;
import EffectiveMobile.testTask.user.model.User;
import EffectiveMobile.testTask.user.repository.UserRepository;
import EffectiveMobile.testTask.user.request.EditRequest;
import EffectiveMobile.testTask.user.request.EmailDeleteRequest;
import EffectiveMobile.testTask.user.request.PhoneDeleteRequest;
import EffectiveMobile.testTask.user.request.TransferRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    if (editRequest.getEmail() == null && editRequest.getPhone() == null) {
      throw new IllegalArgumentException("Email and phone are null");
    }
    if (editRequest.getEmail().isEmpty() && editRequest.getPhone().isEmpty()) {
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

  @Override
  public void deleteUserPhone(PhoneDeleteRequest request) {
    User user = (User) jwtService.getCurrentUser();
    log.info("Current user: {}", user);
    if (request.getPhone() != null) {
      if (request.getPhone().size() < user.getPhone().size()) {
        List<String> usersPhone = user.getPhone();
        usersPhone.removeAll(request.getPhone());
        user.setPhone(usersPhone);
        userRepository.save(user);
      } else {
        throw new IllegalArgumentException("Must be 1 phone left");
      }
    } else {
      throw new IllegalArgumentException("Phone are null");
    }
  }

  @Override
  public void deleteUserEmail(EmailDeleteRequest request) {
    User user = (User) jwtService.getCurrentUser();
    log.info("Current user: {}", user);
    if (request.getEmail() != null) {
      if (request.getEmail().size() < user.getEmail().size()) {
        List<String> usersEmail = user.getEmail();
        usersEmail.removeAll(request.getEmail());
        user.setEmail(usersEmail);
        userRepository.save(user);
      } else {
        throw new IllegalArgumentException("Must be 1 email left");
      }
    } else {
      throw new IllegalArgumentException("Phone are null");
    }
  }

  @Override
  public void transferMoney(TransferRequest request) {
    if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Sum must be positive");
    }
    User fromUser = (User) jwtService.getCurrentUser();
    log.info("From user: {}", fromUser);
    User toUser =
        userRepository
            .findByUsername(request.getToAccountUsername().toString())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "No user with username: " + request.getToAccountUsername()));
    log.info("To user: {}", toUser);
    if (fromUser.getBankAccount().getBalance().compareTo(request.getAmount()) < 0) {
      throw new IllegalArgumentException("Not enough money");
    }
    fromUser
        .getBankAccount()
        .setBalance(fromUser.getBankAccount().getBalance().subtract(request.getAmount()));
    toUser
        .getBankAccount()
        .setBalance(toUser.getBankAccount().getBalance().add(request.getAmount()));
    userRepository.save(fromUser);
    userRepository.save(toUser);
  }
}
