package charlie.springbootmvc.restervice.bootstrap;

import charlie.springbootmvc.restervice.entities.Employee;
import charlie.springbootmvc.restervice.mappers.EmployeeMapper;
import charlie.springbootmvc.restervice.model.EmployeeCsvRecord;
import charlie.springbootmvc.restervice.model.EmployeeDTO;
import charlie.springbootmvc.restervice.repository.EmployeeRepository;
import charlie.springbootmvc.restervice.services.CsvParserService;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final CsvParserService<EmployeeCsvRecord> csvParserService;
    @Override
    public void run(String... args) throws Exception {
        if (employeeRepository.count() > 10) {
            return;
        }
        File employeeFile = ResourceUtils.getFile("classpath:data/employees.csv");
        List<EmployeeCsvRecord> records = csvParserService.parse(employeeFile, EmployeeCsvRecord.class);

        employeeRepository.saveAll(records.stream()
                .map(employeeMapper::employeeCsvRecordToEmployee)
                .toList());
    }
}
