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
package org.cometbid.kubeforce.payroll.gson.util;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 *
 * @author samueladebowale
 */
/**
 * Allows Gson to deal with {@code javax.money} API.
 */
public class MoneyTypeAdapterFactory implements TypeAdapterFactory {

    private final MonetaryAmountFactory<? extends MonetaryAmount> monetaryFactory;

    /**
     * This is the Default implementation. Here, the implementation of the
     * {@code javax.money} defined in the classpath will be used.
     */
    public MoneyTypeAdapterFactory() {
        this(Monetary.getDefaultAmountFactory());
    }

    public MoneyTypeAdapterFactory(final MonetaryAmountFactory<? extends MonetaryAmount> monetaryFactory) {
        this.monetaryFactory = monetaryFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {

        final Class<T> clazz = (Class<T>) typeToken.getRawType();

        if (MonetaryAmount.class.isAssignableFrom(clazz)) {
            return (TypeAdapter<T>) new MonetaryAmountAdapter(monetaryFactory);
        } else if (CurrencyUnit.class.isAssignableFrom(clazz)) {
            return (TypeAdapter<T>) new CurrencyUnitAdapter();
        }

        return null;
    }
}
