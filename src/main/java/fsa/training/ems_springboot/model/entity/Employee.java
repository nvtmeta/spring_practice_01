package fsa.training.ems_springboot.model.entity;

import fsa.training.ems_springboot.enums.EmployeeLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Nationalized
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "deleted")
    private boolean deleted;

    @Enumerated(EnumType.STRING)
    private EmployeeLevel level;

    private BigDecimal salary;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;

}
