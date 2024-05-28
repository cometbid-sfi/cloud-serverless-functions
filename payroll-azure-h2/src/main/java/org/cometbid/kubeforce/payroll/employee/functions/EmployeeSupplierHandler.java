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

/**
 *
 * @author samueladebowale
 */
import com.google.gson.Gson;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.cometbid.kubeforce.payroll.response.model.ApiError;
import org.cometbid.kubeforce.payroll.response.model.ErrorCode;
import org.cometbid.kubeforce.payroll.exceptions.CustomConstraintViolationException;
import org.cometbid.kubeforce.payroll.common.util.PagingFactory;
import org.cometbid.kubeforce.payroll.common.util.SimplePage;
import org.cometbid.kubeforce.payroll.employee.Employee;
import org.cometbid.kubeforce.payroll.employee.EmployeeFinderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@Component
public class EmployeeSupplierHandler {

    //@Autowired
    private final EmployeeFinderService employeeService;
    private final Gson gson;

    public EmployeeSupplierHandler(EmployeeFinderService employeeService, Gson gson) {
        this.employeeService = employeeService;
        this.gson = gson;
    }

    /**
     * GET /find-all-emp?sort_by=email.asc|firstName.desc|lastName AND GET
     * /find-all-emp?sort_by=email,desc|firstName,desc|lastName
     *
     * @param request
     * @param context
     * @return
     */
    @FunctionName("find-all-emp")
    public HttpResponseMessage execute(
            @HttpTrigger(name = "request", methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Map<String, String>> request,
            ExecutionContext context) {

        context.getLogger().info("Executing Employee Supplier...");

        try {
            Pageable pageable = extractQueryParameters(request, context);

            SimplePage<Employee> pagedEmployees = employeeService.findAll(pageable);
            
            String json = gson.toJson(pagedEmployees);

            log.info("Total elements {}", pagedEmployees.getTotalElements());

            if (pagedEmployees.getTotalElements() == 0) {
                log.info("Status code should be 404");

                return request
                        .createResponseBuilder(HttpStatus.NOT_FOUND)
                        .body(json)
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .build();
            }

            return request
                    .createResponseBuilder(HttpStatus.OK)
                    .body(json)
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

    private Pageable extractQueryParameters(HttpRequestMessage<Map<String, String>> request, ExecutionContext context) {
        Map<String, String> queryParams = request.getQueryParameters();

        context.getLogger().info("""
                                 Query parameters passed %s""".formatted(queryParams));

        return PagingFactory.preparePageRequest(queryParams);
    }

    public static void main(String args[]) {
        var sortParams = "email.asc|firstName.desc|lastName";

        var arrays = sortParams.split("[\\s|@&?$+-]+");

        System.out.println("Arrays " + Arrays.asList(arrays));

        //=============================================================
        sortParams = "[email.asc][firstName.desc][lastName]";

        arrays = sortParams.split("[\\s\\[\\]|@&?$+-]+");

        System.out.println("Arrays " + Arrays.asList(arrays));

        //=============================================================
        sortParams = "(email.asc)[firstName.desc][lastName]";

        arrays = sortParams.split("[\\s\\[\\]()|@&?$+-]+");

        System.out.println("Arrays " + Arrays.asList(arrays));

        //=============================================================
        sortParams = "(email.asc){firstName.desc}[lastName]";

        arrays = sortParams.split("[\\s\\[\\]\\{\\}()|@&?$+-]+");

        System.out.println("Arrays " + Arrays.asList(arrays));

        Set<String> deduplicatedFields = new HashSet<>(Arrays.asList(arrays));
        //deduplicatedFields.remove(null);
        //deduplicatedFields.remove("");

        // Removing duplicates...
        System.out.println(deduplicatedFields);

        final Map<String, String> sortFields = new HashMap<>();
        deduplicatedFields.forEach(s -> {
            String[] arrayField = s.split("[\\.,]");
            List<String> list = Arrays.asList(arrayField);

            if (!list.isEmpty()) {
                String key = list.get(0);
                String value = null;
                if (list.size() > 1) {
                    value = list.get(1);
                }

                if (StringUtils.isNotBlank(key)) {
                    sortFields.put(key, value);
                }
            }
        });

        // Removing duplicates...
        System.out.println(sortFields);
    }
}
