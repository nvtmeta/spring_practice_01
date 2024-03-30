package fsa.training.ems_springboot.controller;


import fsa.training.ems_springboot.enums.EmployeeLevel;
import fsa.training.ems_springboot.model.dto.*;
import fsa.training.ems_springboot.model.entity.Department;
import fsa.training.ems_springboot.model.entity.Employee;
import fsa.training.ems_springboot.service.DepartmentService;
import fsa.training.ems_springboot.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public EmployeeController(EmployeeService employeeServiceImpl, DepartmentService departmentService) {
        this.employeeService = employeeServiceImpl;
        this.departmentService = departmentService;
    }


    // /employees?page=0&size=3&sort=firstName,desc&sort=lastName,asc
//    @GetMapping("/employees")
//    public String showEmployeeList(
////                                    @RequestParam(defaultValue = "0") int page,
////                                   @RequestParam(defaultValue = "3") int size,
////                                  @RequestParam List<String> sort,
////                                    @RequestParam(required = false, name = "q") String keyword,
//                                    @RequestParam(name = "q") Optional<String> keywordOpt,
//                                    @RequestParam(name = "level") Optional<EmployeeLevel> levelOpt,
//                                    @PageableDefault(page = 0, size = 3) Pageable pageable,
//                                    Model model) {
//
//        Page<Employee> employeePage;
//        Specification<Employee> spec = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), false);
//        if (keywordOpt.isPresent()) {
//            String keyword = keywordOpt.get();
//            Specification<Employee> specKeyword = (root, query, criteriaBuilder) -> criteriaBuilder.or(
//                        criteriaBuilder.like(root.get("name"), "%" + keyword + "%"),
//                        criteriaBuilder.like(root.get("email"), "%" + keyword + "%")
//            );
//            spec = spec.and(specKeyword);
//        }
//        if (levelOpt.isPresent()) {
//            EmployeeLevel level = levelOpt.get();
//            Specification<Employee> specFilter = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("level"), level);
//            spec = spec.and(specFilter);
//        }
//        employeePage = employeeService.getAll(spec, pageable);
//
//        model.addAttribute("employees", employeePage);
//        return "employee/list";
//    }



    @GetMapping("/employees")
    public String showEmployeeList(
            @RequestParam(name = "q") Optional<String> keywordOpt,
            @RequestParam(name = "level") Optional<EmployeeLevel> levelOpt,
            @PageableDefault(page = 0, size = 3) Pageable pageable,
            Model model) {
        Page<EmployeeListDto> employeePage = employeeService
                .getEmployeePage(keywordOpt.orElse(null), levelOpt.orElse(null), pageable);

        model.addAttribute("employees", employeePage);
        return "employee/list";
    }


//    TODO: Add employee , enum dynamic select level , and read department
//     from database to add employee with department
// TODO: convert to REST API

    @GetMapping("/employees/{id}") // /employees/1, /employees/2
    public String showDetailEmployee(@PathVariable(required = false) Long id, Model model) {
        Optional<Employee> employeeOptional = employeeService.getById(id);
//        Employee employee1 = employee.orElseThrow(IllegalAccessError::new);
//        employee.ifPresent(value -> model.addAttribute("employee", value));
        if (employeeOptional.isEmpty()) {
            return "redirect:/error-not-found";
        }
        Employee employee = employeeOptional.get();
//        convert to employee entity to dto

        EmployeeDetailDto employeeDetailDto = new EmployeeDetailDto();
        BeanUtils.copyProperties(employee, employeeDetailDto);

        Department department = employee.getDepartment();
        if (department != null) {
//            employeeDetailDto.setDepartment(new DepartmentListDto(department.getId(), department.getName()));
        }
        model.addAttribute("employee", employeeDetailDto);
        return "employee/detail"; // FIXME: Show detail
    }

