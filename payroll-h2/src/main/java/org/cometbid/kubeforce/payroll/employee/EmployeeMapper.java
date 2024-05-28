/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.kubeforce.payroll.employee;

import java.util.Map;

/**
 *
 * @author samueladebowale
 */
class EmployeeMapper {
    
    static Employee createEmp(Map<String, String> map) {
        String empName = map.getOrDefault("name", "");
        String empId = map.getOrDefault("employeeId", "-1");
        String email = map.get("email");
        String salary = map.get("salary");
        
        Employee employee = Employee.builder()
                                .name(empName)
                                .employeeIdentifier(Integer.parseInt(empId))
                                .email(email)
                                .salary(salary)
                .build();
        
        //Employee employee = new Employee(map.get("name"), Integer.parseInt(map.get(
          //      "employeeIdentifier")), map.get("email"), map.get("salary")); 
        
        return employee;
    }
}
