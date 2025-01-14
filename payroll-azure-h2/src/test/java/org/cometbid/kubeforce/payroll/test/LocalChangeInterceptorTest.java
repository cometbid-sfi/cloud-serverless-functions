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
package org.cometbid.kubeforce.payroll.test;

import org.cometbid.kubeforce.payroll.CustomLocaleChangeInterceptor;
import org.cometbid.kubeforce.payroll.employee.EmployeeBuilder;
import org.cometbid.kubeforce.payroll.employee.EmployeeMapper;
import org.cometbid.kubeforce.payroll.employee.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author samueladebowale
 */
public class LocalChangeInterceptorTest {

    private CustomLocaleChangeInterceptor changeInterceptor;

    @BeforeEach
    void init() {
        this.changeInterceptor = new CustomLocaleChangeInterceptor();
    }
}