//    @GetMapping("/employees/detail") // /employees/detail?id=1
//    public String showDetailEmployee1(@RequestParam(required = false) Long id, Model model) {
//
//        Optional<Employee> employeeOptional = employeeService.getById(id);
//        System.out.println("employeeOptional" + employeeOptional);
//        if (employeeOptional.isEmpty()) {
//            return "redirect:/error-not-found";
//        }//        convert to employee entity to dto
//        EmployeeDetailDto employeeDetailDto = new EmployeeDetailDto();
//        Employee employee = employeeOptional.get();
//
//        BeanUtils.copyProperties(employee, employeeDetailDto);
//        System.out.println("employeeDetailDto" + employeeDetailDto);
//
////        set to model attribute
//        model.addAttribute("employee", employeeDetailDto);
//        return "employee/detail"; // FIXME: Show detail
//    }


    @GetMapping("/employees/update/{id}")
    public String showUpdateEmployee(@PathVariable Long id, Model model) {

        Optional<Employee> employeeOptional = employeeService.getById(id);
        if (employeeOptional.isEmpty()) {
            return "redirect:/error-not-found";
        }//
        prepareMasterData(model);
        Employee employee = employeeOptional.get();

        EmployeeFormDto employeeUpdateDto = new EmployeeFormDto();
        BeanUtils.copyProperties(employee, employeeUpdateDto);

        if (employee.getDepartment() != null) {
            employeeUpdateDto.setDepartmentId(employee.getDepartment().getId());
        }

        model.addAttribute("employeeFormData", employeeUpdateDto);
        model.addAttribute("employeeId", id);

        return "employee/form";
    }

    @PostMapping("/employees/update/{id}")
    public String updateEmployee(@PathVariable Long id, @Valid
    @ModelAttribute("employeeFormData") EmployeeFormDto employeeUpdateDto,
                                 BindingResult bindingResult
    ) {
        Optional<Employee> employeeOptional = employeeService.getById(id);

        if (employeeOptional.isEmpty()) {
            return "redirect:/error-not-found";
        }
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "employee/form";
        }

        Employee employee = employeeOptional.get();

        //FIXME Update
        BeanUtils.copyProperties(employeeUpdateDto, employee, "email");

//        update department
        Department department = null;
        if (employeeUpdateDto.getDepartmentId() != null) {
            Optional<Department> departmentOptional = departmentService.getById(employeeUpdateDto.getDepartmentId());
            if (departmentOptional.isPresent()) {
                department = departmentOptional.get();
            }
        }
        employee.setDepartment(department);

        employeeService.update(employee);

        return "redirect:/employees?success=update";
    }

    //    add employee
    @GetMapping("/employees/add")
    public String addEmployee(Model model) {

        prepareMasterData(model);
        model.addAttribute("employeeFormData", new EmployeeFormDto());

        return "employee/form";
    }


    @PostMapping("/employees/add")
//    @ModelAttribute("employeeAddDTO")
    public String addEmployee(@ModelAttribute("employeeFormData") @Valid EmployeeFormDto employeeAddDTO,
                              BindingResult bindingResult) {

//        TODO: validate
        if (bindingResult.hasErrors()) {
            System.out.println("bindingResult.getAllErrors()" + bindingResult.getAllErrors());
//            System.out.println(bindingResult.getAllErrors());
            return "employee/form";
        }

//        check if email is duplicated, using services,
        if (employeeService.existsByEmail(employeeAddDTO.getEmail())) {
            bindingResult.rejectValue("email", "employee.email.exists");
            System.out.println("bindingResult.getAllErrors()" + bindingResult.getAllErrors());

            return "employee/form";
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeAddDTO, employee);


//        we just need only id to update department with employee,
//        when insert unknown department, we will be constraint,
        Department department = null;
        if (employeeAddDTO.getDepartmentId() != null) {
            Optional<Department> departmentOptional = departmentService.getById(employeeAddDTO.getDepartmentId());
            if (departmentOptional.isPresent()) {
                department = departmentOptional.get();
            }
        }
        employee.setDepartment(department);

        employeeService.create(employee);

        return "redirect:/employees?success=add";
    }

    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        Optional<Employee> employeeOptional = employeeService.getById(id);

        if (employeeOptional.isEmpty()) {
            return "redirect:/error-not-found";
        }
        employeeService.deleteById(id);

        return "redirect:/employees?success=delete";
    }


    public void prepareMasterData(Model model) {
        List<SelectOptionDto> departmentList = departmentService.getALl().stream()
                .map(department -> new SelectOptionDto(department.getId().toString(), department.getName()))
                .toList();

        model.addAttribute("departments", departmentList);
    }

}
