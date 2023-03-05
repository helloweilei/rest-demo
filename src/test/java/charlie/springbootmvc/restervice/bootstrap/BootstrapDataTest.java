package charlie.springbootmvc.restervice.bootstrap;

import charlie.springbootmvc.restervice.model.EmployeeDTO;
import charlie.springbootmvc.restervice.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class BootstrapDataTest {

    @Autowired
    EmployeeRepository employeeRepository;
    BootstrapData bootstrapData;

    @BeforeEach
    public void setup() {
        bootstrapData = new BootstrapData(employeeRepository);
    }

    @Test
    void testRun() throws Exception {
        bootstrapData.run();
        Assertions.assertThat(employeeRepository.count()).isEqualTo(3);
    }
}