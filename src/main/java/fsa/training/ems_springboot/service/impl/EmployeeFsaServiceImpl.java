package fsa.training.ems_springboot.service.impl;

import fsa.training.ems_springboot.model.entity.Employee;
import fsa.training.ems_springboot.service.EmployeeService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class EmployeeFsaServiceImpl implements EmployeeService {

    public EmployeeFsaServiceImpl() {
        System.out.println("EmployeeFsaServiceImpl");
    }

    @Override
    public List<Employee> getALl() {
        return null;
    }

    @Override
    public Optional<Employee> getById(long id) {
        return null;
    }

    @Override
    public Employee create(Employee employee) {
        return null;
    }

    @Override
    public Employee update(Employee employee) {
        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public void delete(long id) {

    }
}
