package org.cometbid.kubeforce.payroll.employee;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@RequiredArgsConstructor
public class EmployeeConsumer implements Consumer<Map<String, String>> {

    //public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeConsumer.class);

    //@Autowired
    private final EmployeeRepository employeeRepository;

    @Override
    public void accept(Map<String, String> map) {
        log.info("Creating the employee {}", map);
        
        employeeRepository.save(EmployeeMapper.createEmp(map));
    }

}
