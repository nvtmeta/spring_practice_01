package fsa.training.ems_springboot.controller;


import fsa.training.ems_springboot.enums.EmployeeLevel;
import fsa.training.ems_springboot.model.dto.EmployeeAddDto;
import fsa.training.ems_springboot.model.dto.EmployeeDetailDto;
import fsa.training.ems_springboot.model.dto.EmployeeUpdateDto;
import fsa.training.ems_springboot.model.entity.Employee;
import fsa.training.ems_springboot.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


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
    public String getEmployeeList(Model model) {
        List<Employee> employeeList = employeeService.getALl();
        System.out.println("employeeList" + employeeList);
        model.addAttribute("employees", employeeList);
        return "employee/list";
    }


    @GetMapping("/employees/{id}") // /employees/1, /employees/2
    public String showDetailEmployee(@PathVariable(required = false) Long id, Model model) {
        Optional<Employee> employeeOptional = employeeService.getById(id);
//        Employee employee1 = employee.orElseThrow(IllegalAccessError::new);
//        employee.ifPresent(value -> model.addAttribute("employee", value));
        System.out.println("employeeOptional" + employeeOptional);
        if (employeeOptional.isEmpty()) {
            return "redirect:/error-not-found";
        }
        Employee employee = employeeOptional.get();
//        convert to employee entity to dto
        EmployeeDetailDto employeeDetailDto = new EmployeeDetailDto();
        BeanUtils.copyProperties(employee, employeeDetailDto);
        System.out.println("employeeDetailDto" + employeeDetailDto);

//        set to model attribute
        model.addAttribute("employee", employeeDetailDto);

        return "employee/detail"; // FIXME: Show detail
    }

    @GetMapping("/employees/detail") // /employees/detail?id=1
    public String showDetailEmployee1(@RequestParam(required = false) Long id, Model model) {

        Optional<Employee> employeeOptional = employeeService.getById(id);
        System.out.println("employeeOptional" + employeeOptional);
        if (employeeOptional.isEmpty()) {
            return "redirect:/error-not-found";
        }//        convert to employee entity to dto
        EmployeeDetailDto employeeDetailDto = new EmployeeDetailDto();
        Employee employee = employeeOptional.get();

        BeanUtils.copyProperties(employee, employeeDetailDto);
        System.out.println("employeeDetailDto" + employeeDetailDto);

//        set to model attribute
        model.addAttribute("employee", employeeDetailDto);
        return "employee/detail"; // FIXME: Show detail
    }


    @GetMapping("/employees/update/{id}")
    public String showUpdateEmployee(@PathVariable Long id, Model model) {

//        get employee by id
        Optional<Employee> employeeOptional = employeeService.getById(id);
        System.out.println("employeeOptional" + employeeOptional);
//        2. if employee invalid, show errors
        if (employeeOptional.isEmpty()) {
            return "redirect:/error-not-found";
        }//

//        2.convert entity to dto and requestdispatcher
        EmployeeUpdateDto employeeUpdateDto = new EmployeeUpdateDto();
        Employee employee = employeeOptional.get();
        BeanUtils.copyProperties(employee, employeeUpdateDto);
        model.addAttribute("employeeFormData", employeeUpdateDto);
        model.addAttribute("employeeId", id);
        return "employee/form";
    }

    @PostMapping("/employees/update/{id}")
    public String updateEmployee(@PathVariable Long id,
                                 @ModelAttribute("employeeFormData") EmployeeUpdateDto employeeUpdateDto,
                                 BindingResult bindingResult
    ) {
        Optional<Employee> employeeOptional = employeeService.getById(id);

        if (employeeOptional.isEmpty()) {
            return "redirect:/error-not-found";
        }
        Employee employee = employeeOptional.get();

//FIXME Update
        BeanUtils.copyProperties(employeeUpdateDto, employee, "email");
        System.out.println("employee _update" + employee);
//        save employee
        employeeService.update(employee);

//        FIXME NOTIFICATION
//        TODO Record is updated, sort to top,
//        use redirect, we can not use addAttribute, so we have to send data by url link
// send param to list.html

        return "redirect:/employees?success=update";
    }

    //    add employee
    @GetMapping("/employees/add")
    public String addEmployee(Model model) {
        model.addAttribute("employeeFormData", new EmployeeAddDto());
        return "employee/form";
    }


    @PostMapping("/employees/add")
//    @ModelAttribute("employeeAddDTO")
    public String addEmployee(@ModelAttribute("employeeFormData") @Valid EmployeeAddDto employeeAddDTO,
                              BindingResult bindingResult) {

//        TODO: validate
        if (bindingResult.hasErrors()) {
//            System.out.println(bindingResult.getAllErrors());
            return "employee/form";
        }

//        check if email is duplicated, using services,
        if (employeeService.existsByEmail(employeeAddDTO.getEmail())) {
            bindingResult.rejectValue("email", "employee.email.exists");

            return "employee/form";
        }

        Employee employee = new Employee();

        BeanUtils.copyProperties(employeeAddDTO, employee);
        System.out.println("employee" + employee);
        employeeService.create(employee);

        return "redirect:/employees?success=add";
    }

}
