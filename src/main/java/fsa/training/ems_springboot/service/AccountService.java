package fsa.training.ems_springboot.service;

import fsa.training.ems_springboot.model.entity.Account;

import java.util.Optional;

public interface AccountService {

    Optional<Account> findByUsernameAndActivatedTrue(String username);
}
