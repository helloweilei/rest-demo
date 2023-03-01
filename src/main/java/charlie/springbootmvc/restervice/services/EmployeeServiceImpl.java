package charlie.springbootmvc.restervice.services;

import charlie.springbootmvc.restervice.model.Employee;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;


@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final Map<UUID, Employee> employeeMap;

    public EmployeeServiceImpl() {
        Employee employee1 = Employee.builder()
                .id(UUID.randomUUID())
                .name("Charlie")
                .age(25)
                .apartment("R&D")
                .hiredDate(Instant.now())
                .role("Dev")
                .build();
        Employee employee2 = Employee.builder()
                .id(UUID.randomUUID())
                .name("Li Lei")
                .age(26)
                .apartment("People")
                .hiredDate(Instant.now())
                .role("HR")
                .build();
        Employee employee3 = Employee.builder()
                .id(UUID.randomUUID())
                .name("Han Meimei")
                .age(21)
                .apartment("TD")
                .hiredDate(Instant.ofEpochSecond(LocalDate.of(2023, 2, 26).toEpochDay()))
                .role("QA")
                .build();

        this.employeeMap = Stream.of(employee1, employee2, employee3)
                .collect(HashMap<UUID, Employee>::new,
                        (map, emp) -> map.put(emp.getId(), emp),
                        HashMap::putAll);
    }

    @Override
    public List<Employee> listEmployees() {
        return new ArrayList<>(this.employeeMap.values());
    }

    @Override
    public Employee getById(UUID id) {
        return this.employeeMap.get(id);
    }

    @Override
    public Employee newEmployee(Employee employee) {
        Employee saved = Employee.builder()
                .id(UUID.randomUUID())
                .role(employee.getRole())
                .name(employee.getName())
                .hiredDate(employee.getHiredDate())
                .apartment(employee.getApartment())
                .age(employee.getAge())
                .build();
        employeeMap.put(saved.getId(), saved);
        return saved;
    }

    @Override
    public Employee patchEmployee(UUID id, Employee employee) {
        Employee existing = employeeMap.get(id);
        if (StringUtils.hasText(employee.getName())) {
            existing.setName(employee.getName());
        }
        if (null != employee.getRole()) {
            existing.setRole(employee.getRole());
        }
        // ...
        return existing;
    }

    @Override
    public void updateById(UUID id, Employee employee) {
        Employee existing = employeeMap.get(id);
        if (null == existing) {
            throw new RuntimeException("employee not exists");
        }
        existing.setAge(employee.getAge());
        existing.setApartment(employee.getApartment());
        existing.setRole(employee.getRole());
        existing.setHiredDate(employee.getHiredDate());

        employeeMap.put(id, existing);
    }

    @Override
    public void deleteById(UUID id) {
        this.employeeMap.remove(id);
    }
}
