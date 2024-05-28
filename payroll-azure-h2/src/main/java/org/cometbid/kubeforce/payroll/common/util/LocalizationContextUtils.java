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

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;

/**
 *
 * @author samueladebowale
 */
@Log4j2
public class LocalizationContextUtils {

    public static final String THREAD_CONTEXT_LOCALE_KEY = "locale";
    public static final String THREAD_CONTEXT_TIMEZONE_KEY = "timezone";
    public static final String DEFAULT_LANG_CODE = "en_US";
    public static final String DEFAULT_TIMEZONE = "America/New_York";

    public static Map<String, String> mappedLocales = new HashMap<>();
    public static final LocalizationContextUtils ONE_INSTANCE = new LocalizationContextUtils();

    static {
        ThreadContext.put(THREAD_CONTEXT_LOCALE_KEY, DEFAULT_LANG_CODE);
    }

    private LocalizationContextUtils() {
        Locale[] locales = SimpleDateFormat.getAvailableLocales();
        for (Locale locale : locales) {
            mappedLocales.put(locale.toString(), locale.getDisplayLanguage());
        }
    }

    public static LocalizationContextUtils getInstance() {
        return ONE_INSTANCE;
    }

    public Map<String, String> getSystemLocales() {
        return mappedLocales;
    }

    public static Locale getContextLocale() {
        String localeStr = ThreadContext.get(THREAD_CONTEXT_LOCALE_KEY);
        
        localeStr = StringUtils.isNotBlank(localeStr) ? localeStr : DEFAULT_LANG_CODE;

        log.info("User context locale {}", localeStr);
        return LocaleUtils.toLocale(localeStr);
    }

    public static ZoneId getContextZoneId() {
        String timezone = ThreadContext.get(THREAD_CONTEXT_TIMEZONE_KEY);
        
        timezone = StringUtils.isNotBlank(timezone) ? timezone : DEFAULT_TIMEZONE;

        log.info("User context timezone {}", timezone);
        return ZoneId.of(timezone);
    }
    
    public static ZoneOffset getContextZoneOffset(ZoneId zoneId, Instant instant) {
        return ZonedDateTime.ofInstant(instant, zoneId).getOffset();
    }

    public static String getContextLocaleAsString() {
        return ThreadContext.get(THREAD_CONTEXT_LOCALE_KEY);
    }

    public static void setContextLocale(String langCode) {
        log.info("User context locale {}", langCode);

        ThreadContext.put(THREAD_CONTEXT_LOCALE_KEY, langCode);
    }

    public static void setContextTimezone(String userTimezone) {
        log.info("User context timezone {}", userTimezone);

        ThreadContext.put(THREAD_CONTEXT_TIMEZONE_KEY, userTimezone);
    }
}
