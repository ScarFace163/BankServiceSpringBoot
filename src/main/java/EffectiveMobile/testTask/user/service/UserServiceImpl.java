package EffectiveMobile.testTask.user.service;

import EffectiveMobile.testTask.user.model.BankAccount;
import EffectiveMobile.testTask.user.model.User;
import EffectiveMobile.testTask.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public User createUser(User user) {
    BankAccount bankAccount = user.getBankAccount();
    bankAccount.setUser(user);
    bankAccount.setBalance(bankAccount.getStartBalance());
    return userRepository.save(user);
  }

  @Override
  public List<User> findUsers(LocalDate birthDate, String phone, String fullName, String email) {
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
      if (findedUserOptional.isPresent()){
          User findedUser = findedUserOptional.get();
          if (!serchResultListOfUsers.contains(findedUser)) {
              return Collections.emptyList();
          } else {
              serchResultListOfUsers = new ArrayList<>();
              serchResultListOfUsers.add(findedUser);
          }
      }
      else{
          return Collections.emptyList();
      }

    }
    if (birthDate != null) {
      Optional<List<User>> findedUsersOptional = userRepository.findByDate(birthDate);
      if (findedUsersOptional.isPresent()){
          List <User> findedUsers = findedUsersOptional.get();
          if (serchResultListOfUsers.size() == startSizeOfList) {
              serchResultListOfUsers = new ArrayList<>(findedUsers);
          } else if (!findedUsers.contains(serchResultListOfUsers.get(0))) {
              return Collections.emptyList();
          }
      }
      else{
          return Collections.emptyList();
      }
    }
    if (fullName != null) {
      Optional<List<User>> findedUsersOptional = userRepository.findByFullName(fullName);
      if (findedUsersOptional.isPresent()){
          List <User> findedUsers = findedUsersOptional.get();
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
      }
      else{
          return Collections.emptyList();
      }
    }
    return serchResultListOfUsers;
  }
}
