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
package org.cometbid.kubeforce.payroll.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Locale;
import org.cometbid.kubeforce.payroll.CustomLocaleChangeInterceptor;
import org.cometbid.kubeforce.payroll.common.util.StringToEnumConverter;
import org.cometbid.kubeforce.payroll.jackson.util.OffsetDateTimeDeserializer;
import org.cometbid.kubeforce.payroll.jackson.util.OffsetDateTimeSerializer;
import org.cometbid.kubeforce.payroll.jackson.util.SerializedNameAnnotationIntrospector;
import org.cometbid.kubeforce.payroll.jackson.util.ZonedDateTimeDeserializer;
import org.cometbid.kubeforce.payroll.jackson.util.ZonedDateTimeSerializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.zalando.jackson.datatype.money.MoneyModule;

/**
 *
 * @author samueladebowale
 */
@Configuration
@ComponentScan(basePackages = "org.cometbid.kubeforce.payroll")
public class MessageConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(new RequestInterceptor());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToEnumConverter());
    }

    /*
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        slr.setLocaleAttributeName("session.current.locale");
        slr.setTimeZoneAttributeName("session.current.timezone");
        return slr;
    }
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver r = new CookieLocaleResolver("localeInfo");
        r.setDefaultLocale(Locale.US);

        //if set to -1, the cookie is deleted
        // when browser shuts down
        r.setCookieMaxAge(Duration.ofSeconds(24 * 60 * 60));
        return r;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        return new CustomLocaleChangeInterceptor();
    }

    @Bean(name = "messageSource")
    public MessageSource bundleMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // messageSource.setBasename("classpath:locale/messages");
        messageSource.setBasenames("classpath:ValidationMessages");
        //messageSource.setBasenames("classpath:messages/business/messages",
        //     "classpath:language/messages");

        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    @Bean
    @Primary
    public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(messageSource);
        return localValidatorFactoryBean;
    }

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new MoneyModule().withQuotedDecimalNumbers());
        mapper.setAnnotationIntrospector(new SerializedNameAnnotationIntrospector());
        
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer());
        simpleModule.addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());
        simpleModule.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
        simpleModule.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());
        
        
        mapper.registerModule(simpleModule);
        return mapper;
    }
    /*
    @Bean
    public Gson gson() {
        GsonBuilder b = new GsonBuilder();
        b.setExclusionStrategies(new AnnotationExclusionStrategy());
        b.registerTypeAdapterFactory(new MoneyTypeAdapterFactory());
        //b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapterFactory(DateTypeAdapter.FACTORY);
        //b.registerTypeAdapterFactory(TimestampTypeAdapter.FACTORY);
        b.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
        b.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        b.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter());
        b.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeAdapter());
        return b.create();
    }
     */
}
