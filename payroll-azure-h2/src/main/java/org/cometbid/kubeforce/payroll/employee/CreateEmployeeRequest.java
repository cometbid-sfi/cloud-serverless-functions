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
package org.cometbid.kubeforce.payroll.employee;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import javax.money.MonetaryAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import static org.cometbid.kubeforce.payroll.employee.Employee.EMAIL;
import static org.cometbid.kubeforce.payroll.employee.Employee.EMPLOYEE_TYPE;
import static org.cometbid.kubeforce.payroll.employee.Employee.FIRST_NAME;
import static org.cometbid.kubeforce.payroll.employee.Employee.LAST_NAME;
import static org.cometbid.kubeforce.payroll.employee.Employee.MIDDLE_NAME;
import static org.cometbid.kubeforce.payroll.employee.Employee.SALARY;
import org.cometbid.kubeforce.payroll.validators.ValidEmail;

/**
 *
 * @author samueladebowale
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
public class CreateEmployeeRequest extends EmployeeRequest {

    @SerializedName(EMAIL)
    @JsonProperty(EMAIL)
    @Size(max = 100, message = "{User.email.size}")
    @NotBlank(message = "{User.email.notBlank}")
    @ValidEmail
    private String email;

    @JsonCreator
    @Builder
    public CreateEmployeeRequest(@JsonProperty(EMAIL) String email, 
            @JsonProperty(FIRST_NAME) String firstName, 
            @JsonProperty(MIDDLE_NAME) String middleName, 
            @JsonProperty(LAST_NAME) String lastName, 
            @JsonProperty(EMPLOYEE_TYPE) String employeeType, 
            @JsonProperty(SALARY) MonetaryAmount salary) {
        super(firstName, middleName, lastName, employeeType, salary);
        this.email = email;
    }

}
