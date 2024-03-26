package fsa.training.ems_springboot.model.dto;


import fsa.training.ems_springboot.enums.EmployeeLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailDto {

    private Long id;

    private String name;

    private String email;

    private LocalDate dateOfBirth;

    private EmployeeLevel level;

    private DepartmentListDto department;

}
