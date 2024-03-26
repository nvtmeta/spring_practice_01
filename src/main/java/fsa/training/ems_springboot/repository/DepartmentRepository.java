package fsa.training.ems_springboot.repository;

import fsa.training.ems_springboot.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DepartmentRepository extends
        JpaRepository<Department, Long>,
        JpaSpecificationExecutor<Department> {

//    findByName

    Optional<Department> findByName(String name);
}
