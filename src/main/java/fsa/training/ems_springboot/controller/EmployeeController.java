package fsa.training.ems_springboot.controller;


import fsa.training.ems_springboot.enums.EmployeeLevel;
import fsa.training.ems_springboot.model.entity.Employee;
import fsa.training.ems_springboot.service.EmployeeService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    private final List<EmployeeLevel> employeeLevels; // LinkedList of JUNIOR , SENIOR

    public EmployeeController(@Qualifier("employeeServiceImpl") EmployeeService employeeService,
                              @Qualifier("managerLevels") List<EmployeeLevel> employeeLevels) {
        this.employeeService = employeeService;
        this.employeeLevels = employeeLevels;
    }


    @GetMapping("/employees")
    public String getEmployeeList() {
        List<Employee> employeeList = employeeService.getALl();
        System.out.println("employeeList = " + employeeList);
        return "/employee/list";
    }

}
