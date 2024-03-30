package fsa.training.ems_springboot.resource;

import fsa.training.ems_springboot.enums.EmployeeLevel;
import fsa.training.ems_springboot.model.dto.EmployeeDetailDto;
import fsa.training.ems_springboot.model.dto.EmployeeFormDto;
import fsa.training.ems_springboot.model.dto.EmployeeListDto;
import fsa.training.ems_springboot.model.entity.Department;
import fsa.training.ems_springboot.model.entity.Employee;
import fsa.training.ems_springboot.service.DepartmentService;
import fsa.training.ems_springboot.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/employees")
public class EmployeeResource {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public EmployeeResource(EmployeeService employeeServiceImpl, DepartmentService departmentService) {
        this.employeeService = employeeServiceImpl;
        this.departmentService = departmentService;
    }

    @GetMapping("")
    public Page<EmployeeListDto> showEmployeeList(
            @RequestParam(name = "q") Optional<String> keywordOpt,
            @RequestParam(name = "level") Optional<EmployeeLevel> levelOpt,
            @PageableDefault(page = 0, size = 3) Pageable pageable) {

        return employeeService
                .getEmployeePage(keywordOpt.orElse(null), levelOpt.orElse(null), pageable);
    }

    @GetMapping("/{id}") // /employees/1, /employees/2
    public EmployeeDetailDto showDetailEmployee(@PathVariable(required = false) Long id) {
        Optional<Employee> employeeOptional = employeeService.getById(id);

        if (employeeOptional.isEmpty()) {
            return null;
        }

        EmployeeDetailDto employeeDetailDto = new EmployeeDetailDto();
        BeanUtils.copyProperties(employeeOptional.get(), employeeDetailDto);

        Department department = employeeOptional.get().getDepartment();
        if (department != null) {
//            employeeDetailDto.setDepartment(new DepartmentListDto(department.getId(), department.getName()));
        }
        return employeeDetailDto;
    }


    @PutMapping("/{id}")
    public void showUpdateEmployee(@PathVariable Long id, @RequestBody EmployeeFormDto employeeUpdateDto) {

        Optional<Employee> employeeOptional = employeeService.getById(id);
        if (employeeOptional.isEmpty()) {
            return;
        }//
        Employee employee = employeeOptional.get();

        BeanUtils.copyProperties(employeeUpdateDto, employee, "email");

        if (employee.getDepartment() != null) {
            employeeUpdateDto.setDepartmentId(employee.getDepartment().getId());
        }

    }


    @PostMapping
//    @ModelAttribute("employeeAddDTO")
    public ResponseEntity addEmployee(@RequestBody @Valid EmployeeFormDto employeeFormDto) {

//TODO : validate
        if (employeeService.existsByEmail(employeeFormDto.getEmail())) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeFormDto, employee);

        if (employeeFormDto.getDepartmentId() != null) {
            employee.setDepartment(new Department(employeeFormDto.getDepartmentId()));

        }

        employeeService.create(employee);
        BeanUtils.copyProperties(employeeFormDto, EmployeeDetailDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeFormDto);
    }

//    @GetMapping("/employees/delete/{id}")
//    public String deleteEmployee(@PathVariable Long id) {
//        Optional<Employee> employeeOptional = employeeService.getById(id);
//
//        if (employeeOptional.isEmpty()) {
//            return "redirect:/error-not-found";
//        }
//        employeeService.deleteById(id);
//
//        return "redirect:/employees?success=delete";
//    }
//
//
//    public void prepareMasterData(Model model) {
//        List<SelectOptionDto> departmentList = departmentService.getALl().stream()
//                .map(department -> new SelectOptionDto(department.getId().toString(), department.getName()))
//                .toList();
//
//        model.addAttribute("departments", departmentList);
//    }

}
