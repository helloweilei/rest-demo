package charlie.springbootmvc.restervice.services;

import charlie.springbootmvc.restervice.model.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    List<Employee> listEmployees();
    Employee getById(UUID id);

    Employee newEmployee(Employee employee);

    void updateById(UUID id, Employee employee);

    void deleteById(UUID id);
    Employee patchEmployee(UUID id, Employee employee);
}
