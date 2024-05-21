package EffectiveMobile.testTask.user.repository;

import EffectiveMobile.testTask.user.model.BankAccount;
import EffectiveMobile.testTask.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

}
