package charlie.springbootmvc.restervice.controller;

import charlie.springbootmvc.restervice.model.EmployeeDTO;
import charlie.springbootmvc.restervice.services.EmployeeService;
import charlie.springbootmvc.restervice.services.EmployeeServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
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
        EmployeeDTO testEmployee = EmployeeDTO.builder()
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
        EmployeeDTO testEmployee = EmployeeDTO.builder()
                .role("Dev")
                .id(UUID.randomUUID())
                .name("test Employee")
                .age(29)
                .build();
        when(employeeService.getById(testEmployee.getId())).thenReturn(Optional.of(testEmployee));
        mockMvc.perform(get("/api/v1/employees/" + testEmployee.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testEmployee.getId() + "")));

    }

    @Test
    public void getEmployees() throws Exception {
        EmployeeServiceImpl employeeService1 = new EmployeeServiceImpl();
        List<EmployeeDTO> testEmployees = employeeService1.listEmployees();
        when(employeeService.listEmployees()).thenReturn(testEmployees);
        mockMvc.perform(get("/api/v1/employees").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    public void testCreateEmployee() throws Exception {
        EmployeeDTO newEmployee = EmployeeDTO.builder()
                .role("Dev")
                .name("test Employee")
                .age(29)
                .build();
        EmployeeDTO createdEmployee = EmployeeDTO.builder()
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
    public void testEmployeeNameValidation() throws Exception {
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .age(23).build();
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/employees")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isBadRequest()).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        EmployeeDTO newEmployee = EmployeeDTO.builder()
                .role("Dev")
                .name("test Employee")
                .age(29)
                .build();
        UUID id = UUID.randomUUID();
        when(employeeService.updateById(any(), any())).thenReturn(Optional.of(EmployeeDTO.builder().build()));
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
        when(employeeService.deleteById(any())).thenReturn(true);
        mockMvc.perform(delete("/api/v1/employees/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        ArgumentCaptor<UUID> uuidCapture = ArgumentCaptor.forClass(UUID.class);
        verify(employeeService).deleteById(uuidCapture.capture());
        Assertions.assertEquals(id, uuidCapture.getValue());
    }

    @Test
    public void testPatchEmployee() throws Exception {
        UUID id = UUID.randomUUID();
        EmployeeDTO empInfo = EmployeeDTO.builder()
                .name("test").build();
        mockMvc.perform(patch("/api/v1/employees/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empInfo))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidCapture = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<EmployeeDTO> employeeArgumentCaptor = ArgumentCaptor.forClass(EmployeeDTO.class);
        verify(employeeService).patchEmployee(uuidCapture.capture(), employeeArgumentCaptor.capture());
        assertEquals(id, uuidCapture.getValue());
        assertEquals(empInfo, employeeArgumentCaptor.getValue());
    }

    @Test
    public void testNotFoundException() throws Exception {
        UUID id = UUID.randomUUID();
        when(employeeService.getById(id)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/employees/{employeeId}", id))
                .andExpect(status().isNotFound());
    }
}
