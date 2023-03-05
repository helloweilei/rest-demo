package charlie.springbootmvc.restervice.services;

import charlie.springbootmvc.restervice.model.EmployeeDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeService {
    List<EmployeeDTO> listEmployees();
    Optional<EmployeeDTO> getById(UUID id);

    EmployeeDTO newEmployee(EmployeeDTO employee);

    Optional<EmployeeDTO> updateById(UUID id, EmployeeDTO employee);

    boolean deleteById(UUID id);
    EmployeeDTO patchEmployee(UUID id, EmployeeDTO employee);
}
