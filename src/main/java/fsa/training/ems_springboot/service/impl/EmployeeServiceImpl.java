package fsa.training.ems_springboot.service.impl;

import fsa.training.ems_springboot.model.entity.Employee;
import fsa.training.ems_springboot.repository.EmployeeRepository;
import fsa.training.ems_springboot.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getALl() {
        return (List<Employee>) employeeRepository.findAll();
    }
}
