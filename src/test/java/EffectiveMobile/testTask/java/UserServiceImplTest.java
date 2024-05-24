package EffectiveMobile.testTask.java;

import EffectiveMobile.testTask.user.service.UserServiceImpl;
import EffectiveMobile.testTask.user.repository.UserRepository;
import EffectiveMobile.testTask.user.request.TransferRequest;
import EffectiveMobile.testTask.user.model.User;
import EffectiveMobile.testTask.user.model.BankAccount;
import EffectiveMobile.testTask.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

  private UserServiceImpl userService;
  private UserRepository userRepository;
  private JwtService jwtService;

  @BeforeEach
  public void setUp() {
    userRepository = mock(UserRepository.class);
    jwtService = mock(JwtService.class);
    userService = new UserServiceImpl(userRepository, jwtService);
  }

  @Test
  public void testTransferMoney() {
    // Arrange
    User fromUser = new User();
    BankAccount fromAccount = new BankAccount();
    fromAccount.setBalance(BigDecimal.valueOf(500));
    fromUser.setBankAccount(fromAccount);

    User toUser = new User();
    BankAccount toAccount = new BankAccount();
    toAccount.setBalance(BigDecimal.valueOf(300));
    toUser.setBankAccount(toAccount);

    when(jwtService.getCurrentUser()).thenReturn(fromUser);
    when(userRepository.findByUsername("toUser")).thenReturn(Optional.of(toUser));

    TransferRequest request = new TransferRequest();
    request.setToAccountUsername("toUser");
    request.setAmount(BigDecimal.valueOf(200));

    // Act
    userService.transferMoney(request);

    // Assert
    assertEquals(BigDecimal.valueOf(300), fromUser.getBankAccount().getBalance());
    assertEquals(BigDecimal.valueOf(500), toUser.getBankAccount().getBalance());
    verify(userRepository, times(2)).save(any(User.class));
  }
}
