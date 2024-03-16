package fsa.training.ems_springboot.repository;

import fsa.training.ems_springboot.model.entity.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long>, PagingAndSortingRepository<Employee, Long> {
    Optional<Employee> findByIdAndDeletedFalse(Long id);

    List<Employee> findByDeletedFalse();



    boolean existsByEmailIgnoreCaseAndDeletedFalse(String email);
}
