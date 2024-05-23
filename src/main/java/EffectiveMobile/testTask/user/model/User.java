package EffectiveMobile.testTask.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "username", unique = true)
  private String username;

  @Column(name = "password")
  private String password;

  @ElementCollection
  @Fetch(FetchMode.JOIN)
  @Column(name = "email", unique = true)
  private List<String> email;

  @ElementCollection
  @Fetch(FetchMode.JOIN)
  @Column(name = "phone", unique = true)
  private List<String> phone;

  @Column(name = "birth_date")
  private LocalDate birthDate;

  @Column(name = "full_name")
  private String fullName;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "bank_account_id", referencedColumnName = "id")
  @JsonIgnore
  private BankAccount bankAccount;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return String.valueOf(password);
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
