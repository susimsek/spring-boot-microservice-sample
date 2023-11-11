package io.github.susimsek.account.service;

import io.github.susimsek.account.dto.AccountDTO;
import io.github.susimsek.account.dto.CustomerDTO;

import java.util.List;

public interface AccountService {

    void createAccount(CustomerDTO customer);

    CustomerDTO fetchAccount(String mobileNumber);

    List<AccountDTO> getAccountRevisions(Long accountNumber);

    String geCreatorUsername(Long accountNumber);

    boolean updateAccount(Long accountNumber, CustomerDTO customer);

    void deleteAccount(String mobileNumber);

    boolean updateCommunicationStatus(Long accountNumber);
}
