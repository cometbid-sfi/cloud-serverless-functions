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
package org.cometbid.kubeforce.payroll.jackson.util;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.google.gson.annotations.SerializedName;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author samueladebowale
 */
@Log4j2
@NoArgsConstructor
public final class SerializedNameAnnotationIntrospector extends JacksonAnnotationIntrospector {

    @Getter
    private static final AnnotationIntrospector instance = new SerializedNameAnnotationIntrospector();

    @Override
    public PropertyName findNameForDeserialization(final Annotated annotated) {
        //@Nullable
        final SerializedName serializedName = annotated.getAnnotation(SerializedName.class);
        if (serializedName == null) {
            log.info("SerializedName not used...");
            return super.findNameForDeserialization(annotated);
        }

        log.info("Dealing with Enum annotation SerializedName {}", serializedName.value());

        // TODO how to handle serializedName.alternate()?
        return new PropertyName(serializedName.value());
    }

    @Override
    public boolean hasIgnoreMarker(AnnotatedMember m) {
        return m instanceof AnnotatedField
                && m.getAnnotation(SerializedName.class) == null;
    }

}
