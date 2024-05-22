package EffectiveMobile.testTask.auth;

import EffectiveMobile.testTask.security.JwtService;
import EffectiveMobile.testTask.user.model.BankAccount;
import EffectiveMobile.testTask.user.model.Role;
import EffectiveMobile.testTask.user.model.User;
import EffectiveMobile.testTask.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .bankAccount(new BankAccount())
                .email(request.getEmail())
                .phone(request.getPhone())
                .birthDate(LocalDate.parse("2004-06-12"))
                .fullName("Viktor Sergeevich Korneplod")
                .role(Role.USER)
                .build();
        user.getBankAccount().setStartBalance(request.getStartBalance());
        user.getBankAccount().setBalance(request.getStartBalance());
        user.getBankAccount().setUser(user);
        log.info("SavedUser{}", user);
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(EntityNotFoundException::new);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
