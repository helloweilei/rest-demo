package charlie.springbootmvc.restervice.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class EmployeeDTO {
    private UUID id;

    @NotNull
    @NotBlank
    private String name;

    private String apartment;
    private int age;
    private Instant hiredDate;
    private String role;
    private String email;
}
