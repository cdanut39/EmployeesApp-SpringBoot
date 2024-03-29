package com.learning.spring.rest.employees.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.learning.spring.rest.employees.dto.EmployeeDTO;
import com.learning.spring.rest.employees.services.EmployeeServiceImpl;
import com.learning.spring.rest.employees.utils.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static com.learning.spring.rest.employees.utils.Constants.EMPLOYEE_ADDED;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
public class EmployeeControllerTests {

    @Mock
    private EmployeeServiceImpl employeeService;

    @InjectMocks
    EmployeeController employeeController;

    private MockMvc mockMvc;

    private EmployeeDTO employeeDTO1;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        EmployeeDTO.EmployeeDTOBuilder builder = new EmployeeDTO.EmployeeDTOBuilder();
        employeeDTO1 = builder.setUserId(1).setFirstName("Danut").setCommunityName("QA").setLastName("Cristea").setEmail("dancristea@sv.ro").
                setPhoneNumber(123456789).build();
    }

    @Test
    public void getEmployeeByIdTest() throws Exception {
        when(employeeService.getEmployeeById(employeeDTO1.getUserId())).thenReturn(employeeDTO1);
        mockMvc.perform(get("/employee/{id}", employeeDTO1.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email", is(employeeDTO1.getEmail())));
    }

    @Test
    public void getAllEmployeesTest() throws Exception {
        EmployeeDTO.EmployeeDTOBuilder builder = new EmployeeDTO.EmployeeDTOBuilder();
        EmployeeDTO employeeDTO2 = builder.setUserId(2).setFirstName("Georgian").setCommunityName("Java").setLastName("Cristea").setEmail("georgiancristea@sv.ro").build();

        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(employeeDTO1, employeeDTO2));
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("[0].email", is(employeeDTO1.getEmail())))
                .andExpect(jsonPath("[1].email", is(employeeDTO2.getEmail())));
    }

    @Test
    public void addEmployeeTest() throws Exception {

        when(employeeService.save(any(EmployeeDTO.class))).thenReturn(employeeDTO1);


        mockMvc.perform(post("/register/employee")
                .contentType("application/json")
                .content(asJsonString(employeeDTO1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message", is(EMPLOYEE_ADDED)));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }
}
