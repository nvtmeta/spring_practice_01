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

    @GetMapping
    public ResponseEntity<Page<EmployeeListDto>> showEmployeeList(
            @RequestParam(name = "q") Optional<String> keywordOpt,
            @RequestParam(name = "level") Optional<EmployeeLevel> levelOpt,
            @PageableDefault(page = 0, size = 3) Pageable pageable) {

        Page<EmployeeListDto> employeePage = employeeService
                .getEmployeePage(keywordOpt.orElse(null), levelOpt.orElse(null), pageable);

        return new ResponseEntity<>(employeePage, HttpStatus.OK);
    }

    @GetMapping("/{id}") // /api/employees/1, /api/employees/2
    public ResponseEntity<EmployeeDetailDto> showDetailEmployee(@PathVariable Long id) {
        Optional<Employee> employeeOptional = employeeService.getById(id);

        if (employeeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Employee employee = employeeOptional.get();
        EmployeeDetailDto employeeDetailDto = new EmployeeDetailDto();
        BeanUtils.copyProperties(employee, employeeDetailDto);

        if (employee.getDepartment() != null) {
            employeeDetailDto.setDepartmentId(employee.getDepartment().getId());
        }

        return ResponseEntity.ok(employeeDetailDto);
    }

    @PostMapping
    public ResponseEntity<?> addEmployee(@RequestBody @Valid EmployeeFormDto employeeFormDto) {

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



    @PutMapping("/{id}")
    public ResponseEntity<?> showUpdateEmployee(@PathVariable Long id, @RequestBody EmployeeFormDto employeeUpdateDto) {

        Optional<Employee> employeeOptional = employeeService.getById(id);
        if (employeeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Employee employee = employeeOptional.get();

        BeanUtils.copyProperties(employeeUpdateDto, employee, "email");

        if (employee.getDepartment() != null) {
            employee.setDepartment(new Department(employeeUpdateDto.getDepartmentId()));
        }

        employeeService.update(employee);

        return ResponseEntity.status(HttpStatus.OK).build();

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        Optional<Employee> employeeOptional = employeeService.getById(id);

        if (employeeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        employeeService.deleteById(id);
        return ResponseEntity.ok().build();
    }


}
