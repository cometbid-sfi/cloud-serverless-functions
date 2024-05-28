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
package org.cometbid.kubeforce.payroll.exceptions;

import lombok.Getter;
import org.cometbid.kubeforce.payroll.common.util.ResourceBundleAccessor;
import org.cometbid.kubeforce.payroll.response.model.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 *
 * @author samueladebowale
 */
/**
 * @author Gbenga
 *
 */
public class InvalidRequestException extends ApplicationDefinedRuntimeException {

    private int statusCode;

    public InvalidRequestException() {
        this(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    public InvalidRequestException(String message) {
        this(HttpStatus.BAD_REQUEST, message);
    }

    public InvalidRequestException(String message, Throwable cause) {
        this(HttpStatus.BAD_REQUEST, message, cause);
    }

    public InvalidRequestException(HttpStatus status, String message) {
        this(status, message, null);
    }

    public InvalidRequestException(HttpStatus status, Throwable cause) {
        this(status, status.getReasonPhrase(), cause);
    }

    public InvalidRequestException(HttpStatus status, String message, Throwable cause) {
        // TODO Auto-generated constructor stub
        super(message, cause);
        this.statusCode = status.value();
    }

    /**
     *
     */
    @Override
    public String getErrorCode() {
        return ErrorCode.BAD_REQUEST_ERR_CODE.getErrCode();
    }

    /**
     *
     */
    @Override
    public String getErrorMessage() {
        String msgKey = ErrorCode.BAD_REQUEST_ERR_CODE.getErrMsgKey();
        return ResourceBundleAccessor.accessMessageInBundle(msgKey, new Object[]{});
    }
}
  