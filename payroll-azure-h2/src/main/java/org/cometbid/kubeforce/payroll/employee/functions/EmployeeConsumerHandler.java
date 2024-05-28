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
import java.util.logging.Level;
import lombok.extern.log4j.Log4j2;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import java.util.Map;
import org.cometbid.kubeforce.payroll.employee.EmployeeTypeDTO;
import org.cometbid.kubeforce.payroll.employee.EmployeeNameDTO;
import org.cometbid.kubeforce.payroll.response.model.ApiError;
import org.cometbid.kubeforce.payroll.response.model.ErrorCode;
import org.cometbid.kubeforce.payroll.exceptions.CustomConstraintViolationException;
import org.cometbid.kubeforce.payroll.common.util.GenericProgrammaticValidator;
import org.cometbid.kubeforce.payroll.employee.CreateEmployeeRequest;
import org.cometbid.kubeforce.payroll.employee.Employee;
import org.cometbid.kubeforce.payroll.employee.EmployeeService;
import org.cometbid.kubeforce.payroll.employee.UpdEmployeeRequest;
import org.springframework.stereotype.Component;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@Component
public class EmployeeConsumerHandler {

    //private final EmployeeNameConsumer employeeNameConsumer;
    private final EmployeeService employeeService;
    private final Gson gson;

    public EmployeeConsumerHandler(EmployeeService employeeConsumer, Gson gson) {
        this.employeeService = employeeConsumer;
        //this.employeeTypeConsumer = employeeTypeConsumer;
        this.gson = gson;
    }

