package charlie.springbootmvc.restervice.model;

import charlie.springbootmvc.restervice.converters.CsvInstantConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;

import java.time.Instant;

@Data
public class EmployeeCsvRecord {
    @CsvBindByName(column = "employeeName")
    private String name;

    @CsvBindByName
    private String apartment;

    @CsvBindByName
    private int age;

    @CsvCustomBindByName(converter = CsvInstantConverter.class)
    private Instant hiredDate;

    @CsvBindByName
    private String role;

    @CsvBindByName
    private String email;
}
