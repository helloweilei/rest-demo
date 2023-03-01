package charlie.springbootmvc.restervice.controller;

import charlie.springbootmvc.restervice.model.Employee;
import charlie.springbootmvc.restervice.services.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/employees")
@Slf4j
public class EmployeeController {

    private  final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Employee> listEmployees() {
        return this.employeeService.listEmployees();
    }

    @RequestMapping(value = "/{employeeId}", method = RequestMethod.GET)
    public Employee getEmployeeById(@PathVariable("employeeId") UUID id) {
        log.debug("[Debug]::getEmployeeById: " + id);
        return this.employeeService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = this.employeeService.newEmployee(employee);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/employees/" + savedEmployee.getId());
        return new ResponseEntity<>(savedEmployee, headers, HttpStatus.CREATED);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity updateById(@PathVariable("employeeId") UUID id,@RequestBody Employee employee) {
        this.employeeService.updateById(id, employee);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping ("/{employeeId}")
    public ResponseEntity patchById(@PathVariable("employeeId") UUID id,@RequestBody Employee employee) {
        this.employeeService.patchEmployee(id, employee);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity deleteById(@PathVariable("employeeId") UUID id) {
        this.employeeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity handleNotFound() {
//        return new ResponseEntity(HttpStatus.NOT_FOUND);
//    }
}
