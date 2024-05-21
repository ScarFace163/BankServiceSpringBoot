package EffectiveMobile.testTask.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bank_account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_balance")
    private BigDecimal startBalance;

    @Column(name = "balance")
    private BigDecimal balance;


    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
}