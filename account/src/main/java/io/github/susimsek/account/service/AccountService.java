package io.github.susimsek.account.service;

import io.github.susimsek.account.dto.CustomerDTO;

public interface AccountService {

    void createAccount(CustomerDTO customer);

    CustomerDTO fetchAccount(String mobileNumber);

    boolean updateAccount(Long accountNumber, CustomerDTO customer);

    void deleteAccount(String mobileNumber);

    boolean updateCommunicationStatus(Long accountNumber);
}
