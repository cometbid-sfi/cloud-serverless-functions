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
package com.kubeforce.payroll.gson.util.test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.cometbid.kubeforce.payroll.gson.util.MoneyTypeAdapterFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author samueladebowale
 */
//@RunWith(MockitoJUnitRunner.class)
public class CurrencyUnitAdapterTest {

    private Gson gson = new GsonBuilder().registerTypeAdapterFactory(new MoneyTypeAdapterFactory())
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    @Test
    public void serializeNullCurrencyUnitValueAsObject() throws Exception {
        assertTrue("null".equals(gson.toJson(null, CurrencyUnit.class)));
    }

    @Test
    public void serializeConcreteCurrencyUnit() throws Exception {
        final Iterator<CurrencyUnit> amounts = buildCurrencyUnit();

        assertThat(gson.toJson(amounts.next(), CurrencyUnit.class), equalTo("\"CHF\""));
        assertThat(gson.toJson(amounts.next(), CurrencyUnit.class), equalTo("\"DKK\""));
        assertThat(gson.toJson(amounts.next(), CurrencyUnit.class), equalTo("\"EUR\""));
        assertThat(gson.toJson(amounts.next(), CurrencyUnit.class), equalTo("\"GBP\""));
        assertThat(gson.toJson(amounts.next(), CurrencyUnit.class), equalTo("\"NOK\""));
        assertThat(gson.toJson(amounts.next(), CurrencyUnit.class), equalTo("\"PLN\""));
        assertThat(gson.toJson(amounts.next(), CurrencyUnit.class), equalTo("\"SEK\""));
    }

    @Test
    public void deserializeNullCurrencyUnit() throws Exception {
        assertNull(gson.fromJson("null", CurrencyUnit.class));
    }

    @Test
    public void deserializeCurrencyUnitCollection() throws Exception {
        TypeToken<Collection<CurrencyUnit>> token = new TypeToken<Collection<CurrencyUnit>>() {
        };

        final Collection<CurrencyUnit> amountCollection = gson.fromJson("[null, \"EUR\"]", token.getType());

        assertThat(amountCollection, hasSize(2));
        assertThat(amountCollection, contains(nullValue(), equalTo(buildCurrencyUnit("EUR"))));
    }

    @Test
    public void deserializeWithUnknownCurrencyException() throws Exception {

        //thrown.expect(JsonSyntaxException.class);
        //thrown.expectMessage(equalTo("UnknownCurrencyException [currencyCode=EEE]"));
        Throwable exception = assertThrows(JsonSyntaxException.class, () -> gson.fromJson("\"EEE\"", CurrencyUnit.class));

        assertEquals("UnknownCurrencyException [currencyCode=EEE]", exception.getMessage());
    }

    private static Iterator<CurrencyUnit> buildCurrencyUnit() {
        return Arrays.asList(buildCurrencyUnit("CHF"), //
                buildCurrencyUnit("DKK"), //
                buildCurrencyUnit("EUR"), //
                buildCurrencyUnit("GBP"), //
                buildCurrencyUnit("NOK"), //
                buildCurrencyUnit("PLN"), //
                buildCurrencyUnit("SEK")).iterator();
    }

    private static CurrencyUnit buildCurrencyUnit(final String currencyCode) {
        return Monetary.getCurrency(currencyCode);
    }
}
