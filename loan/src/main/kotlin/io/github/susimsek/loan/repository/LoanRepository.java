package io.github.susimsek.loan.repository;

import io.github.susimsek.loan.entity.Loan;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>,
    JpaSpecificationExecutor<Loan> {

    String LOAN_BY_MOBILE_NUMBER_CACHE = "loanByMobileNumber";

    @Cacheable(cacheNames = LOAN_BY_MOBILE_NUMBER_CACHE)
    Optional<Loan> findByMobileNumber(String mobileNumber);

    boolean existsByMobileNumber(String mobileNumber);

    Optional<Loan> findByLoanNumber(String loanNumber);

}