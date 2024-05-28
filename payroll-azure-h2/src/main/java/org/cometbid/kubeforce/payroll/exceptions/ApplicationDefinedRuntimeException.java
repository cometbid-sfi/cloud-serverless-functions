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

import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.exception.DefaultExceptionContext;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.apache.commons.lang3.exception.ExceptionContext;
import org.cometbid.kubeforce.payroll.response.model.ErrorCode;
import org.cometbid.kubeforce.payroll.common.util.ResourceBundleAccessor;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author samueladebowale
 */
public class ApplicationDefinedRuntimeException extends ResponseStatusException implements ExceptionContext {

    /**
     *
     */
    private static final long serialVersionUID = 8532405745958624402L;

    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    /**
     * The context where the data is stored.
     */
    private final ExceptionContext exceptionContext;

    /**
     * Instantiates ApplicationDefinedRuntimeException without message or cause.
     * <p>
     * The context information is stored using a default implementation.
     *
     * Creates a new instance of <code>ApplicationDefinedExceptions</code>
     * without detail message.
     */
    public ApplicationDefinedRuntimeException() {
        super(STATUS, "Unexpected runtime error occured");
        exceptionContext = new DefaultExceptionContext();
    }

    /**
     * Instantiates ApplicationDefinedRuntimeException without message or cause.
     * <p>
     * The context information is stored using a default implementation.
     *
     * Creates a new instance of <code>ApplicationDefinedExceptions</code>
     * without detail message.
     * @param message
     * @param cause
     */
    public ApplicationDefinedRuntimeException(final String message, final Throwable cause) {
        super(STATUS, message, cause);
        exceptionContext = new DefaultExceptionContext();
    }

    /**
     * Instantiates ApplicationDefinedRuntimeException with message, but without
     * cause.
     * <p>
     * The context information is stored using a default implementation.
     *
     * @param status
     * @param message the exception message, may be null
     */
    public ApplicationDefinedRuntimeException(final HttpStatus status, final String message) {
        super(status, message);
        exceptionContext = new DefaultExceptionContext();
    }

    /**
     * Instantiates ApplicationDefinedRuntimeException with cause, but without
     * message.
     * <p>
     * The context information is stored using a default implementation.
     *
     * @param status
     * @param cause the underlying cause of the exception, may be null
     */
    public ApplicationDefinedRuntimeException(final HttpStatus status, final Throwable cause) {
        super(status, cause.getMessage(), cause);
        exceptionContext = new DefaultExceptionContext();
    }

    /**
     * Instantiates ApplicationDefinedRuntimeException with cause and message.
     * <p>
     * The context information is stored using a default implementation.
     *
     * @param status
     * @param message the exception message, may be null
     * @param cause the underlying cause of the exception, may be null
     */
    public ApplicationDefinedRuntimeException(final HttpStatus status, final String message, final Throwable cause) {
        super(status, message, cause);
        exceptionContext = new DefaultExceptionContext();
    }

    /**
     * Instantiates ApplicationDefinedRuntimeException with cause, message, and
     * ExceptionContext.
     *
     * @param status
     * @param message the exception message, may be null
     * @param cause the underlying cause of the exception, may be null
     * @param context the context used to store the additional information, null
     * uses default implementation
     */
    public ApplicationDefinedRuntimeException(final HttpStatus status, final String message, final Throwable cause,
            ExceptionContext context) {
        super(status, message, cause);
        if (context == null) {
            context = new DefaultExceptionContext();
        }
        exceptionContext = context;
    }

    // -----------------------------------------------------------------------
    /**
     * Adds information helpful to a developer in diagnosing and correcting the
     * problem. For the information to be meaningful, the value passed should
     * have a reasonable toString() implementation. Different values can be
     * added with the same label multiple times.
     * <p>
     * Note: This exception is only serializable if the object added is
     * serializable.
     * </p>
     *
     * @param label a textual label associated with information, {@code null}
     * not recommended
     * @param value information needed to understand exception, may be
     * {@code null}
     * @return {@code this}, for method chaining, not {@code null}
     */
    @Override
    public ApplicationDefinedRuntimeException addContextValue(final String label, final Object value) {
        exceptionContext.addContextValue(label, value);
        return this;
    }

    /**
     * Sets information helpful to a developer in diagnosing and correcting the
     * problem. For the information to be meaningful, the value passed should
     * have a reasonable toString() implementation. Any existing values with the
     * same labels are removed before the new one is added.
     * <p>
     * Note: This exception is only serializable if the object added as value is
     * serializable.
     * </p>
     *
     * @param label a textual label associated with information, {@code null}
     * not recommended
     * @param value information needed to understand exception, may be
     * {@code null}
     * @return {@code this}, for method chaining, not {@code null}
     */
    @Override
    public ApplicationDefinedRuntimeException setContextValue(final String label, final Object value) {
        exceptionContext.setContextValue(label, value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Object> getContextValues(final String label) {
        return this.exceptionContext.getContextValues(label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getFirstContextValue(final String label) {
        return this.exceptionContext.getFirstContextValue(label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Pair<String, Object>> getContextEntries() {
        return this.exceptionContext.getContextEntries();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getContextLabels() {
        return exceptionContext.getContextLabels();
    }

    /**
     * Provides the message explaining the exception, including the contextual
     * data.
     *
     * @see java.lang.Throwable#getMessage()
     * @return the message, never null
     */
    @Override
    public String getMessage() {
        return getFormattedExceptionMessage(super.getMessage());
    }

    /**
     * Provides the message explaining the exception without the contextual
     * data.
     *
     * @see java.lang.Throwable#getMessage()
     * @return the message
     * @since 3.0.1
     */
    public String getRawMessage() {
        return super.getMessage();
    }

    /**
     *
     * @return
     */
    public String getErrorCode() {
        return ErrorCode.APP_DEFINED_ERR_CODE.getErrCode();
    }

    /**
     *
     * @return
     */
    public String getErrorMessage() {
        String msgKey = ErrorCode.APP_DEFINED_ERR_CODE.getErrMsgKey();
        return ResourceBundleAccessor.accessMessageInBundle(msgKey, new Object[]{});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormattedExceptionMessage(final String baseMessage) {
        return exceptionContext.getFormattedExceptionMessage(baseMessage);
    }
}
