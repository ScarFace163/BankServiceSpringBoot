package EffectiveMobile.testTask.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "username", unique = true)
  private String username;

  @Column(name = "password")
  private String password;

  @ElementCollection
  @Column(name = "email", unique = true)
  private List<String> email;

  @ElementCollection
  @Column(name = "phone", unique = true)
  private List<String> phone;

  @Column(name = "birth_date")
  private LocalDate birthDate;

  @Column(name = "full_name")
  private String fullName;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name="bank_account_id", referencedColumnName = "id")
  private BankAccount bankAccount;
}
