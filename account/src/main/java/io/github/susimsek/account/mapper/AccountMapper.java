package io.github.susimsek.account.mapper;

import io.github.susimsek.account.dto.AccountDTO;
import io.github.susimsek.account.entity.Account;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AccountMapper extends EntityMapper<AccountDTO, Account> {
}
