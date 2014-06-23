/* 
 * Copyright 2013 Daniel Felix Ferber.
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
package br.com.danielferber.slf4jtoys.slf4j.profiler.meter;

import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import org.slf4j.Logger;

public class MeterFactory {

//    private static final Map<String, String> context = new TreeMap<String, String>();

    public static Meter getMeter(String name) {
        return new Meter(LoggerFactory.getLogger(name));
    }

    public static Meter getMeter(Class<?> clazz) {
        return new Meter(LoggerFactory.getLogger(clazz));
    }

    public static Meter getMeter(Class<?> clazz, String operationId) {
        return new Meter(LoggerFactory.getLogger(clazz, operationId));
    }

    public static Meter getMeter(Logger logger, String operationId) {
        return new Meter(LoggerFactory.getLogger(logger, operationId));
    }

//    public static void put(String name) {
//        context.put(name, null);
//    }
//
//    public static void put(String name, String value) {
//        context.put(name, value);
//    }
//
//    public static void remove(String name) {
//        context.remove(name);
//    }
//
//    static Map<String, String> getContext() {
//        return context;
//    }
}
