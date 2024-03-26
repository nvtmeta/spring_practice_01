package fsa.training.ems_springboot.service.impl;

import fsa.training.ems_springboot.model.entity.Department;
import fsa.training.ems_springboot.repository.DepartmentRepository;
import fsa.training.ems_springboot.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


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

    @Override
    public List<Department> findByDeletedFalse() {
        return departmentRepository.findByDeletedFalse();
    }

    @Override
    public Optional<Department> getById(long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public Optional<Department> getByName(String name) {
        return departmentRepository.findByName(name);
    }
}
