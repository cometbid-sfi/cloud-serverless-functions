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
package org.cometbid.kubeforce.payroll.error.handler;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import com.fasterxml.jackson.core.JsonParseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cometbid.kubeforce.payroll.common.util.ResourceBundleAccessor;
import org.cometbid.kubeforce.payroll.config.YamlPropertySourceFactory;
import org.cometbid.kubeforce.payroll.exceptions.ApplicationDefinedRuntimeException;
import org.cometbid.kubeforce.payroll.response.model.ApiError;
import org.cometbid.kubeforce.payroll.response.model.AppResponse;
import org.cometbid.kubeforce.payroll.response.model.ErrorCode;
import org.cometbid.kubeforce.payroll.exceptions.CustomConstraintViolationException;
import org.cometbid.kubeforce.payroll.exceptions.EmployeeAlreadyExistException;
import org.cometbid.kubeforce.payroll.exceptions.EmployeeNotFoundException;
import org.cometbid.kubeforce.payroll.exceptions.InvalidRequestException;
import org.cometbid.kubeforce.payroll.exceptions.ResourceNotFoundException;
import org.cometbid.kubeforce.payroll.exceptions.ServiceUnavailableException;
import static org.cometbid.kubeforce.payroll.response.model.ErrorCode.BAD_REQUEST_ERR_CODE;
import org.hibernate.exception.ConstraintViolationException;
import static org.springframework.http.HttpStatus.*;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@Configuration
@RestControllerAdvice
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class GlobalExceptionHandler {

    @Value("${api.common.reportError}")
    private String sendReportUri;

    @Value("${api.common.version}")
    private String currentApiVersion;

    @Value("${api.common.help}")
    private String moreInfoUrl;

    private static final String DEFAULT_ERROR_CODE = ErrorCode.SYS_DEFINED_ERR_CODE.getErrCode();

    /**
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {Exception.class})
    public @ResponseBody
    AppResponse genericServerError(Exception ex, HttpServletRequest request, HttpServletResponse response) {

        String message = getErrorMessage(ErrorCode.SYS_DEFINED_ERR_CODE.getErrMsgKey());

        return createHttpErrorInfo(INTERNAL_SERVER_ERROR, DEFAULT_ERROR_CODE, request, message, ex);
    }

    /**
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler(value = {ServiceUnavailableException.class})
    public @ResponseBody
    AppResponse serviceFailure(ServiceUnavailableException ex, HttpServletRequest request, HttpServletResponse response) {

        String message = getErrorMessage(ErrorCode.SYS_DEFINED_ERR_CODE.getErrMsgKey());

        return createHttpErrorInfo(SERVICE_UNAVAILABLE, ex.getErrorCode(), request, message, ex);
    }

    /**
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected @ResponseBody
    AppResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response) {
        log.error("400 Status Code", ex);

        final BindingResult result = ex.getBindingResult();

        AppResponse customError = createHttpErrorInfo(BAD_REQUEST, BAD_REQUEST_ERR_CODE.getErrCode(), request, null, ex);
        ApiError apiError = (ApiError) customError.getApiResponse();

        result.getAllErrors().stream().forEach(e -> {

            if (e instanceof FieldError fieldError) {
                apiError.addValidationError(fieldError);
                // return ((FieldError) e).getField() + " : " + e.getDefaultMessage();
            } else {
                if (e != null) {
                    apiError.addValidationError(new ObjectError(e.getObjectName(), e.getDefaultMessage()));
                    // return e.getObjectName() + " : " + e.getDefaultMessage();
                }
            }
        });

        return customError;
    }

    /**
     *
     * @param ex
     * @param request
     * @return
     */
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public @ResponseBody
    AppResponse handleNotFoundExceptions(ResourceNotFoundException ex, HttpServletRequest request) {

        if (ex instanceof EmployeeNotFoundException enfe) {
            return createHttpErrorInfo(NOT_FOUND, enfe.getErrorCode(), request, null, ex);
        }

        return createHttpErrorInfo(NOT_FOUND, ex != null ? ex.getErrorCode() : DEFAULT_ERROR_CODE, request, null, ex);
    }

    /**
     *
     * @param exception
     * @param request
     * @return
     */
    @ResponseStatus(CONFLICT)
    @ExceptionHandler({EmployeeAlreadyExistException.class})
    public @ResponseBody
    AppResponse handleEmployeeAlreadyExistsException(EmployeeAlreadyExistException exception, HttpServletRequest request) {

        return createHttpErrorInfo(CONFLICT, exception.getErrorCode(), request, null, exception);
    }

    /**
     *
     * @param request
     * @param ex
     * @return
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(InvalidRequestException.class)
    public @ResponseBody
    AppResponse handleInvalidInputException(HttpServletRequest request,
            InvalidRequestException ex) {

        return createHttpErrorInfo(BAD_REQUEST, ex.getErrorCode(), request, null, ex);
    }

    /**
     *
     * @param ex
     * @param request
     * @return
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class, CustomConstraintViolationException.class, ConversionFailedException.class})
    public @ResponseBody
    AppResponse handleValidationExceptions(Exception ex, HttpServletRequest request) {

        AppResponse customError = null;

        if (ex instanceof CustomConstraintViolationException err) {
            customError = createHttpErrorInfo(HttpStatus.BAD_REQUEST, err.getErrorCode(), request, null, ex);

            ApiError apiError = (ApiError) customError.getApiResponse();
            apiError.addValidationErrors(err.getConstraintViolations());
        } else if (ex instanceof ConversionFailedException err) {
            customError = createHttpErrorInfo(HttpStatus.BAD_REQUEST, DEFAULT_ERROR_CODE, request, "Data conversion error", ex);

            ApiError apiError = (ApiError) customError.getApiResponse();
            Object obj = err.getValue();

            apiError.addValidationError(new ObjectError(obj != null ? obj.toString() : "", err.getMessage()));
        } else {
            customError = createHttpErrorInfo(HttpStatus.BAD_REQUEST,
                    ErrorCode.CONSTRAINT_VIOLATION_ERR_CODE.getErrCode(), request, null, ex);
        }

        return customError;
    }

    /**
     *
     * @param ex
     * @param request
     * @return
     */
    @ResponseStatus(NOT_ACCEPTABLE)
    @ExceptionHandler({JsonParseException.class})
    public @ResponseBody
    AppResponse handleJsonParseException(JsonParseException ex, HttpServletRequest request) {

        String message = getErrorMessage(ErrorCode.JSON_PARSE_ERROR.getErrMsgKey());

        log.info("JsonParseException :: request.getMethod(): " + request.getMethod());

        return createHttpErrorInfo(HttpStatus.BAD_REQUEST, ErrorCode.JSON_PARSE_ERROR.getErrCode(), request, message, ex);
    }

    /**
     *
     * @param ex
     * @param request
     * @return
     */
    @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({HttpMessageNotReadableException.class, HttpMediaTypeNotAcceptableException.class,
        HttpMediaTypeNotSupportedException.class, HttpMessageNotWritableException.class,
        HttpMediaTypeNotAcceptableException.class})
    public @ResponseBody
    AppResponse handleHttpMessageException(Exception ex, HttpServletRequest request) {

        String message = "Not available";
        String localErrorCode = DEFAULT_ERROR_CODE;

        if (ex instanceof HttpMessageNotReadableException) {
            message = getErrorMessage(ErrorCode.HTTP_MESSAGE_NOT_READABLE.getErrMsgKey());
            localErrorCode = ErrorCode.HTTP_MESSAGE_NOT_READABLE.getErrCode();
        } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
            message = getErrorMessage(ErrorCode.HTTP_MEDIA_TYPE_NOT_ACCEPTABLE.getErrMsgKey());
            localErrorCode = ErrorCode.HTTP_MEDIA_TYPE_NOT_ACCEPTABLE.getErrCode();
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            message = getErrorMessage(ErrorCode.HTTP_MEDIATYPE_NOT_SUPPORTED.getErrMsgKey());
            localErrorCode = ErrorCode.HTTP_MEDIATYPE_NOT_SUPPORTED.getErrCode();
        } else if (ex instanceof HttpMessageNotWritableException) {
            message = getErrorMessage(ErrorCode.HTTP_MESSAGE_NOT_WRITABLE.getErrMsgKey());
            localErrorCode = ErrorCode.HTTP_MESSAGE_NOT_WRITABLE.getErrCode();
        }

        log.info("Exception :: request.getMethod(): " + request.getMethod());

        return createHttpErrorInfo(HttpStatus.BAD_REQUEST, localErrorCode, request, message, ex);
    }

    private AppResponse createHttpErrorInfo(HttpStatus httpStatus, String errorCode, HttpServletRequest request,
            String message, Exception ex) {

        final String path = request.getRequestURI();

        if (StringUtils.isBlank(message)) {
            message = ex.getMessage();
        }

        log.debug("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);

        return new AppResponse(currentApiVersion, request.getMethod(), errorCode, httpStatus, message, path,
                sendReportUri, moreInfoUrl, ex);
    }

    private String getErrorMessage(String messagekey) {

        return ResourceBundleAccessor.accessMessageInBundle(messagekey, new Object[]{});
    }
}
