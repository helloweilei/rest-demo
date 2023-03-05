package charlie.springbootmvc.restervice.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class EmployeeDTO {
    private UUID id;
    private String name;
    private String apartment;
    private int age;
    private Instant hiredDate;
    private String role;
}
