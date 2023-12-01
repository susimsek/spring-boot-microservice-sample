package io.github.susimsek.loan.repository;

import io.github.susimsek.loan.entity.Loan;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    String LOAN_BY_MOBILE_NUMBER = "loanByMobileNumber";

    @Cacheable(cacheNames = LOAN_BY_MOBILE_NUMBER)
    Optional<Loan> findByMobileNumber(String mobileNumber);

    boolean existsByMobileNumber(String mobileNumber);

    Optional<Loan> findByLoanNumber(String loanNumber);

}