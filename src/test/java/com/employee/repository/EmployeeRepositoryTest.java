package com.employee.repository;



import com.employee.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee("John", "Doe", "1", "john.doe@example.com", "Software Engineer");
    }

    @Test
    void saveEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);
        assertNotNull(savedEmployee);
        assertEquals(employee.getEmail(), savedEmployee.getEmail());
    }

    @Test
    void findEmployeeById() {
        employeeRepository.save(employee);
        Optional<Employee> foundEmployee = employeeRepository.findById(employee.getId());
        assertTrue(foundEmployee.isPresent());
        assertEquals(employee.getEmail(), foundEmployee.get().getEmail());
    }

    @Test
    void updateEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);
        savedEmployee.setTitle("Senior Software Engineer");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);
        assertEquals("Senior Software Engineer", updatedEmployee.getTitle());
    }

    @Test
    void deleteEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);
        employeeRepository.deleteById(savedEmployee.getId());
        Optional<Employee> deletedEmployee = employeeRepository.findById(savedEmployee.getId());
        assertFalse(deletedEmployee.isPresent());
    }
}
