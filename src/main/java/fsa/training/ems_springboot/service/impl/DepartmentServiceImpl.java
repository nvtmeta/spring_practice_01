package fsa.training.ems_springboot.service.impl;

import fsa.training.ems_springboot.model.entity.Department;
import fsa.training.ems_springboot.repository.DepartmentRepository;
import fsa.training.ems_springboot.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Department> getALl() {
        return departmentRepository.findAll();
    }
}
