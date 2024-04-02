package fsa.training.ems_springboot.security;

import fsa.training.ems_springboot.model.entity.Account;
import fsa.training.ems_springboot.repository.AccountRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SecurityUserDetailService implements UserDetailsService {
    private final AccountRepository accountRepository;

    public SecurityUserDetailService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByUsernameAndActivatedTrue(username);
        System.out.println("accountOptional" + accountOptional);
        if (accountOptional.isEmpty()) {
            System.out.println("username: " + username);

            throw new UsernameNotFoundException("Account: " + username + " not found");
        }
        System.out.println("username: " + username);
        Account account = accountOptional.get();
        List<GrantedAuthority> authorities = account.getAuthorities().stream()
                .map(authority -> "ROLE_" + authority.getName())
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new User(account.getUsername(), account.getPassword(), authorities);
    }

}
