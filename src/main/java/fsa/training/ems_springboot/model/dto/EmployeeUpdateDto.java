package fsa.training.ems_springboot.model.dto;

import fsa.training.ems_springboot.enums.EmployeeLevel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeUpdateDto {

    private BigDecimal salary;

    @NotBlank
    private String name;

    @Email(message = "Email is not valid")
    @NotBlank
    private String email;

    private LocalDate dateOfBirth;
    private EmployeeLevel level;

    private String departmentName;

    private Long departmentId;
}
