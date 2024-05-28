package org.cometbid.kubeforce.payroll.employee;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Function;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 
 * @author samueladebowale
 */
@Log4j2
@RequiredArgsConstructor
public class EmployeeFunction implements Function<Long, Employee> {

    //@Autowired
    private final EmployeeRepository employeeRepository;

    @Override
    public Employee apply(Long s) {
        Optional<Employee> employeeOptional = employeeRepository.findById(s);
        
        if (employeeOptional.isPresent()) {
            return employeeOptional.get();
        }
        return null;
    }
}
