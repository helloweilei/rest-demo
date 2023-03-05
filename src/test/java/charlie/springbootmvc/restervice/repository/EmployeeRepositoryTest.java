package charlie.springbootmvc.restervice.repository;

import charlie.springbootmvc.restervice.entities.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTest {
    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    public void testCreateEmployee() {
        Employee saved = employeeRepository.save(
                Employee.builder()
                        .name("Wei lei")
                        .build()
        );
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isInstanceOf(UUID.class);
    }
}