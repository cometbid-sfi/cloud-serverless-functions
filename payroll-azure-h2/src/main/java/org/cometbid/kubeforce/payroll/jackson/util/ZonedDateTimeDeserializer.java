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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import lombok.extern.log4j.Log4j2;
import static org.cometbid.kubeforce.payroll.common.util.DateUtils.GENERIC_DATE_FORMAT;
import org.cometbid.kubeforce.payroll.common.util.LocalizationContextUtils;
import static org.cometbid.kubeforce.payroll.common.util.TimeZonesConverter.*;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public class ZonedDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {

    //private static final String localDateTimeFormat = "yyyy-MMM-dd hh:mm:ss";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(GENERIC_DATE_FORMAT);

    /*
    private static DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("yyyy-MMM-dd hh:mm:ss");
     */
    public ZonedDateTimeDeserializer() {
        this(null);
    }

    public ZonedDateTimeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ZonedDateTime deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext)
            throws IOException {

        String dateSpecified = jsonParser.getText();
        log.info("Specified Date: " + dateSpecified);

        // Get specified timezone offset information 
        TemporalAccessor dateTime = parse(dateSpecified);
        log.info("Parsed Date: " + dateTime);

        ZoneId zonedId = LocalizationContextUtils.getContextZoneId();
        log.info("Zone id: " + zonedId);

        LocalDateTime localDateTime = LocalDateTime.from(dateTime);
        log.info("Local Date time: " + localDateTime);

        ZonedDateTime convertedValue = ZonedDateTime.of(localDateTime, zonedId);
        log.info("Converted Zoned Date time: " + convertedValue);

        return convert(convertedValue, DEFAULT_ZONEID);
    }

    private TemporalAccessor parse(String v) {
        return formatter.parseBest(v,
                OffsetDateTime::from,
                ZonedDateTime::from,
                LocalDateTime::from,
                LocalDate::from);
    }
}
