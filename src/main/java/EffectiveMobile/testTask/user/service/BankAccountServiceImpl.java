package EffectiveMobile.testTask.user.service;

import EffectiveMobile.testTask.user.model.BankAccount;
import EffectiveMobile.testTask.user.repository.BankAccountRepository;

public class BankAccountServiceImpl implements BankAccountService {
  private BankAccountRepository bankAccountRepository;

  @Override
  public BankAccount createBankAccount(BankAccount bankAccount) {
    return bankAccountRepository.save(bankAccount);
  }
}
