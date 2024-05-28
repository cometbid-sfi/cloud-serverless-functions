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

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import javax.money.CurrencyUnit;
import org.cometbid.kubeforce.payroll.gson.util.MoneyTypeAdapterFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

//@RunWith(MockitoJUnitRunner.class)
public class MonetaryAmountAdapterTest {

    private Gson gson = new GsonBuilder().registerTypeAdapterFactory(new MoneyTypeAdapterFactory())
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    @Test
    public void serializeNullMonetaryValueAsObject() throws Exception {
        assertTrue("null".equals(gson.toJson(null, MonetaryAmount.class)));
    }

    @Test
    public void serializeConcreteMonetaryValues() throws Exception {
        final Iterator<MonetaryAmount> amounts = buildMonetaryAmounts();

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":-1000000000,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":-10.01,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":-0.1,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":0,\"currency\":\"EUR\"}"));
        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":0.006,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":0.01,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":0.1,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":0.12,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":1,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":1,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":1.02,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":1.2,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":1.23,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":1.234,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":50,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":50.02,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":50.1,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":2000,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":2000.01,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":2000.32,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":2000000,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":2000000.01,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":2000000.23,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":2000000000,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":2000000000.03,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":2000000000.2,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":2000000000.23,\"currency\":\"EUR\"}"));

        assertThat(gson.toJson(amounts.next(), MonetaryAmount.class), //
                equalTo("{\"amount\":3000000000,\"currency\":\"EUR\"}"));
    }

    @Test
    public void deserializeNullMoneyAmount() throws Exception {
        assertNull(gson.fromJson("null", MonetaryAmount.class));
    }

    @Test
    public void deserializeMonetaryAmountCollection() throws Exception {
        TypeToken<Collection<MonetaryAmount>> token = new TypeToken<Collection<MonetaryAmount>>() {
        };

        final Collection<MonetaryAmount> amountCollection = gson.fromJson(
                "[null, {\"amount\":15.78,\"currency\":\"EUR\"}]", token.getType());

        assertThat(amountCollection, hasSize(2));
        assertThat(amountCollection, contains(nullValue(), equalTo(buildMoney(15.78, "EUR"))));
    }

    @Test
    public void deserializeMonetaryAmountWithAdditionalFields() throws Exception {
        final MonetaryAmount amount = gson.fromJson( //
                "{" //
                + "\"field\":1," //
                + "\"field2\":\"mock\"," //
                + "\"amount\":15.78," //
                + "\"field3\":42," //
                + "\"currency\":\"EUR\"," //
                + "\"field4\":\"345\"" //
                + "}", MonetaryAmount.class);

        assertThat(amount, equalTo(buildMoney(15.78, "EUR")));
    }

    @Test
    public void deserializeMonetaryAmountWithAdditionalObjects() throws Exception {
        final MonetaryAmount amount = gson.fromJson( //
                "{" //
                + "\"complex_object1\":{\"field1\":\"value1\"}," //
                + "\"complex_object2\":{\"field2\":\"value2\"}," //
                + "\"amount\":15.78," //
                + "\"complex_object3\":{\"field3\":3}," //
                + "\"currency\":\"EUR\"," //
                + "\"complex_object4\":{\"field4\":3.14}" //
                + "}", MonetaryAmount.class);

        assertThat(amount, equalTo(buildMoney(15.78, "EUR")));
    }

    @Test
    public void deserializeMonetaryAmountWithMixedAdditionalFields() throws Exception {
        final MonetaryAmount amount = gson.fromJson( //
                "{" //
                + "\"field1\":1," //
                + "\"complex_object2\":{\"field2\":\"value2\"}," //
                + "\"amount\":15.78," //
                + "\"complex_object3\":{\"field3\":3}," //
                + "\"currency\":\"EUR\"," //
                + "\"field4\":\"345\"" //
                + "}", MonetaryAmount.class);

        assertThat(amount, equalTo(buildMoney(15.78, "EUR")));
    }

    @Test
    public void deserializeWithStringAmount() throws Exception {

        final MonetaryAmount amount = gson.fromJson("{\"amount\":\"15.78\",\"currency\":\"EUR\"}",
                MonetaryAmount.class);

        assertThat(amount, equalTo(buildMoney(15.78, "EUR")));
    }

    @Test
    public void deserializeMissingAmountInMonetaryAmount() throws Exception {
        //thrown.expect(JsonSyntaxException.class);
        //thrown.expectMessage(equalTo("Missing required fields from Monetary Amount: [amount]."));

        Throwable exception = assertThrows(JsonSyntaxException.class, () -> gson.fromJson("{\"number\":15.78,\"currency\":\"EUR\"}", MonetaryAmount.class));

        assertEquals("Missing required fields from Monetary Amount: [amount].", exception.getMessage());
    }

    @Test
    public void deserializeMissingCurrencyInMonetaryAmount() throws Exception {
        //thrown.expect(JsonSyntaxException.class);
        //thrown.expectMessage(equalTo("Missing required fields from Monetary Amount: [currency]."));

        Throwable exception = assertThrows(JsonSyntaxException.class, () -> gson.fromJson("{\"amount\":15.78,\"cur\":\"EUR\"}", MonetaryAmount.class));

        assertEquals("Missing required fields from Monetary Amount: [currency].", exception.getMessage());

        //gson.fromJson("{\"amount\":15.78,\"cur\":\"EUR\"}", MonetaryAmount.class);
    }

    @Test
    public void deserializeWithNumberFormatException() throws Exception {
        //thrown.expect(JsonSyntaxException.class);
        //thrown.expectMessage(equalTo("Non numeric String contained in the [amount] field."));

        Throwable exception = assertThrows(JsonSyntaxException.class, () -> gson.fromJson("{\"amount\":\"ABC\",\"currency\":\"EUR\"}", MonetaryAmount.class));

        assertEquals("Non numeric String contained in the [amount] field.", exception.getMessage());

        // gson.fromJson("{\"amount\":\"ABC\",\"currency\":\"EUR\"}", MonetaryAmount.class);
    }

    @Test
    public void deserializeWithUnknownCurrencyException() throws Exception {
        //thrown.expect(JsonSyntaxException.class);
        //thrown.expectMessage(equalTo("UnknownCurrencyException [currencyCode=EEE]"));

        Throwable exception = assertThrows(JsonSyntaxException.class, () -> gson.fromJson("{\"amount\":12.78,\"currency\":\"EEE\"}", MonetaryAmount.class));

        assertEquals("UnknownCurrencyException [currencyCode=EEE]", exception.getMessage());

        //gson.fromJson("{\"amount\":12.78,\"currency\":\"EEE\"}", MonetaryAmount.class);
    }

    @Test
    public void serializeDelegatedMonetaryAmount() throws Exception {
        assertThat(gson.toJson(buildTestClass(), MonetaryAmountDelegation.class), //
                equalTo("{\"name\":\"Test Name\",\"money\":{\"amount\":10,\"currency\":\"EUR\"}}"));
    }

    @Test
    public void deserializeDelegatedMonetaryAmount() throws Exception {
        final MonetaryAmountDelegation delegation = gson.fromJson(
                "{\"name\":\"Test Name\",\"money\":{\"amount\":10,\"currency\":\"EUR\"}}",
                MonetaryAmountDelegation.class);

        assertThat(delegation.getMoney(), equalTo(buildTestClass().getMoney()));
    }

    private static Iterator<MonetaryAmount> buildMonetaryAmounts() {
        return Arrays.asList(buildMoney(-1000000000, "EUR"), buildMoney(-10.01, "EUR"), //
                buildMoney(-0.10, "EUR"), buildMoney(0, "EUR"), //
                buildMoney(0.006, "EUR"), buildMoney(0.01, "EUR"), //
                buildMoney(0.10, "EUR"), buildMoney(0.12, "EUR"), //
                buildMoney(1, "EUR"), buildMoney(1.0, "EUR"), //
                buildMoney(1.02, "EUR"), buildMoney(1.2, "EUR"), //
                buildMoney(1.23, "EUR"), buildMoney(1.234, "EUR"), //
                buildMoney(50, "EUR"), buildMoney(50.02, "EUR"), //
                buildMoney(50.10, "EUR"), buildMoney(2000, "EUR"), //
                buildMoney(2000.01, "EUR"), buildMoney(2000.32, "EUR"), //
                buildMoney(2000000, "EUR"), buildMoney(2000000.01, "EUR"), //
                buildMoney(2000000.23, "EUR"), buildMoney(2000000000, "EUR"), //
                buildMoney(2000000000.03, "EUR"), buildMoney(2000000000.20, "EUR"), //
                buildMoney(2000000000.23, "EUR"), buildMoney(3000000000L, "EUR")).iterator();
    }

    private static MonetaryAmount buildMoney(final Number amount, final String currencyCode) {
        return Monetary.getDefaultAmountFactory().setNumber(amount).setCurrency(currencyCode).create();
    }

    private MonetaryAmountDelegation buildTestClass() {
        MonetaryAmountDelegation delegation = new MonetaryAmountDelegation();

        delegation.setName("Test Name");
        delegation.setMoney(buildMoney(10, "EUR"));

        return delegation;
    }

    class MonetaryAmountDelegation {

        private String name;

        private MonetaryAmount money;

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public MonetaryAmount getMoney() {
            return money;
        }

        public void setMoney(final MonetaryAmount money) {
            this.money = money;
        }
    }
}
