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
import java.util.stream.Collectors;


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
        Optional<Department> department = departmentService.getById(employee.getDepartment().getId());
        DepartmentListDto departmentListDto = new DepartmentListDto();
        BeanUtils.copyProperties(department.get(), departmentListDto);
        employeeDetailDto.setDepartment(departmentListDto);
        System.out.println("employeeDetailDto" + employeeDetailDto);
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

        EmployeeUpdateDto employeeUpdateDto = new EmployeeUpdateDto();
        Employee employee = employeeOptional.get();
        BeanUtils.copyProperties(employee, employeeUpdateDto);


        Department departmentSave = employee.getDepartment();
        BeanUtils.copyProperties(departmentSave, employeeUpdateDto);
//        employeeUpdateDto.setDepartment(new DepartmentListDto(departmentSave.getId(), departmentSave.getName()));

        List<Department> departmentList = departmentService.getALl();
        List<DepartmentListDto> departmentListDtoList = departmentList.stream()
                .map(department -> new DepartmentListDto(department.getId(), department.getName()))
                .collect(Collectors.toList());

        model.addAttribute("departmentList", departmentListDtoList);


        model.addAttribute("employeeFormData", employeeUpdateDto);
        model.addAttribute("employeeId", id);

        return "employee/form";
    }

    @PostMapping("/employees/update/{id}")
    public String updateEmployee(@PathVariable Long id, @Valid
                                 @ModelAttribute("employeeFormData") EmployeeUpdateDto employeeUpdateDto,
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
//        save employee
        // Set the selected department
        Optional<Department> department = departmentService.getByName(employeeUpdateDto.getDepartmentName());
        if (department.isEmpty()) {
            return "redirect:/error-not-found";
        }
        employee.setDepartment(department.get());

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

        List<Department> departmentList = departmentService.getALl();
        List<DepartmentListDto> departmentListDtoList = departmentList.stream()
                .map(department -> new DepartmentListDto(department.getId(), department.getName()))
                .collect(Collectors.toList());
        model.addAttribute("departmentList", departmentListDtoList);

        return "employee/form";
    }


    @PostMapping("/employees/add")
//    @ModelAttribute("employeeAddDTO")
    public String addEmployee(@ModelAttribute("employeeFormData") @Valid EmployeeAddDto employeeAddDTO,
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

        // Set the selected department
        Optional<Department> department = departmentService.getByName(employeeAddDTO.getDepartmentName());
        if (department.isEmpty()) {
            return "redirect:/error-not-found";
        }
        employee.setDepartment(department.get());

        employeeService.create(employee);

        return "redirect:/employees?success=add";
    }

    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        Optional<Employee> employeeOptional = employeeService.getById(id);

        if (employeeOptional.isEmpty()) {
            return "redirect:/error-not-found";
        }
        employeeOptional.get().setDeleted(true);
        employeeService.update(employeeOptional.get());

        return "redirect:/employees?success=delete";
    }

}
