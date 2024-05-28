package org.cometbid.kubeforce.payroll;

import org.cometbid.kubeforce.payroll.employee.EmployeeConsumer;
import org.cometbid.kubeforce.payroll.employee.EmployeeFunction;
import org.cometbid.kubeforce.payroll.employee.EmployeeRepository;
import org.cometbid.kubeforce.payroll.employee.EmployeeSupplier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.FunctionalSpringApplication;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author samueladebowale
 */
@SpringBootApplication
public class PayrollApplication {

    public static void main(String[] args) {
        // Can be used to run the function application locally
        SpringApplication.run(PayrollApplication.class, args);

        // Uncomment below when deploying to a serverless platforms such as lambda, knative, etc.
        // FunctionalSpringApplication.run(PayrollApplication.class, args);
    }

    @Bean
    public EmployeeFunction employeeFunction(final EmployeeRepository employeeRepository) {
        return new EmployeeFunction(employeeRepository);
    }

    @Bean
    public EmployeeConsumer employeeConsumer(final EmployeeRepository employeeRepository) {
        return new EmployeeConsumer(employeeRepository);
    }

    @Bean
    public EmployeeSupplier exampleSupplier(final EmployeeRepository employeeRepository) {
        return new EmployeeSupplier(employeeRepository);
    }

}
