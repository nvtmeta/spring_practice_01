package fsa.training.ems_springboot.service;

import fsa.training.ems_springboot.model.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<Employee> getALl();


    Optional<Employee> getById(long id);


    Employee create(Employee employee);


    Employee update(Employee employee);

    boolean existsByEmail(String email);


    void delete(long id);
}
