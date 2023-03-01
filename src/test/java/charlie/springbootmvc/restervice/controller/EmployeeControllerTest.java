package charlie.springbootmvc.restervice.controller;

import charlie.springbootmvc.restervice.model.Employee;
import charlie.springbootmvc.restervice.services.EmployeeService;
import charlie.springbootmvc.restervice.services.EmployeeServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({EmployeeController.class})
class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EmployeeService employeeService;

    @Test
    public void testCreateJSON() throws JsonProcessingException {
        // ObjectMapper objectMapper = new ObjectMapper();
        // objectMapper.findAndRegisterModules();
        Employee testEmployee = Employee.builder()
                .role("Dev")
                .id(UUID.randomUUID())
                .name("test Employee")
                .age(29)
                .hiredDate(Instant.now())
                .build();
        System.out.println(objectMapper.writeValueAsString(testEmployee));
    }

    @Test
    void getEmployeeById() throws Exception {
        Employee testEmployee = Employee.builder()
                .role("Dev")
                .id(UUID.randomUUID())
                .name("test Employee")
                .age(29)
                .build();
        when(employeeService.getById(testEmployee.getId())).thenReturn(testEmployee);
        mockMvc.perform(get("/api/v1/employees/" + testEmployee.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testEmployee.getId() + "")));

    }

    @Test
    public void getEmployees() throws Exception {
        EmployeeServiceImpl employeeService1 = new EmployeeServiceImpl();
        List<Employee> testEmployees = employeeService1.listEmployees();
        when(employeeService.listEmployees()).thenReturn(testEmployees);
        mockMvc.perform(get("/api/v1/employees").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    public void testCreateEmployee() throws Exception {
        Employee newEmployee = Employee.builder()
                .role("Dev")
                .name("test Employee")
                .age(29)
                .build();
        Employee createdEmployee = Employee.builder()
                .role("Dev")
                .name("test Employee")
                .age(29)
                .id(UUID.randomUUID())
                .build();
        when(employeeService.newEmployee(newEmployee)).thenReturn(createdEmployee);
        mockMvc.perform(post("/api/v1/employees")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", "/api/v1/employees/" + createdEmployee.getId()));

    }

    @Test
    public void testUpdateEmployee() throws Exception {
        Employee newEmployee = Employee.builder()
                .role("Dev")
                .name("test Employee")
                .age(29)
                .build();
        UUID id = UUID.randomUUID();
        mockMvc.perform(put("/api/v1/employees/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isNoContent());
        verify(employeeService).updateById(id, newEmployee);
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(delete("/api/v1/employees/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        ArgumentCaptor<UUID> uuidCapture = ArgumentCaptor.forClass(UUID.class);
        verify(employeeService).deleteById(uuidCapture.capture());
        Assertions.assertEquals(id, uuidCapture.getValue());
    }

    @Test
    public void testPatchEmployee() throws Exception {
        UUID id = UUID.randomUUID();
        Employee empInfo = Employee.builder()
                .name("test").build();
        mockMvc.perform(patch("/api/v1/employees/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empInfo))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidCapture = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeService).patchEmployee(uuidCapture.capture(), employeeArgumentCaptor.capture());
        assertEquals(id, uuidCapture.getValue());
        assertEquals(empInfo, employeeArgumentCaptor.getValue());
    }

    @Test
    public void testNotFoundException() throws Exception {
        UUID id = UUID.randomUUID();
        when(employeeService.getById(id)).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/api/v1/employees/{employeeId}", id))
                .andExpect(status().isNotFound());
    }
}