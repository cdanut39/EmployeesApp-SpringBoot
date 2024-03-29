package com.learning.spring.rest.employees.services;


import com.learning.spring.rest.employees.dto.ManagerDTO;
import com.learning.spring.rest.employees.exceptions.user.UserAlreadyExistsException;

public interface ManagerService {

    ManagerDTO save(ManagerDTO managerDTO) throws UserAlreadyExistsException;
}
