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
import lombok.extern.log4j.Log4j2;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import java.util.Optional;
import org.cometbid.kubeforce.payroll.employee.Employee;
import org.cometbid.kubeforce.payroll.employee.EmployeeFinderService;
import org.cometbid.kubeforce.payroll.employee.EmployeeRepository;
import org.cometbid.kubeforce.payroll.response.model.ApiError;
import org.cometbid.kubeforce.payroll.response.model.ErrorCode;
import org.springframework.stereotype.Component;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@Component
public class EmployeeFunctionHandler {

    /**
     * Plain Spring bean (not Spring Cloud Functions!)
     */
    // private final Function<String, String> uppercase;
    // private final FunctionCatalog functionCatalog;
    private final EmployeeRepository employeeRepository;
    private final EmployeeFinderService employeeService;
    private final Gson gson;

    /**
     *
     * @param empService
     * @param employeeRepository
     * @param gson
     */
    public EmployeeFunctionHandler(EmployeeFinderService empService, EmployeeRepository employeeRepository, Gson gson) {
        this.employeeService = empService;
        this.employeeRepository = employeeRepository;
        this.gson = gson;
    }

    /**
     *
     * @param request
     * @param context
     * @return
     */
    @FunctionName("find-emp")
    public HttpResponseMessage getByEmployeeId(
            @HttpTrigger(name = "request", methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            ExecutionContext context) {

        context.getLogger().info("Executing Employee Function...");
        /*
        String timezone = request.getHeaders().getOrDefault(THREAD_CONTEXT_TIMEZONE_KEY,
                DEFAULT_TIMEZONE);

        LocalizationContextUtils.setContextTimezone(timezone);
         */
        String empId = request.getQueryParameters().getOrDefault("empId", "-1");

        //context.getLogger().info("""
        //   The parameter passed %s %s""".formatted(empId, timezone));
        Employee employee = employeeService.findByEmpId(empId);

        try {
            return request
                    .createResponseBuilder(HttpStatus.OK)
                    .body(gson.toJson(employee))
                    .header("Content-Type", "application/json")
                    .build();

        } catch (Exception ex) {

            return request
                    .createResponseBuilder(HttpStatus.NOT_FOUND)
                    .body("Employee not Found with id " + empId)
                    .header("Content-Type", "text/plain")
                    .build();
        }
    }

    /**
     *
     * @param request
     * @param context
     * @return
     */
    @FunctionName("count-emp")
    public HttpResponseMessage execute(
            @HttpTrigger(name = "request", methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            ExecutionContext context) {

        String response = "All employee Count: " + employeeRepository.count();

        try {
            return request
                    .createResponseBuilder(HttpStatus.OK)
                    .body(response)
                    .header("Content-Type", "application/json")
                    .build();

        } catch (Exception ex) {

            log.error("Error occured", ex);

            int statusCode = HttpStatus.BAD_REQUEST.value();

            ApiError apiError = ApiError.create(request.getUri().getPath(),
                    request.getHttpMethod().name(), ErrorCode.APP_DEFINED_ERR_CODE.getErrCode(),
                    statusCode, "Error occured processing request", ex.getMessage());

            // Convert to json
            String json = gson.toJson(apiError);

            return request
                    .createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body(json)
                    .header("Content-Type", "application/json")
                    .build();
        }
    }

}
