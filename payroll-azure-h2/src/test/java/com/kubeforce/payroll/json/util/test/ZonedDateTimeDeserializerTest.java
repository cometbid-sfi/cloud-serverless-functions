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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import lombok.NoArgsConstructor;
import static org.cometbid.kubeforce.payroll.common.util.LocalizationContextUtils.getContextZoneId;

import org.cometbid.kubeforce.payroll.common.util.TimeZonesConverter;
import static org.cometbid.kubeforce.payroll.common.util.TimeZonesConverter.DEFAULT_ZONEID;
import org.cometbid.kubeforce.payroll.jackson.util.ZonedDateTimeDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author samueladebowale
 */
public class ZonedDateTimeDeserializerTest extends AbstractJackson2MarshallingTest {

    private ZonedDateTimeDeserializer deserializer;

    @BeforeEach
    public void init() {
        deserializer = new ZonedDateTimeDeserializer();
    }

    @Test
    public void test() throws IOException {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
        mapper.registerModule(simpleModule);

        String expectedJson = "{\"parseDate\":\"2018-12-04T18:47:38.927Z\"}";

        MyPojo resultPojo = new ZonedDateTimeDeserializerTest.MyPojo();
        ZonedDateTime zdtWithZoneOffset = ZonedDateTime.parse("2018-12-04T18:47:38.927Z", DateTimeFormatter.ISO_ZONED_DATE_TIME);

        ZonedDateTime intermediate = ZonedDateTime.of(zdtWithZoneOffset.toLocalDateTime(), getContextZoneId());
        ZonedDateTime zdtInLocalTimeline = TimeZonesConverter.convert(intermediate, DEFAULT_ZONEID);
        System.out.println("local Datetime zone: " + zdtInLocalTimeline);

        resultPojo.setZonedDateTime(zdtInLocalTimeline);
        //ZonedDateTime utcTime = TimeZonesConverter.convert(resultPojo.getZonedDateTime(), DEFAULT_ZONEID);
        //resultPojo.setZonedDateTime(utcTime);

        MyPojo expectedPojo = mapper.readValue(expectedJson, MyPojo.class);
        System.out.println("final Datetime zone: " + expectedPojo.getZonedDateTime());

        int result = zdtInLocalTimeline.compareTo(expectedPojo.getZonedDateTime());
        Assertions.assertEquals(0, result);

    }

    @Data
    @NoArgsConstructor
    static class MyPojo {

        @JsonProperty("parseDate")
        @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
        private ZonedDateTime zonedDateTime;
    }
}
