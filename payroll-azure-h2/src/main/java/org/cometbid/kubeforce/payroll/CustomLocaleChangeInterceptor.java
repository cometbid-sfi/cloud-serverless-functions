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

import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import static org.cometbid.kubeforce.payroll.common.util.LocalizationContextUtils.*;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public class CustomLocaleChangeInterceptor extends LocaleChangeInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ServletException {

        String langCode = DEFAULT_LANG_CODE;

        if (request.getHeader("locale") != null) {
            langCode = request.getHeader("locale");
        } else if (request.getParameter("lang") != null) {
            langCode = request.getParameter("lang");
        } else if (request.getParameter("language") != null) {
            langCode = request.getParameter("language");
        } else if (request.getParameter("locale") != null) {
            langCode = request.getParameter("locale");
        }

        log.info("""
                 Locale Change preHandle called...%s"""
                .formatted(langCode));

        setContextLocale(langCode);
        this.setParamName(langCode);

        return super.preHandle(request, response, handler);
    }
}
