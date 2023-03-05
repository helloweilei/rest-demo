package charlie.springbootmvc.restervice.mappers;

import charlie.springbootmvc.restervice.entities.Employee;
import charlie.springbootmvc.restervice.model.EmployeeDTO;
import org.mapstruct.Mapper;

@Mapper
public interface EmployeeMapper {
    Employee employeeDTOToEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO employeeToEmployeeDTO(Employee employee);
}