    /**
     *
     * @param request
     * @param context
     * @return
     */
    @FunctionName("create-emp")
    public HttpResponseMessage createEmployee(
            @HttpTrigger(name = "request", methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Map<String, String>> request,
            ExecutionContext context) {

        context.getLogger().info("Executing create Employee Function...");

        try {
            Employee employee = employeeService.saveEmployee(extractBodyValuesForEmployee(request.getBody()));

            return request
                    .createResponseBuilder(HttpStatus.OK)
                    .body(gson.toJson(employee))
                    .header("Content-Type", "application/json")
                    .build();

        } catch (Exception ex) {
            log.error("Error occured", ex);

            int statusCode = HttpStatus.BAD_REQUEST.value();

            ApiError apiError = ApiError.create(request.getUri().getPath(),
                    request.getHttpMethod().name(), ErrorCode.APP_DEFINED_ERR_CODE.getErrCode(),
                    statusCode, "Error occured processing request", ex.getMessage());

            if (ex instanceof CustomConstraintViolationException err) {

                apiError = ApiError.create(request.getUri().getPath(),
                        request.getHttpMethod().name(), err.getErrorCode(),
                        statusCode, "Error occured processing request", err.getErrorMessage());

                apiError.addValidationErrors(err.getConstraintViolations());
            }

            // Convert to json
            String json = gson.toJson(apiError);

            return request
                    .createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body(json)
                    .header("Content-Type", "application/json")
                    .build();
        }
    }

    private CreateEmployeeRequest extractBodyValuesForEmployee(Map<String, String> map) {

        JsonElement jsonElement = gson.toJsonTree(map);
        CreateEmployeeRequest employeeDto = gson.fromJson(jsonElement, CreateEmployeeRequest.class);

        GenericProgrammaticValidator.validate(employeeDto);

        return employeeDto;
    }

    /**
     *
     * @param request
     * @param context
     * @return
     */
    @FunctionName("upd-emp")
    public HttpResponseMessage updEmployee(
            @HttpTrigger(name = "request", methods = {HttpMethod.PUT},
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Map<String, String>> request,
            ExecutionContext context) {

        //Use SCF composition. 
        context.getLogger().info("Executing employee Salary...");

        try {
            //Use plain Spring Beans.
            UpdEmployeeRequest requestDto = extractBodyValuesForEmployeeUpdate(request.getBody());
            context.getLogger().log(Level.INFO, "Request parameter...{0}", requestDto);

            String employeeId = request.getQueryParameters().getOrDefault("empId", "-1");

            Employee employee = this.employeeService.updateEmployee(requestDto, employeeId);

            return request
                    .createResponseBuilder(HttpStatus.OK)
                    .body(employee)
                    .header("Content-Type", "text/plain")
                    .build();

        } catch (Exception ex) {
            log.error("Error occured", ex);

            int statusCode = HttpStatus.BAD_REQUEST.value();

            ApiError apiError = ApiError.create(request.getUri().getPath(),
                    request.getHttpMethod().name(), ErrorCode.APP_DEFINED_ERR_CODE.getErrCode(),
                    statusCode, "Error occured processing request", ex.getMessage());

            if (ex instanceof CustomConstraintViolationException err) {

                apiError = ApiError.create(request.getUri().getPath(),
                        request.getHttpMethod().name(), err.getErrorCode(),
                        statusCode, "Error occured processing request", err.getErrorMessage());

                apiError.addValidationErrors(err.getConstraintViolations());
            }

            // ConversionFailedException
            return request
                    .createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body(apiError)
                    .header("Content-Type", "application/json")
                    .build();
        }
    }

    private UpdEmployeeRequest extractBodyValuesForEmployeeUpdate(Map<String, String> map) {

        JsonElement jsonElement = gson.toJsonTree(map);
        UpdEmployeeRequest employeeDto = gson.fromJson(jsonElement, UpdEmployeeRequest.class);

        GenericProgrammaticValidator.validate(employeeDto);

        return employeeDto;
    }

    /**
     *
     * @param request
     * @param context
     * @return
     */
    @FunctionName("emp-salary")
    public HttpResponseMessage updEmpSalary(
            @HttpTrigger(name = "request", methods = {HttpMethod.POST, HttpMethod.PUT},
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Map<String, String>> request,
            ExecutionContext context) {

        //Use SCF composition. 
        context.getLogger().info("Executing employee Salary...");

        try {
            //Use plain Spring Beans.
            EmployeeTypeDTO requestDto = extractBodyValuesForEmpType(request.getBody());
            context.getLogger().log(Level.INFO, "Request parameter...{0}", requestDto);

            String employeeId = request.getQueryParameters().getOrDefault("empId", "-1");

            Employee employee = this.employeeService.updateEmployeeType(requestDto, employeeId);

            return request
                    .createResponseBuilder(HttpStatus.OK)
                    .body(employee)
                    .header("Content-Type", "application/json")
                    .build();

        } catch (Exception ex) {
            log.error("Error occured", ex);

            int statusCode = HttpStatus.BAD_REQUEST.value();

            ApiError apiError = ApiError.create(request.getUri().getPath(),
                    request.getHttpMethod().name(), ErrorCode.APP_DEFINED_ERR_CODE.getErrCode(),
                    statusCode, "Error occured processing request", ex.getMessage());

            if (ex instanceof CustomConstraintViolationException err) {

                apiError = ApiError.create(request.getUri().getPath(),
                        request.getHttpMethod().name(), err.getErrorCode(),
                        statusCode, "Error occured processing request", err.getErrorMessage());

                apiError.addValidationErrors(err.getConstraintViolations());
            }

            // ConversionFailedException
            return request
                    .createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body(apiError)
                    .header("Content-Type", "application/json")
                    .build();
        }
    }

    private EmployeeTypeDTO extractBodyValuesForEmpType(Map<String, String> map) {

        JsonElement jsonElement = gson.toJsonTree(map);
        EmployeeTypeDTO employeeDto = gson.fromJson(jsonElement, EmployeeTypeDTO.class);

        GenericProgrammaticValidator.validate(employeeDto);

        return employeeDto;
    }

    /**
     *
     * @param request
     * @param context
     * @return
     */
    @FunctionName("emp-type")
    public HttpResponseMessage updEmpName(
            @HttpTrigger(name = "request", methods = {HttpMethod.POST, HttpMethod.PUT},
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Map<String, String>> request,
            ExecutionContext context) {

        //Use SCF composition. 
        //Function composed = this.functionCatalog.lookup("employeeConsumer");
        context.getLogger().info("Executing employee type...");

        //Use plain Spring Beans.
        //UpdateEmployeeNameDTO requestDto = request.getBody();
        try {

            //Use plain Spring Beans.
            EmployeeNameDTO requestDto = extractBodyValuesForEmpName(request.getBody());
            context.getLogger().log(Level.INFO, "Request parameter...{0}", requestDto);

            String employeeId = request.getQueryParameters().getOrDefault("empId", "-1");

            Employee employee = this.employeeService.updateEmployeeName(requestDto, employeeId);

            return request
                    .createResponseBuilder(HttpStatus.OK)
                    .body(employee)
                    .header("Content-Type", "text/plain")
                    .build();

        } catch (Exception ex) {
            log.error("Error occured", ex);

            int statusCode = HttpStatus.BAD_REQUEST.value();

            ApiError apiError = ApiError.create(request.getUri().getPath(),
                    request.getHttpMethod().name(), ErrorCode.APP_DEFINED_ERR_CODE.getErrCode(),
                    statusCode, "Error occured processing request", ex.getMessage());

            if (ex instanceof CustomConstraintViolationException err) {

                apiError = ApiError.create(request.getUri().getPath(),
                        request.getHttpMethod().name(), err.getErrorCode(),
                        statusCode, "Error occured processing request", err.getErrorMessage());

                apiError.addValidationErrors(err.getConstraintViolations());
            }

            // ConversionFailedException
            return request
                    .createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body(apiError)
                    .header("Content-Type", "application/json")
                    .build();
        }
    }

    private EmployeeNameDTO extractBodyValuesForEmpName(Map<String, String> map) {

        JsonElement jsonElement = gson.toJsonTree(map);
        EmployeeNameDTO employeeDto = gson.fromJson(jsonElement, EmployeeNameDTO.class);

        GenericProgrammaticValidator.validate(employeeDto);

        return employeeDto;
    }

}
