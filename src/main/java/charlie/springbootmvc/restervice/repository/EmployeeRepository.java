package charlie.springbootmvc.restervice.repository;

import charlie.springbootmvc.restervice.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
}
