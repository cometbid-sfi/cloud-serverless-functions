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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import static com.kubeforce.payroll.json.util.test.AbstractJackson2MarshallingTest.serializerProvider;
import java.io.IOException;
import java.io.StringWriter;
import java.time.OffsetDateTime;
import org.cometbid.kubeforce.payroll.jackson.util.OffsetDateTimeSerializer;
import org.junit.jupiter.api.Test;

/**
 *
 * @author samueladebowale
 */
public class OffsetDateTimeSerializerTest extends AbstractJackson2MarshallingTest {

    @Test
    public void test() throws IOException {
        OffsetDateTimeSerializer serializer = new OffsetDateTimeSerializer();

        OffsetDateTime utcValue = OffsetDateTime.now();
        StringWriter stringJson = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(stringJson);

        serializer.serialize(utcValue, generator, serializerProvider);
    }

}
