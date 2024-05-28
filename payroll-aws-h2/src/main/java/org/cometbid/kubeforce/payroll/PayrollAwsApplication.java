/*
 * The MIT License
 *
 * Copyright 2024 samueladebowale.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.cometbid.kubeforce.payroll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 *
 * @author samueladebowale
 */
@EnableWebMvc
@ComponentScan(basePackages = "org.cometbid.kubeforce.payroll")
@SpringBootApplication
public class PayrollAwsApplication {

    public static void main(String[] args) {
        // Can be used to run the function application locally
        SpringApplication.run(PayrollAwsApplication.class, args);

        // Uncomment below when deploying to a serverless platforms such as lambda, knative, etc.
        // FunctionalSpringApplication.run(PayrollAwsApplication.class, args);
    }

    /*
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
     */
}
