package charlie.springbootmvc.restervice.controller;

import charlie.springbootmvc.restervice.entities.Employee;
import charlie.springbootmvc.restervice.mappers.EmployeeMapper;
import charlie.springbootmvc.restervice.model.EmployeeDTO;
import charlie.springbootmvc.restervice.repository.EmployeeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.JsonPath;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.net.ssl.SSLEngineResult;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class EmployeeControllerApiTest {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeController employeeController;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
     void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testJpaValidation() throws Exception {
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .name("weilei").build();
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employees")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Is.is(1)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    public void testListEmployees() {

        List<EmployeeDTO> employees = employeeController.listEmployees();
        assertThat(employees.size()).isEqualTo(3);
    }

    @Test
    @Transactional
    @Rollback
    public void testEmptyList() {
        employeeRepository.deleteAll();
        List<EmployeeDTO> employees = employeeController.listEmployees();
        assertThat(employees.size()).isEqualTo(0);
    }

    @Test
    public void testGetById() {
        Employee existing = employeeRepository.findAll().get(0);
        EmployeeDTO foundEmployee = employeeController.getEmployeeById(existing.getId());
        assertThat(foundEmployee).isNotNull();
    }

    @Test
    public void testEmployeeNotFound() {
        assertThrows(NotFoundException.class, () -> employeeController.getEmployeeById(UUID.randomUUID()));
    }

    @Transactional
    @Rollback
    @Test
    public void testCreateEmployee() {
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .name("Test1")
                .role("UI").build();
        ResponseEntity<?> responseEntity = employeeController.createEmployee(employeeDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String locationPath = responseEntity.getHeaders().getLocation().getPath();
        UUID createdId = UUID.fromString(locationPath.substring(locationPath.lastIndexOf("/") + 1));
        Employee savedEmployee = employeeRepository.findById(createdId).get();

        assertThat(savedEmployee).isNotNull();

    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateEmployee() {
        Employee employee = employeeRepository.findAll().get(0);
        EmployeeDTO employeeDTO = employeeMapper.employeeToEmployeeDTO(employee);
        employeeDTO.setId(null);
        employeeDTO.setName("new test name");
        ResponseEntity<?> responseEntity = employeeController.updateById(employee.getId(), employeeDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        Employee updated = employeeRepository.findById(employee.getId()).get();

        assertThat(updated.getName()).isEqualTo("new test name");

    }

    @Test
    public void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> employeeController.updateById(
                UUID.randomUUID(),
                EmployeeDTO.builder().build()
        ));
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteById() {
        Employee employee = employeeRepository.findAll().get(0);
        ResponseEntity<?> responseEntity = employeeController.deleteById(employee.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(employeeRepository.findById(employee.getId())).isEmpty();
    }

    @Test
    public void testDeleteNotExisting() {
        assertThrows(NotFoundException.class, () -> employeeController.deleteById(UUID.randomUUID()));
    }

}
