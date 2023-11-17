package io.github.susimsek.account.service.impl;

import io.github.susimsek.account.dto.CustomerDetailsDTO;
import io.github.susimsek.account.exception.ResourceNotFoundException;
import io.github.susimsek.account.mapper.CustomerMapper;
import io.github.susimsek.account.repository.AccountRepository;
import io.github.susimsek.account.repository.CustomerRepository;
import io.github.susimsek.account.service.CustomerService;
import io.github.susimsek.account.client.CardFeignClient;
import io.github.susimsek.account.client.LoanFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final CardFeignClient cardFeignClient;
    private final LoanFeignClient loanFeignClient;

    private final CustomerMapper customerMapper;

    @Override
    public CustomerDetailsDTO fetchCustomerDetails(String mobileNumber, String correlationId) {
        var customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
            () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        var account = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
            () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        var loan = loanFeignClient.fetchLoanDetails(correlationId, mobileNumber);

        var card = cardFeignClient.fetchCardDetails(correlationId, mobileNumber);

        return customerMapper.toCustomerDetailsDTO(customer, account, loan, card);
    }
}
