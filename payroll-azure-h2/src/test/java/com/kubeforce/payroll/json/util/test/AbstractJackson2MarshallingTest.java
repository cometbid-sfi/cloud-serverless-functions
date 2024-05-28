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
package com.kubeforce.payroll.json.util.test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.StringWriter;
import java.io.Writer;
import org.junit.jupiter.api.BeforeAll;

/**
 *
 * @author samueladebowale
 */
public abstract class AbstractJackson2MarshallingTest {

    protected static ObjectMapper mapper;
    protected static DeserializationContext deserializationContext;
    protected static SerializerProvider serializerProvider;

    @BeforeAll
    public static void setUp() {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        serializerProvider = mapper.getSerializerProvider();
        deserializationContext = mapper.getDeserializationContext();
    }

    protected String write(Object object) throws Exception {
        Writer writer = new StringWriter();
        mapper.writeValue(writer, object);
        return writer.toString();
    }

    protected <T> T read(String source, Class<T> targetType) throws Exception {
        return mapper.readValue(source, targetType);
    }

    protected String getPackagePath() {
        return "/" + this.getClass().getPackage().getName().replace('.', '/');
    }
}
