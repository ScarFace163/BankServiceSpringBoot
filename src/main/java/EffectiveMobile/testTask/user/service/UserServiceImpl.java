package EffectiveMobile.testTask.user.service;

import EffectiveMobile.testTask.user.model.BankAccount;
import EffectiveMobile.testTask.user.model.User;
import EffectiveMobile.testTask.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        BankAccount bankAccount = user.getBankAccount();
        bankAccount.setUser(user);
        bankAccount.setBalance(bankAccount.getStartBalance());
        return  userRepository.save(user);
    }
}
