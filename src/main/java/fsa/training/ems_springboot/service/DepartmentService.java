package fsa.training.ems_springboot.service;

import fsa.training.ems_springboot.model.dto.DepartmentListDto;
import fsa.training.ems_springboot.model.entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    List<Department> getALl();

    Optional<Department> getById(long id);
    Optional<Department> getByName(String name);

}
