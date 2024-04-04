package fsa.training.ems_springboot.repository;

import fsa.training.ems_springboot.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByUsernameIgnoreCaseAndActivatedTrue(String username);
}
