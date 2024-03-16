package fsa.training.ems_springboot.service.impl;

import fsa.training.ems_springboot.model.entity.Employee;
import fsa.training.ems_springboot.repository.EmployeeRepository;
import fsa.training.ems_springboot.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getALl() {
        return (List<Employee>) employeeRepository.findByDeletedFalse();
    }

    @Override
    public Optional<Employee> getById(long id) {
        return employeeRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public Employee create(Employee employee) {
//        employee.setDeleted(false);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmailIgnoreCaseAndDeletedFalse(email);
    }

    @Override
    public void delete(long id) {

    }
}
