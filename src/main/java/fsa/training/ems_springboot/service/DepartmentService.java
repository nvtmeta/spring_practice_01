package fsa.training.ems_springboot.service;

import fsa.training.ems_springboot.model.dto.DepartmentListDto;
import fsa.training.ems_springboot.model.entity.Department;

import java.util.List;

public interface DepartmentService {

    List<Department> getALl();
}
