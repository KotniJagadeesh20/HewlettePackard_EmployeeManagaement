package com.employee.controller;



import com.employee.contoller.EmployeeController;
import com.employee.entity.Employee;
import com.employee.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        employee = new Employee("John", "Doe", "1", "john.doe@example.com", "Software Engineer");
    }

    @Test
    void getAllEmployees() throws Exception {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].first_name").value("John"));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void getEmployeeById() throws Exception {
        when(employeeService.getEmployeeById(employee.getId())).thenReturn(employee);

        mockMvc.perform(get("/employees/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value("John"));

        verify(employeeService, times(1)).getEmployeeById(employee.getId());
    }

    @Test
    void addEmployee() throws Exception {
        when(employeeService.addEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"first_name\": \"John\", \"last_name\": \"Doe\", \"employee_id\": \"1\", \"email\": \"john.doe@example.com\", \"title\": \"Software Engineer\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.first_name").value("John"));

        verify(employeeService, times(1)).addEmployee(any(Employee.class));
    }

    @Test
    void updateEmployee() throws Exception {
        Employee updatedEmployee = new Employee("Jane", "Doe", "1", "jane.doe@example.com", "Senior Engineer");
        when(employeeService.updateEmployee(anyLong(), any(Employee.class))).thenReturn(updatedEmployee);

        mockMvc.perform(put("/employees/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"first_name\": \"Jane\", \"last_name\": \"Doe\", \"employee_id\": \"1\", \"email\": \"jane.doe@example.com\", \"title\": \"Senior Engineer\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value("Jane"));

        verify(employeeService, times(1)).updateEmployee(anyLong(), any(Employee.class));
    }

    @Test
    void deleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(anyLong());

        mockMvc.perform(delete("/employees/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(employeeService, times(1)).deleteEmployee(anyLong());
    }
}
