/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.kubeforce.payroll.exceptions;

import org.cometbid.kubeforce.payroll.response.model.ErrorCode;
import org.cometbid.kubeforce.payroll.common.util.ResourceBundleAccessor;
import org.springframework.http.HttpStatus;

/**
 *
 * @author samueladebowale
 */
public class EmployeeAlreadyExistException extends ApplicationDefinedRuntimeException {

    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    /**
     *
     */
    public EmployeeAlreadyExistException() {
        this(new Object[]{});
    }

    /**
     *
     * @param args
     */
    public EmployeeAlreadyExistException(Object[] args) {
        this("employee.alreadyExist", args);
    }

    /**
     *
     * @param messagekey
     * @param args
     */
    public EmployeeAlreadyExistException(String messagekey, Object[] args) {
        this(messagekey, args, null);
    }

    /**
     *
     * @param messagekey
     * @param args
     * @param ex
     */
    public EmployeeAlreadyExistException(String messagekey, Object[] args, Throwable ex) {
        super(STATUS, ResourceBundleAccessor.accessMessageInBundle(messagekey, args), ex);
    }

    /**
     *
     */
    @Override
    public String getErrorCode() {
        return ErrorCode.EMP_EXIST_ERR_CODE.getErrCode();
    }

    /**
     *
     */
    @Override
    public String getErrorMessage() {
        String msgKey = ErrorCode.EMP_EXIST_ERR_CODE.getErrMsgKey();
        return ResourceBundleAccessor.accessMessageInBundle(msgKey, new Object[]{});
    }

}
