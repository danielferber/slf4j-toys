/*
 * Copyright 2016 x7ws.
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
package org.usefultoys.slf4j.watcher;

import org.usefultoys.slf4j.internal.Config;

/**
 * Collection of properties that drive {@link Watcher} and {@link WatcherData} behavior.
 * Initial values are read from system properties, if available.
 * Some properties allow reassigning their values at runtime.
 */
public class WatcherConfig {
    /**
     * Time to wait before reporting the first watcher status, in milliseconds.
     * Value is read from system property {@code slf4jtoys.watcher.delay} at application startup, defaults to {@code 1 minute}.
     * You may assign a new value at runtime, but if the default watcher is already running, you need to restart it.
     */
    public static long delayMilliseconds = Config.getMillisecondsProperty("slf4jtoys.watcher.delay", 60000L);
    /**
     * Time period to wait before reporting further watcher status, in milliseconds.
     * Time to wait before reporting the first watcher status, in milliseconds.
     * Value is read from system property {@code slf4jtoys.watcher.period} at application startup, defaults to {@code 10 minutes}.
     * You may assign a new value at runtime, but if the default watcher is already running, you need to restart it.
     */
    public static long periodMilliseconds = Config.getMillisecondsProperty("slf4jtoys.watcher.period", 600000L);
}