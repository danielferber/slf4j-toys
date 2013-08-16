/*
 * Copyright 2012 Daniel Felix Ferber
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.danielferber.slf4jtoys.slf4j.profiler.internal;

public class LoggerMessageWriter {

    /* Internal parser state. */
    private boolean firstProperty;
    StringBuilder builder;
    
    public LoggerMessageWriter reset(StringBuilder builder) {
        firstProperty = true;
        this.builder = builder;
        return this;
    }

    public LoggerMessageWriter writeQuotedString(final String string) {
        builder.append(Syntax.STRING_DELIM);
        builder.append(string.replace(Syntax.STRING_DELIM_STR, Syntax.STRING_DELIM_QUOTED_STR));
        builder.append(Syntax.STRING_DELIM);
        return this;
    }

    public LoggerMessageWriter openData(char c) {
        builder.append(c);
        builder.append(Syntax.DATA_OPEN);
        return this;
    }

    public LoggerMessageWriter closeData() {
        builder.append(Syntax.DATA_CLOSE);
        return this;
    }

    public LoggerMessageWriter property(String name, long value) {
        property(name, Long.toString(value));
        return this;
    }

    public LoggerMessageWriter property(String name, long value1, long value2) {
        property(name, Long.toString(value1), Long.toString(value2));
        return this;
    }

    public LoggerMessageWriter property(String name, long value1, long value2, long value3) {
        property(name, Long.toString(value1), Long.toString(value2), Long.toString(value3));
        return this;
    }

    public LoggerMessageWriter property(String name, long value1, long value2, long value3, long value4) {
        property(name, Long.toString(value1), Long.toString(value2), Long.toString(value3), Long.toString(value4));
        return this;
    }

    public LoggerMessageWriter property(String name, double value) {
        property(name, Double.toString(value));
        return this;
    }

    public LoggerMessageWriter property(String name, String value) {
        if (!firstProperty) {
            builder.append(Syntax.PROPERTY_SEPARATOR);
        } else {
            firstProperty = false;
        }
        builder.append(name);
        builder.append(Syntax.PROPERTY_EQUALS);
        builder.append(value);
        return this;
    }

    public LoggerMessageWriter property(String name, String value1, String value2) {
        if (!firstProperty) {
            builder.append(Syntax.PROPERTY_SEPARATOR);
        } else {
            firstProperty = false;
        }
        builder.append(name);
        builder.append(Syntax.PROPERTY_EQUALS);
        builder.append(value1);
        builder.append(Syntax.PROPERTY_DIV);
        builder.append(value2);
        return this;
    }

    public LoggerMessageWriter property(String name, String value1, String value2, String value3) {
        if (!firstProperty) {
            builder.append(Syntax.PROPERTY_SEPARATOR);
        } else {
            firstProperty = false;
        }
        builder.append(name);
        builder.append(Syntax.PROPERTY_EQUALS);
        builder.append(value1);
        builder.append(Syntax.PROPERTY_DIV);
        builder.append(value2);
        builder.append(Syntax.PROPERTY_DIV);
        builder.append(value3);
        return this;
    }

    public LoggerMessageWriter property(String name, String value1, String value2, String value3, String value4) {
        if (!firstProperty) {
            builder.append(Syntax.PROPERTY_SEPARATOR);
        } else {
            firstProperty = false;
        }
        builder.append(name);
        builder.append(Syntax.PROPERTY_EQUALS);
        builder.append(value1);
        builder.append(Syntax.PROPERTY_DIV);
        builder.append(value2);
        builder.append(Syntax.PROPERTY_DIV);
        builder.append(value3);
        builder.append(Syntax.PROPERTY_DIV);
        builder.append(value4);
        return this;
    }
}
