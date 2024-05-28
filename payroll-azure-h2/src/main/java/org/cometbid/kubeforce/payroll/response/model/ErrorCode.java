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
package org.cometbid.kubeforce.payroll.response.model;

/**
 *
 * @author samueladebowale
 */
public enum ErrorCode {

    SYS_DEFINED_ERR_CODE("SYSTEM-ERR", "sys.def.error"),
    APP_DEFINED_ERR_CODE("APP-DEF-001", "app.def.error"),
    AUTHENTICATION_ERR_CODE("AUTH-ERR-001", "auth.error"),
    BAD_REQUEST_ERR_CODE("BAD-REQ-001", "bad.req.error"),
    CONSTRAINT_VIOLATION_ERR_CODE("INV-DATA-001", "data.val.error"),
    GENERIC_NOT_FOUND_ERR_CODE("GEN-NF-001", "not.found.error"),
    EMP_EXIST_ERR_CODE("EMP-EXIST-001", "emp.exist.err"),
    EMP_NOT_FOUND_ERR_CODE("EMP-NF-001", "emp.not.found"),
    REQUEST_CONNECT_TIMEOUT_ERR_CODE("TIMEOUT-001", "connect.timeout.err"),
    UNAVAILABLE_SERVICE_ERR_CODE("UN-SERV-001", "unavailable.service"),
    HTTP_MEDIATYPE_NOT_SUPPORTED("HTTP-ERR-0002", "media.type.unsupported"),
    HTTP_MESSAGE_NOT_WRITABLE("HTTP-ERR-0003", "request.unwritable"),
    HTTP_MEDIA_TYPE_NOT_ACCEPTABLE("HTTP-ERR-0004", "media.type.unacceptable"),
    JSON_PARSE_ERROR("HTTP-ERR-0005", "json.parser.err"),
    HTTP_MESSAGE_NOT_READABLE("HTTP-ERR-0006", "request.unreadable");

    private final String errCode;
    private final String errMsgKey;

    ErrorCode(final String errCode, final String errMsgKey) {
        this.errCode = errCode;
        this.errMsgKey = errMsgKey;
    }

    /**
     * Provides an app-specific error code to help find out exactly what
     * happened. It's a human-friendly identifier for a given exception.
     *
     * @return a short text code identifying the error
     */
    public String getErrCode() {
        return errCode;
    }

    /**
     * @return the errMsgKey
     */
    public String getErrMsgKey() {
        return errMsgKey;
    }

}
