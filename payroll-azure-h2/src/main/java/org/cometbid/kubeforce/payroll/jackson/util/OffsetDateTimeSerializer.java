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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import org.cometbid.kubeforce.payroll.common.util.LocalizationContextUtils;
import static org.cometbid.kubeforce.payroll.common.util.TimeZonesConverter.*;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public class OffsetDateTimeSerializer extends StdSerializer<OffsetDateTime> {

     //private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(GENERIC_DATE_FORMAT);
    
     private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss a 'Z'");
     private static final DateTimeFormatter ODT_FORMATTER = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd hh:mm:ssa ")
                //.appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                .appendOffsetId()
                .toFormatter(Locale.ROOT);

    public OffsetDateTimeSerializer() {
        super(OffsetDateTime.class);
    }

    @Override
    public void serialize(OffsetDateTime utcValue, JsonGenerator gen,
            SerializerProvider provider) throws IOException {

        log.info("OffsetDateTime from: {}", utcValue);

        if (!Objects.isNull(utcValue)) {
            ZoneId zoneId = LocalizationContextUtils.getContextZoneId();
            log.info("Zone id: {}", zoneId);

            OffsetDateTime convertedValue = convertOffset(utcValue, zoneId);
            log.info("OffsetDateTime to: {}", convertedValue);

            String dateTimeOffset = ODT_FORMATTER.format(convertedValue);
            log.info("OffsetDateTime formatted: {}", dateTimeOffset);

            gen.writeString(dateTimeOffset);
        }
    }

}
