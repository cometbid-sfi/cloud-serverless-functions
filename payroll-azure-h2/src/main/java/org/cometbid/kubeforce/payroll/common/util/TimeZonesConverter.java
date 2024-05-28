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
package org.cometbid.kubeforce.payroll.common.util;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author samueladebowale
 */
public class TimeZonesConverter {

    private static final TimeZonesConverter ONE_INSTANCE = new TimeZonesConverter();
    public static final ZoneId DEFAULT_ZONEID = ZoneId.of("UTC");
    private static final ZoneOffset DEFAULT_ZONE_OFFSET = DEFAULT_ZONEID.getRules().getOffset(LocalDateTime.now());

    private final static Set<String> ALL_ZONE_IDS;

    static {
        ALL_ZONE_IDS = ZoneId.getAvailableZoneIds();
    }

    private TimeZonesConverter() {
    }

    public static TimeZonesConverter getInstance() {
        return ONE_INSTANCE;
    }

    public static ZonedDateTime convert(ZonedDateTime fromTimeZone, ZoneId toNewTimeZone) {
        return ZonedDateTime.ofInstant(
                fromTimeZone.toInstant(),
                toNewTimeZone);
    }

    public static OffsetDateTime convertOffset(OffsetDateTime fromTimeZone, ZoneId toNewTimeZone) {
        return OffsetDateTime.ofInstant(
                fromTimeZone.toInstant(),
                toNewTimeZone);
    }

    public static ZonedDateTime getZonedDateTimeInUTC() {
        return Instant.now().atZone(DEFAULT_ZONEID);
    }

    public static OffsetDateTime getOffsetDateTimeInUTC() {
        return OffsetDateTime.now(DEFAULT_ZONE_OFFSET);
    }

    public static OffsetTime getOffsetTimeInUTC() {
        return OffsetTime.now(DEFAULT_ZONE_OFFSET);
    }

    public static Set<String> getAvailableTimeZoneIds() {
        return ZoneId.getAvailableZoneIds();
    }

    public static Map<ZoneOffset, List<ZoneId>> getTimeZonesGroupByOffset() {
        return ALL_ZONE_IDS.stream()
                .map(ZoneId::of)
                .collect(Collectors.groupingBy(x -> x.getRules().getOffset(Instant.now())));
    }

    public static ZoneId getZoneId(String timeZoneId) {
        boolean foundZone = ALL_ZONE_IDS.stream().anyMatch(e -> e.equalsIgnoreCase(timeZoneId));

        if (foundZone) {
            return ZoneId.of(timeZoneId);
        }

        return ZoneId.systemDefault();
    }

    public static ZoneOffset getZoneOffset(String timeZoneId) {
        ZoneId zoneId = getZoneId(timeZoneId);
        return zoneId.getRules().getOffset(Instant.now());
    }

    public static void getTimeZonesGroupByOffsetWithFormatting() {

        getTimeZonesGroupByOffset().entrySet().stream().sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    System.out.println(entry.getKey());
                    for (var zone : entry.getValue()) {
                        System.out.print("    ");
                        System.out.println(zone.getId());
                    }
                });
    }

    public static void main(String... args) {
        //ZonedDateTime utcTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("-07"));
        ZonedDateTime utcTime = getZonedDateTimeInUTC();
        ZonedDateTime localTime = convert(utcTime, Clock.systemDefaultZone().getZone());
        System.out.println(utcTime + " = " + localTime);

        getTimeZonesGroupByOffsetWithFormatting();

        // System.out.println("Available timezones: \n" + getTimeZonesGroupByOffsetWithFormatting());
    }

}
