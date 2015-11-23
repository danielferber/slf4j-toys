/*
 * Copyright 2015 Daniel Felix Ferber.
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
package org.usefultoys.slf4j;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.usefultoys.slf4j.watcher.Watcher;

/**
 * Profiling session for the current JVM. Stores the UUID logged on each message
 * on the current JVM. Keeps the timer calls the watcher periodically.
 *
 * @author Daniel Felix Ferber
 */
public final class Session {

    private Session() {
        // prevent instances
    }

    public static final String uuid = UUID.randomUUID().toString().replace("-", "");
    public static boolean useMemoryManagedBean = getProperty("slf4jtoys.useMemoryManagedBean", false);
    public static boolean useClassLoadingManagedBean = getProperty("slf4jtoys.useClassLoadingManagedBean", false);
    public static boolean useCompilationManagedBean = getProperty("slf4jtoys.useCompilationManagedBean", false);
    public static boolean useGarbageCollectionManagedBean = getProperty("slf4jtoys.useGarbageCollectionManagedBean", false);
    public static boolean usePlatformManagedBean = getProperty("slf4jtoys.usePlatformManagedBean", false);

    public static final Watcher DEFAULT_WATCHER = new Watcher(LoggerFactory.getLogger(readWatcherLoggerName()));
    
    private static ScheduledExecutorService defaultWatcherExecutor = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledFuture<?> scheduledDefaultWatcher;

    public static synchronized void startDefaultWatcher() {
        if (defaultWatcherExecutor == null) {
            defaultWatcherExecutor = Executors.newSingleThreadScheduledExecutor();
        }
        if (scheduledDefaultWatcher == null) {
            scheduledDefaultWatcher = defaultWatcherExecutor.scheduleAtFixedRate(DEFAULT_WATCHER, readWatcherDelayMillisecondsProperty(), readWatcherPeriodMillisecondsProperty(), TimeUnit.MILLISECONDS);
        }
    }

    public static synchronized void stopDefaultWatcher() {
        if (scheduledDefaultWatcher != null) {
            scheduledDefaultWatcher.cancel(true);
        }
        if (defaultWatcherExecutor != null) {
            defaultWatcherExecutor.shutdownNow();
            defaultWatcherExecutor = null;
        }
    }

    public static String getProperty(final String name, final String defaultValue) {
        final String value = System.getProperty(name);
        return value == null ? defaultValue : value;
    }

    public static boolean getProperty(final String name, final boolean defaultValue) {
        final String value = System.getProperty(name);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public static int getProperty(final String name, final int defaultValue) {
        final String value = System.getProperty(name);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (final NumberFormatException e) {
            return defaultValue;
        }
    }

    public static long getProperty(final String name, final long defaultValue) {
        final String value = System.getProperty(name);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (final NumberFormatException e) {
            return defaultValue;
        }
    }

    public static long getMillisecondsProperty(final String name, final long defaultValue) {
        final String value = System.getProperty(name);
        if (value == null) {
            return defaultValue;
        }
        try {
            int multiplicador = 1;
            int suffixLength = 1;
            if (value.endsWith("ms")) {
                suffixLength = 2;
            } else if (value.endsWith("s")) {
                multiplicador = 1000;
            } else if (value.endsWith("m")) {
                multiplicador = 60 * 1000;
            } else if (value.endsWith("h")) {
                multiplicador = 60 * 60 * 1000;
            } else {
                return defaultValue;
            }
            return Long.parseLong(value.substring(0, value.length() - suffixLength)) * multiplicador;

        } catch (final NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean readMeterPrintCategoryProperty() {
        return Session.getProperty("slf4jtoys.meter.print.category", true);
    }

    public static long readMeterProgressPeriodProperty() {
        return Session.getMillisecondsProperty("slf4jtoys.meter.progress.period", 2000L);
    }

    public static long readWatcherPeriodMillisecondsProperty() {
        return getMillisecondsProperty("slf4jtoys.watcher.period", 600000L);
    }

    public static long readWatcherDelayMillisecondsProperty() {
        return getMillisecondsProperty("slf4jtoys.watcher.delay", 60000L);
    }
    
    public static String readWatcherLoggerName() {
        return getProperty("slf4jtoys.watcher.name", "watcher");
    }
}