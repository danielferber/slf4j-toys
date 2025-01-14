/*
 * Copyright 2024 Daniel Felix Ferber
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
package org.usefultoys.slf4j.examples.report;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.usefultoys.slf4j.Session;
import org.usefultoys.slf4j.report.Reporter;

/**
 * Demonstrates the default report generated by
 * {@link Session#runDefaultReport()}, but choosing a custom set of reports.
 *
 * @author Daniel Felix Ferber
 */
public class CustomReport2 {

    public static void main(String[] args) {

        Executor executor = Executors.newFixedThreadPool(3);
        Reporter reporter = new Reporter();
        executor.execute(reporter.new ReportPhysicalSystem());
        executor.execute(reporter.new ReportMemory());
    }
}
