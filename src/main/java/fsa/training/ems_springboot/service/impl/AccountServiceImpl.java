package fsa.training.ems_springboot.service.impl;

import fsa.training.ems_springboot.model.entity.Account;
import fsa.training.ems_springboot.repository.AccountRepository;
import fsa.training.ems_springboot.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Account> findByUsernameAndActivatedTrue(String username) {
        return accountRepository.findByUsernameIgnoreCaseAndActivatedTrue(username);
    }
}
