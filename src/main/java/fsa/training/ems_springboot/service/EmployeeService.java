package fsa.training.ems_springboot.service;

import fsa.training.ems_springboot.enums.EmployeeLevel;
import fsa.training.ems_springboot.model.dto.EmployeeListDto;
import fsa.training.ems_springboot.model.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<Employee> getALl();

    Page<Employee> getAll(Pageable pageable);

    Page<Employee> getAll(Specification<Employee> spec, Pageable pageable);

    Page<EmployeeListDto> getEmployeePage(String keyword, EmployeeLevel level, Pageable pageable);

    Optional<Employee> getById(long id);


    void create(Employee employee);


    void update(Employee employee);

    boolean existsByEmail(String email);

    void deleteById(long id);

}
