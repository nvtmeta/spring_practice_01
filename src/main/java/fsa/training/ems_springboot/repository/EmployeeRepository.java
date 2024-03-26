package fsa.training.ems_springboot.repository;

import fsa.training.ems_springboot.enums.EmployeeLevel;
import fsa.training.ems_springboot.model.dto.EmployeeListDto;
import fsa.training.ems_springboot.model.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EmployeeRepository extends
        CrudRepository<Employee, Long>,
        PagingAndSortingRepository<Employee, Long>,
        JpaSpecificationExecutor<Employee> {
    Optional<Employee> findByIdAndDeletedFalse(Long id);

    List<Employee> findByDeletedFalse();


    boolean existsByEmailIgnoreCaseAndDeletedFalse(String email);

    //    keyword for name or email
    @Query(value = """
            SELECT new fsa.training.ems_springboot.model.dto.EmployeeListDto(e.id, e.name, e.email, e.dateOfBirth, e.level ,
                         d.id, d.name ) FROM Employee e left join e.department d on d.deleted = false
                        WHERE  e.deleted = false  AND
                        (:keyword is null OR (lower(e.name) LIKE lower(concat('%', :keyword, '%') ))  OR
                        lower(e.email) LIKE lower(concat('%', :keyword, '%')))
                        AND (:level is null OR e.level = :level)
            """)
//    or isDeleted false will error when employee don't belong any department
//    avoid e.department.name vs d.name, should use d.name because first one is inner join ,
    Page<EmployeeListDto> getEmployeePage(@Param("keyword") String keyword, @Param("level") EmployeeLevel level, Pageable pageable);
}
