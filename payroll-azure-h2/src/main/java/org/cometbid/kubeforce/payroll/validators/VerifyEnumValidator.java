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
package org.cometbid.kubeforce.payroll.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Implementation for the user-defined constraint annotation @VerifyValue This
 * is a general purpose validator which verifies the value for any enum If an
 * Enum object has a getValue() method it will validate based on the value of
 * the Enum else will use the EnumConstant
 *
 * @author Gbenga Adebowale
 */
@Log4j2
@Setter
public class VerifyEnumValidator implements ConstraintValidator<VerifyEnumValue, Object> {

    Class<? extends Enum<?>> enumClass;

    /**
     *
     * @param enumObject
     */
    @Override
    public void initialize(final VerifyEnumValue enumObject) {
        enumClass = enumObject.value();
    }

    /**
     * Checks if the value specified is valid
     *
     * @param myval The value for the object
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(final Object myval, final ConstraintValidatorContext constraintValidatorContext) {

        log.debug("***** Verifying enum String value *****");

        if ((myval != null) && (enumClass != null)) {
            Enum[] enumValues = enumClass.getEnumConstants();
            Object enumValue = null;

            for (Enum enumerable : enumValues) {
                if (myval.toString().equalsIgnoreCase(enumerable.toString())) {
                    return true;
                }
                enumValue = getEnumValue(enumerable);
                if ((enumValue != null) && (myval.toString().equalsIgnoreCase(enumValue.toString()))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Invokes the getValue() method for enum if present
     *
     * @param enumerable The Enum object
     * @return returns the value of enum from getValue() or enum constant
     */
    private Object getEnumValue(Enum<?> enumerable) {
        try {
            for (Method method : enumerable.getClass().getDeclaredMethods()) {
                if (method.getName().equals("getValue")) {
                    return method.invoke(enumerable);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("Enum conversion failed ", e); 
        }
        return null;
    }

}
