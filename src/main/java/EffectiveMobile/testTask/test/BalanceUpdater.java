package EffectiveMobile.testTask.test;

import EffectiveMobile.testTask.user.model.BankAccount;
import EffectiveMobile.testTask.user.repository.BankAccountRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class BalanceUpdater {

  private final BankAccountRepository bankAccountRepository;

  public BalanceUpdater(BankAccountRepository bankAccountRepository) {
    this.bankAccountRepository = bankAccountRepository;
  }

  @Scheduled(cron = "0 * * * * ?")
  public void updateBalances() {
    List<BankAccount> allBankAccounts = bankAccountRepository.findAll();

    for (BankAccount account : allBankAccounts) {
      BigDecimal startBalance = account.getStartBalance();
      BigDecimal currentBalance = account.getBalance();
      BigDecimal maxBalance = startBalance.multiply(BigDecimal.valueOf(2.07));

      if (currentBalance.compareTo(maxBalance) < 0) {
        BigDecimal newBalance = currentBalance.multiply(BigDecimal.valueOf(1.05));

        if (newBalance.compareTo(maxBalance) <= 0) {
          account.setBalance(newBalance);

        } else {
          account.setBalance(maxBalance);
        }
        bankAccountRepository.save(account);
      }
    }
  }

  @Scheduled(cron = "0 0 0 1 * ?")
  public void updateStartBalances() {
    List<BankAccount> allBankAccounts = bankAccountRepository.findAll();

    for (BankAccount account : allBankAccounts) {
      BigDecimal currentBalance = account.getBalance();
      account.setStartBalance(currentBalance);
    }
  }
}
