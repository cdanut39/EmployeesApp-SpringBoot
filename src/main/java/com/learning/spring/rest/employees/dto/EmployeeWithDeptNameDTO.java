package com.learning.spring.rest.employees.dto;

public class EmployeeWithDeptNameDTO extends UserDTO {

    private String deptName;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
