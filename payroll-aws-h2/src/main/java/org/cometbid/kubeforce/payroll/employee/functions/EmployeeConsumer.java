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
package org.cometbid.kubeforce.payroll.employee.functions;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.util.Map;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.cometbid.component.api.validators.GenericProgrammaticValidator;
import org.cometbid.kubeforce.payroll.employee.model.CreateEmployeeRequest;
import org.cometbid.kubeforce.payroll.employee.model.EmployeeService;
import org.springframework.stereotype.Component;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@Component("create-emp")
@RequiredArgsConstructor
public class EmployeeConsumer implements Consumer<Map<String, String>> {

    private final Gson gson;
    private final EmployeeService employeeService;

    @Override
    public void accept(Map<String, String> map) {
        log.info("Creating the employee {}", map);
        
        CreateEmployeeRequest requestDto = extractBodyValuesForEmployee(map);
        
        employeeService.saveEmployee(requestDto);  
    }
    
    private CreateEmployeeRequest extractBodyValuesForEmployee(Map<String, String> map) {

        JsonElement jsonElement = gson.toJsonTree(map);
        CreateEmployeeRequest employeeDto = gson.fromJson(jsonElement, CreateEmployeeRequest.class);

        GenericProgrammaticValidator.validate(employeeDto);

        return employeeDto;
    }

}
