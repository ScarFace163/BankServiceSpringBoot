package EffectiveMobile.testTask.user.repository;

import EffectiveMobile.testTask.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  @Query("SELECT u FROM User u WHERE u.birthDate > :date")
  Optional<List<User>> findByDate(LocalDate date);

  @Query("SELECT u FROM User u WHERE u.fullName LIKE :fullName%")
  Optional<List<User>> findByName(String fullName);

  Optional<User> findByPhone(String phone);

  Optional<User> findByEmail(String email);
}
