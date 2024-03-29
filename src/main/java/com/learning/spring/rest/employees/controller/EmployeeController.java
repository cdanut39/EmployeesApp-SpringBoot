package com.learning.spring.rest.employees.controller;

import com.learning.spring.rest.employees.dto.BaseCommunityDTO;
import com.learning.spring.rest.employees.dto.EmployeeDTO;
import com.learning.spring.rest.employees.dto.UserDTO;
import com.learning.spring.rest.employees.exceptions.NoResultsException;
import com.learning.spring.rest.employees.exceptions.community.CommunityNotFoundByNameException;
import com.learning.spring.rest.employees.exceptions.community.CommunityNotValidException;
import com.learning.spring.rest.employees.exceptions.employee.EmployeeNotFoundException;
import com.learning.spring.rest.employees.exceptions.employee.EmployeeNotValidException;
import com.learning.spring.rest.employees.exceptions.user.UserAlreadyExistsException;
import com.learning.spring.rest.employees.exceptions_handler.ValidationError;
import com.learning.spring.rest.employees.services.EmployeeServiceImpl;
import com.learning.spring.rest.employees.utils.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.learning.spring.rest.employees.utils.BindingResultErrors.getErrors;
import static com.learning.spring.rest.employees.utils.Constants.*;

@RestController
public class EmployeeController {

    private static final Logger logger = LogManager.getLogger(EmployeeController.class);

    private EmployeeServiceImpl employeeServices;

    @Autowired
    public EmployeeController(EmployeeServiceImpl employeeServices, Response response) {
        this.employeeServices = employeeServices;
    }


    /**
     *
     * POST
     *
     */
    @ApiOperation(
            value = "Add a new employee",
            notes = "Can be called only by users with ADMIN or MANAGER roles."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employee added successfully"),
            @ApiResponse(code = 400, message = "Employee data not valid"),
            @ApiResponse(code = 404, message = "Employee not found")
    })
    @PostMapping("/register/employee")
    public ResponseEntity<Response> addEmployee(@Valid @RequestBody EmployeeDTO employee, BindingResult result) throws EmployeeNotValidException, UserAlreadyExistsException, CommunityNotFoundByNameException {
        Response response=new Response();
        if (result.hasErrors()) {
            List<ValidationError> errors = getErrors(result);
            logger.error("Invalid data for adding new employee");
            throw new EmployeeNotValidException("Employee data not valid", errors);
        }
        employeeServices.save(employee);
        response.setMessage(EMPLOYEE_ADDED);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     *
     * GET
     *
     */
    @GetMapping("/employee/{id}")
    public ResponseEntity<UserDTO> getEmployeeById(@PathVariable("id") int id) throws EmployeeNotFoundException {
        EmployeeDTO getEmployee = employeeServices.getEmployeeById(id);
        return new ResponseEntity<>(getEmployee, HttpStatus.OK);
    }

    @GetMapping(value = "/employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return new ResponseEntity<>(employeeServices.getAllEmployees(), HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<EmployeeDTO>> searchEmployees(@RequestParam(value = "lastname") String lastname, @RequestParam(value = "community", required = false) String community) throws NoResultsException {
        List<EmployeeDTO> employeeDTOList = employeeServices.searchEmployeeBy(lastname, community);
        if (employeeDTOList.isEmpty()) {
            throw new NoResultsException(NO_RESULTS);
        }
        return new ResponseEntity<>(employeeDTOList, HttpStatus.OK);
    }

    @GetMapping(value = "/getEmployees")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesWithPagination(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size, @RequestParam(value = "sortBy") String criteria) {
        return new ResponseEntity<>(employeeServices.getEmployeesWithPagination(page, size, criteria), HttpStatus.OK);
    }

    @GetMapping(value = "/employees/orderBy/{criteria}/{direction}")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(@PathVariable("criteria") String criteria, @PathVariable("direction") String direction) {
        return new ResponseEntity<>(employeeServices.getEmployeesSortedByCriteria(criteria, direction), HttpStatus.OK);
    }


    /**
     *
     * PUT
     *
     */
    @PutMapping("/employee/update/{id}")
    public ResponseEntity<Response> updateEmployee(@PathVariable("id") int id, @Valid @RequestBody EmployeeDTO employee, BindingResult result) throws EmployeeNotValidException, EmployeeNotFoundException {
        Response response=new Response();
        if (result.hasErrors()) {
            List<ValidationError> errors = getErrors(result);
            logger.error("Invalid data for adding new employee");
            throw new EmployeeNotValidException("Employee data not valid", errors);
        }
        employeeServices.updateEmployee(id, employee);
        response.setMessage(EMPLOYEE_MODIFIED);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/employee/{empID}/setCommunity")
    public ResponseEntity<Response> assignCommunity(@PathVariable("empID") int empId, @Valid @RequestBody BaseCommunityDTO community, BindingResult result) throws EmployeeNotFoundException, CommunityNotFoundByNameException, CommunityNotValidException {
        Response response=new Response();
        if (result.hasErrors()) {
            List<ValidationError> errors = getErrors(result);
            logger.error("Invalid data for adding new community");
            throw new CommunityNotValidException("community data not valid", errors);
        }
        employeeServices.assignCommunity(empId, community);
        response.setMessage(COMMUNITY_ASSIGNED);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     *
     * DELETE
     *
     */
    @DeleteMapping("/employee/{id}")
    public ResponseEntity<Response> deleteEmployee(@PathVariable("id") int id) throws EmployeeNotFoundException {
        Response response=new Response();
        employeeServices.removeEmployee(id);
        response.setMessage(EMPLOYEE_REMOVED);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




}
