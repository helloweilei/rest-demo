package charlie.springbootmvc.restervice.bootstrap;

import charlie.springbootmvc.restervice.entities.Employee;
import charlie.springbootmvc.restervice.model.EmployeeDTO;
import charlie.springbootmvc.restervice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final EmployeeRepository employeeRepository;
    @Override
    public void run(String... args) throws Exception {
        if (employeeRepository.count() > 0) {
            return;
        }
        Employee employee1 = Employee.builder()
                .name("Charlie")
                .age(25)
                .apartment("R&D")
                .hiredDate(Instant.now())
                .role("Dev")
                .build();
        Employee employee2 = Employee.builder()
                .name("Li Lei")
                .age(26)
                .apartment("People")
                .hiredDate(Instant.now())
                .role("HR")
                .build();
        Employee employee3 = Employee.builder()
                .name("Han Meimei")
                .age(21)
                .apartment("TD")
                .hiredDate(Instant.ofEpochSecond(LocalDate.of(2023, 2, 26).toEpochDay()))
                .role("QA")
                .build();
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);
    }
}
