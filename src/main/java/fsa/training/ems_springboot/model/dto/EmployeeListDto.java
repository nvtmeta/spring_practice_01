package fsa.training.ems_springboot.model.dto;

import fsa.training.ems_springboot.enums.EmployeeLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
public class EmployeeListDto {


    private Long id;

    public EmployeeListDto(Long id, String name, String email, LocalDate dateOfBirth, EmployeeLevel level, BigDecimal salary,
                           Long departmentId, String departmentName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.level = level;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.salary = salary;
    }

    private String name;

    public EmployeeListDto(Long id, String name, String email, LocalDate dateOfBirth, EmployeeLevel level, Long departmentId, String departmentName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.level = level;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    private String email;

    private LocalDate dateOfBirth;

    private BigDecimal salary;
    private EmployeeLevel level;
    private Long departmentId;
    private String departmentName;
}
