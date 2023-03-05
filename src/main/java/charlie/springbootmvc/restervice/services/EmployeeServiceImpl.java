package charlie.springbootmvc.restervice.services;

import charlie.springbootmvc.restervice.model.EmployeeDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;


@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final Map<UUID, EmployeeDTO> employeeMap;

    public EmployeeServiceImpl() {
        EmployeeDTO employee1 = EmployeeDTO.builder()
                .id(UUID.randomUUID())
                .name("Charlie")
                .age(25)
                .apartment("R&D")
                .hiredDate(Instant.now())
                .role("Dev")
                .build();
        EmployeeDTO employee2 = EmployeeDTO.builder()
                .id(UUID.randomUUID())
                .name("Li Lei")
                .age(26)
                .apartment("People")
                .hiredDate(Instant.now())
                .role("HR")
                .build();
        EmployeeDTO employee3 = EmployeeDTO.builder()
                .id(UUID.randomUUID())
                .name("Han Meimei")
                .age(21)
                .apartment("TD")
                .hiredDate(Instant.ofEpochSecond(LocalDate.of(2023, 2, 26).toEpochDay()))
                .role("QA")
                .build();

        this.employeeMap = Stream.of(employee1, employee2, employee3)
                .collect(HashMap<UUID, EmployeeDTO>::new,
                        (map, emp) -> map.put(emp.getId(), emp),
                        HashMap::putAll);
    }

    @Override
    public List<EmployeeDTO> listEmployees() {
        return new ArrayList<>(this.employeeMap.values());
    }

    @Override
    public Optional<EmployeeDTO> getById(UUID id) {
        return Optional.of(this.employeeMap.get(id));
    }

    @Override
    public EmployeeDTO newEmployee(EmployeeDTO employee) {
        EmployeeDTO saved = EmployeeDTO.builder()
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
    public EmployeeDTO patchEmployee(UUID id, EmployeeDTO employee) {
        EmployeeDTO existing = employeeMap.get(id);
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
    public Optional<EmployeeDTO> updateById(UUID id, EmployeeDTO employee) {
        EmployeeDTO existing = employeeMap.get(id);
        if (null == existing) {
            throw new RuntimeException("employee not exists");
        }
        existing.setAge(employee.getAge());
        existing.setApartment(employee.getApartment());
        existing.setRole(employee.getRole());
        existing.setHiredDate(employee.getHiredDate());

        employeeMap.put(id, existing);
        return Optional.of(existing);
    }

    @Override
    public boolean deleteById(UUID id) {
        this.employeeMap.remove(id);
        return true;
    }
}
