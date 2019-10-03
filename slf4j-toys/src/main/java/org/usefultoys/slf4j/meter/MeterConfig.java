/*
 * Copyright 2019 Daniel Felix Ferber
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.usefultoys.slf4j.meter;

import org.usefultoys.slf4j.internal.Config;

/**
 * Collection of properties that drive {@link Meter} and {@link MeterData} behavior.
 * Initial values are read from system properties at application startup, if available.
 * They may be assigned at application startup, before calling any {@link Meter} methods.
 * Some properties allow reassigning their values at runtime.
 *
 * @author Daniel Felix Ferber
 */
public class MeterConfig {

    /**
     * Time to wait before reporting next progress status, in milliseconds. Meter allows reporting progress status of incremental operations by
     * calling the {@link Meter#inc()}, {@link Meter#incBy(long)} and {@link Meter#incTo(long)} method on each step. To prevent crowding the log file and
     * to prevent performance degradation, Meter waits a minimal amount of time before printing the next status message. Value is read from system
     * property {@code slf4jtoys.meter.progress.period} at application startup and defaults to {@code 2 seconds}. The number represents a long integer
     * that represents milliseconds. The system property allows the number suffixed with 'ms', 's', 'm' and 'h' to represent milliseconds, seconds,
     * minutes and hours. You may assign a new value at runtime.
     */
    public static long progressPeriodMilliseconds = Config.getMillisecondsProperty("slf4jtoys.meter.progress.period", 2000L);

    /**
     * If {@link Meter} and {@link MeterData} print the category on the 1-line summary message. The category groups operations that are closely
     * related. Usually, the category is the same as logger name declared within the class that creates the `Meter`. The usual logger configuration
     * already includes the logger name. But if your logger configuration omits the logger name, then you may set this property to true. Value is read
     * from system property {@code slf4jtoys.meter.print.category} at application startup, defaults to {@code false}. You may assign a new value at
     * runtime.
     */
    public static boolean printCategory = Config.getProperty("slf4jtoys.meter.print.category", false);

    /**
     * If {@link Meter} and {@link MeterData} print the status at the beginning of the 1-line summary message.
     * The Meter status is closely related to the logger level. If using Logback as logging framework, it is possible
     * to display the Meter status instead of the logger level and the status may be ommited from the message. Value is read
     * from system property {@code slf4jtoys.meter.print.status} at application startup, defaults to {@code true}. You may assign a new value at
     * runtime.
     */
    public static boolean printStatus = Config.getProperty("slf4jtoys.meter.print.status", true);

    /**
     * If {@link Meter} and {@link MeterData} print the event position on the 1-line summary message.
     * <p>Value is read from system property {@code slf4jtoys.meter.print.position} at application startup, defaults to {@code true}.
     * You may assign a new value at runtime.
     */
    public static boolean printPosition = Config.getProperty("slf4jtoys.meter.print.position", false);
    public static boolean printLoad = Config.getProperty("slf4jtoys.meter.print.load", false);
    public static boolean printMemory = Config.getProperty("slf4jtoys.meter.print.memory", false);

    /**
     * A prefix added to all loggers that write encoded event data with trace level.
     * The prefix allows one to output event data on a separate logger hierarchy, which eases running an independent configuration
     * hierarchy (enabling/disabling loggers, writing to separated appender) without needing
     * to create complex filters to distinguish readable messages from encoded data.
     * <p>For example, by setting the prefix to {@code 'data.'}, a Meter using logger {@code a.b.c.MyClass} will write
     * readable messages to {@code a.b.c.MyClass} and encoded event data to {@code data.a.b.c.MyClass}.
     * <p>Value is read from system property {@code slf4jtoys.meter.data.prefix} at application startup, defaults to empty.
     * You may assign a new value at runtime.
     */
    public static String dataPrefix = Config.getProperty("slf4jtoys.meter.data.prefix", "");
    /**
     * A suffix added to all loggers that write encoded event data with trace level.
     * The suffix allows one to output event data on a separate logger within the same logger hierarchy, which allows
     * customizing the logger configuration for individual data loggers.
     * <p>For example, by setting the suffix to {@code '.data'}, a Meter using logger {@code a.b.c.MyClass} will write
     * readable messages to {@code a.b.c.MyClass} and encoded event data to {@code a.b.c.MyClass.data}.
     * <p>Value is read from system property {@code slf4jtoys.meter.data.suffix} at application startup, defaults to empty.
     * You may assign a new value at runtime.
     */
    public static String dataSuffix = Config.getProperty("slf4jtoys.meter.data.suffix", "");
    /**
     * A prefix added to all loggers that write readable messages.
     * The prefix allows one to output readable messages on a separate logger hierarchy, which eases running an independent configuration
     * hierarchy (enabling/disabling loggers, writing to separated appender) without needing
     * to create complex filters to distinguish readable messages from encoded data.
     * <p>For example, by setting the prefix to {@code 'message.'}, a Meter using logger {@code a.b.c.MyClass} will write
     * readable messages to {@code message.a.b.c.MyClass} and encoded event data to {@code a.b.c.MyClass}.
     * <p>Value is read from system property {@code slf4jtoys.meter.message.prefix} at application startup, defaults to empty.
     * You may assign a new value at runtime.
     */
    public static String messagePrefix = Config.getProperty("slf4jtoys.meter.message.prefix", "");
    /**
     * A suffix added to all loggers that readable messages.
     * The suffix allows one to output readable messages on a separate logger within the same logger hierarchy, which allows
     * customizing the logger configuration for individual data loggers.
     * <p>For example, by setting the suffix to {@code '.data'}, a Meter using logger {@code a.b.c.MyClass} will write
     * readable messages to {@code a.b.c.MyClass.message} and encoded event data to {@code a.b.c.MyClass}.
     * <p>Value is read from system property {@code slf4jtoys.meter.message.suffix} at application startup, defaults to empty.
     * You may assign a new value at runtime.
     */
    public static String messageSuffix = Config.getProperty("slf4jtoys.meter.message.suffix", "");
    /**
     *
     */
    public static int dataUuidSize = Config.getProperty("slf4jtoys.meter.data.uuid.size", 10);
}
