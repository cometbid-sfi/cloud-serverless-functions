package org.cometbid.kubeforce.payroll.employee;

import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class EmployeeSupplier implements Supplier {

    //public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeSupplier.class);
    Random Generator = new Random();
    //@Autowired
    private final EmployeeRepository EmployeeRepository;

    @Override
    public Employee get() {
        List<Employee> employees = EmployeeRepository.findAll();
        
        int size = employees.size();
        int index = Generator.nextInt(0, size);
        
        log.info("Getting the employee of our choice - {}", employees);
        return employees.get(index);
    }
}
