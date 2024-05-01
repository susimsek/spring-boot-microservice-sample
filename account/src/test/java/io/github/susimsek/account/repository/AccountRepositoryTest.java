package io.github.susimsek.account.repository;

import io.github.susimsek.account.config.DatabaseConfig;
import io.github.susimsek.account.entity.Account;
import io.github.susimsek.account.security.SpringSecurityAuditorAware;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({
    DatabaseConfig.class,
    SpringSecurityAuditorAware.class
})
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @PersistenceContext
    private EntityManager em;

    private Account account;

    @BeforeEach
    void setUp() {
        account = Account.builder()
            .accountNumber(1799112206L)
            .customerId(1L)
            .accountType("123 Main Street")
            .branchAddress("New York")
            .communicationSw(false)
            .deleted(false)
            .build();
    }

    @Test
    void saveTest() {
        var other =  Account.builder()
            .accountNumber(1799112206L)
            .customerId(1L)
            .accountType("123 Main Street")
            .branchAddress("New York")
            .communicationSw(false)
            .deleted(false)
            .build();
        Set<Account> set = new HashSet<>();
        set.add(account);
        set.add(other);
        accountRepository.saveAll(set);
        var fetchedAccount = em.find(Account.class, 1799112206L);
        Assertions.assertTrue(set.contains(other));
        Assertions.assertEquals(1L, accountRepository.count());
        Assertions.assertEquals(1799112206L, fetchedAccount.getAccountNumber());
    }

}