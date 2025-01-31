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
package org.usefultoys.slf4j;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.io.OutputStream;
import java.io.PrintStream;

import static org.slf4j.LoggerFactory.getILoggerFactory;

/**
 * An alternative to {@link org.slf4j.LoggerFactory}, with additional useful
 * methods.
 *
 * @author Daniel Felix Ferber
 */
public final class LoggerFactory {

    private LoggerFactory() {
        // prevent instances
    }

    /**
     * Returns a logger named according to the name parameter using the
     * statically bound {@link ILoggerFactory} instance. Equivalent to
     * {@link org.slf4j.LoggerFactory#getLogger(String)}.
     * <p>
     * Recommended to get a special purpose logger defined by the application,
     * whose name does not follow the fully qualified name convention and that
     * tracks some globally available feature.
     *
     * @param name The name of the logger.
     * @return the logger
     */
    public static Logger getLogger(final String name) {
        return getILoggerFactory().getLogger(name);
    }

    /**
     * Returns a logger named according to the class passed as parameter, using
     * the statically bound {@link ILoggerFactory} instance.
     * <p>
     * Recommended to get a logger that tracks features provided by the class.
     *
     * @param clazz the returned logger will be named after {@literal clazz}
     * @return the logger
     */
    public static Logger getLogger(final Class<?> clazz) {
        return getILoggerFactory().getLogger(clazz.getName());
    }

    /**
     * Returns a logger named according to a parent logger and the operation or feature,
     * using the statically bound {@link ILoggerFactory} instance.
     * <p>
     * Recommended to get a logger subordinated to an existing logger named after a class.
     *
     * @param clazz the returned logger will be named after clazz
     * @param name the name of operation or feature appended to the parent logger name.
     * @return the logger
     */
    public static Logger getLogger(final Class<?> clazz, final String name) {
        return getILoggerFactory().getLogger(clazz.getName() + '.' + name);
    }

    /**
     * Returns a logger named according to a parent logger and the operation or feature,
     * using the statically bound {@link ILoggerFactory} instance.
     * <p>
     * Recommended to get a logger subordinated to an existing logger.
     *
     * @param logger the returned logger will be named after this parent logger
     * @param name the name of operation or feature appended to the parent logger name.
     * @return the logger
     */
    public static Logger getLogger(final Logger logger, final String name) {
        return getILoggerFactory().getLogger(logger.getName() + '.' + name);
    }

    /**
     * Returns a {@link PrintStream} whose close and flush methods write the formatted text as a trace message to logger. 
     *
     * @param logger the logger text is reported to.
     * @return the PrintStream that writes its text to the logger.
     */
    public static PrintStream getTracePrintStream(final Logger logger) {
        if (!logger.isTraceEnabled()) {
            return new NullPrintStream();
        }
        return new PrintStream(getTraceOutputStream(logger));
    }

    /**
     * Returns a {@link PrintStream} whose close and flush methods write the formatted text as a debug message to logger. 
     *
     * @param logger the logger text is reported to.
     * @return the PrintStream that writes its text to the logger.
     */
    public static PrintStream getDebugPrintStream(final Logger logger) {
        if (!logger.isDebugEnabled()) {
            return new NullPrintStream();
        }
        return new PrintStream(getDebugOutputStream(logger));
    }

    /**
     * Returns a {@link PrintStream} whose close and flush methods write the formatted text as an information message to logger. 
     *
     * @param logger the logger text is reported to.
     * @return the PrintStream that writes its text to the logger.
     */
    public static PrintStream getInfoPrintStream(final Logger logger) {
        if (!logger.isInfoEnabled()) {
            return new NullPrintStream();
        }
        return new PrintStream(getInfoOutputStream(logger));
    }

    /**
     * Returns a {@link PrintStream} whose close and flush methods write the formatted text as a warning message to logger. 
     *
     * @param logger the logger text is reported to.
     * @return the PrintStream that writes its text to the logger.
     */
    public static PrintStream getWarnPrintStream(final Logger logger) {
        if (!logger.isWarnEnabled()) {
            return new NullPrintStream();
        }
        return new PrintStream(getWarnOutputStream(logger));
    }

    /**
     * Returns a {@link PrintStream} whose close and flush methods write the formatted text as an error message to logger. 
     *
     * @param logger the logger text is reported to.
     * @return the PrintStream that writes its text to the logger.
     */
    public static PrintStream getErrorPrintStream(final Logger logger) {
        if (!logger.isErrorEnabled()) {
            return new NullPrintStream();
        }
        return new PrintStream(getErrorOutputStream(logger));
    }

    /**
     * Returns a {@link OutputStream} whose close and flush methods write the unformatted data as a trace message to logger. 
     *
     * @param logger the logger data is reported to.
     * @return the OutputStream that writes its data to the logger.
     */
    public static OutputStream getTraceOutputStream(final Logger logger) {
        if (!logger.isTraceEnabled()) {
            return new NullOutputStream();
        }
        return new LoggerOutputStream() {
            @Override
            protected void writeToLogger() {
                logger.trace(extractString());
            }
        };
    }

    /**
     * Returns a {@link OutputStream} whose close and flush methods write the unformatted data as a debug message to logger. 
     *
     * @param logger the logger data is reported to.
     * @return the OutputStream that writes its data to the logger.
     */
    public static OutputStream getDebugOutputStream(final Logger logger) {
        if (!logger.isDebugEnabled()) {
            return new NullOutputStream();
        }
        return new LoggerOutputStream() {
            @Override
            protected void writeToLogger() {
                logger.debug(extractString());
            }
        };
    }

    /**
     * Returns a {@link OutputStream} whose close and flush methods write the unformatted data as an information message to logger. 
     *
     * @param logger the logger data is reported to.
     * @return the OutputStream that writes its data to the logger.
     */
    public static OutputStream getInfoOutputStream(final Logger logger) {
        if (!logger.isInfoEnabled()) {
            return new NullOutputStream();
        }
        return new LoggerOutputStream() {
            @Override
            protected void writeToLogger() {
                logger.info(extractString());
            }
        };
    }

    /**
     * Returns a {@link OutputStream} whose close and flush methods write the unformatted data as a warning message to logger. 
     *
     * @param logger the logger data is reported to.
     * @return the OutputStream that writes its data to the logger.
     */
    public static OutputStream getWarnOutputStream(final Logger logger) {
        if (!logger.isWarnEnabled()) {
            return new NullOutputStream();
        }
        return new LoggerOutputStream() {
            @Override
            protected void writeToLogger() {
                logger.warn(extractString());
            }
        };
    }

    /**
     * Returns a {@link OutputStream} whose close and flush methods write the unformatted data as an error message to logger. 
     *
     * @param logger the logger data is reported to.
     * @return the OutputStream that writes its data to the logger.
     */
    public static OutputStream getErrorOutputStream(final Logger logger) {
        if (!logger.isErrorEnabled()) {
            return new NullOutputStream();
        }
        return new LoggerOutputStream() {
            @Override
            protected void writeToLogger() {
                logger.error(extractString());
            }
        };
    }

    /**
     * Returns a {@link PrintStream} whose close and flush methods write the structured text as a trace message to logger. 
     * Shortcut to {@code getTracePrintStream(LoggerFactory.getLogger(logger, name))}.
     *
     * @param logger the logger text is reported to.
     * @param name the name of operation or feature appended to the logger name.
     * @return the PrintStream that writes its text to the logger.
     */
    public static PrintStream getTracePrintStream(final Logger logger, final String name) {
        return getTracePrintStream(getLogger(logger, name));
    }

    /**
     * Returns a {@link PrintStream} whose close and flush methods write the structured text as a debug message to logger. 
     * Shortcut to {@code getDebugPrintStream(LoggerFactory.getLogger(logger, name))}.
     *
     * @param logger the logger text is reported to.
     * @param name the name of operation or feature appended to the logger name.
     * @return the PrintStream that writes its text to the logger.
     */
    public static PrintStream getDebugPrintStream(final Logger logger, final String name) {
        return getDebugPrintStream(getLogger(logger, name));
    }

    /**
     * Returns a {@link PrintStream} whose close and flush methods write the structured text as an information message to logger. 
     * Shortcut to {@code getInfoPrintStream(LoggerFactory.getLogger(logger, name))}.
     *
     * @param logger the logger text is reported to.
     * @param name the name of operation or feature appended to the logger name.
     * @return the PrintStream that writes its text to the logger.
     */
    public static PrintStream getInfoPrintStream(final Logger logger, final String name) {
        return getInfoPrintStream(getLogger(logger, name));
    }

    /**
     * Returns a {@link PrintStream} whose close and flush methods write the structured text as a warning message to logger. 
     * Shortcut to {@code getWarnPrintStream(LoggerFactory.getLogger(logger, name))}.
     *
     * @param logger the logger text is reported to.
     * @param name the name of operation or feature appended to the logger name.
     * @return the PrintStream that writes its text to the logger.
     */
    public static PrintStream getWarnPrintStream(final Logger logger, final String name) {
        return getWarnPrintStream(getLogger(logger, name));
    }

    /**
     * Returns a {@link PrintStream} whose close and flush methods write the structured text as an error message to logger. 
     * Shortcut to {@code getErrorPrintStream(LoggerFactory.getLogger(logger, name))}.
     *
     * @param logger the logger text is reported to.
     * @param name the name of operation or feature appended to the logger name.
     * @return the PrintStream that writes its text to the logger.
     */
    public static PrintStream getErrorPrintStream(final Logger logger, final String name) {
        return getErrorPrintStream(getLogger(logger, name));
    }

    /**
     * Returns a {@link OutputStream} whose close and flush methods write the unformatted data as a trace message to logger. 
     * Shortcut to {@code getTraceOutputStream(LoggerFactory.getLogger(logger, name))}.
     *
     * @param logger the logger data is reported to.
     * @param name the name of operation or feature appended to the logger name.
     * @return the OutputStream that writes its data to the logger.
     */
    public static OutputStream getTraceOutputStream(final Logger logger, final String name) {
        return getTraceOutputStream(getLogger(logger, name));

    }

    /**
     * Returns a {@link OutputStream} whose close and flush methods write the unformatted data as a debug message to logger. 
     * Shortcut to {@code getDebugOutputStream(LoggerFactory.getLogger(logger, name))}.
     *
     * @param logger the logger data is reported to.
     * @param name the name of operation or feature appended to the logger name.
     * @return the OutputStream that writes its data to the logger.
     */
    public static OutputStream getDebugOutputStream(final Logger logger, final String name) {
        return getDebugOutputStream(getLogger(logger, name));
    }

    /**
     * Returns a {@link OutputStream} whose close and flush methods write the unformatted data as an information message to logger. 
     * Shortcut to {@code getInfoOutputStream(LoggerFactory.getLogger(logger, name))}.
     *
     * @param logger the logger data is reported to.
     * @param name the name of operation or feature appended to the logger name.
     * @return the OutputStream that writes its data to the logger.
     */
    public static OutputStream getInfoOutputStream(final Logger logger, final String name) {
        return getInfoOutputStream(getLogger(logger, name));
    }

    /**
     * Returns a {@link OutputStream} whose close and flush methods write the unformatted data as a warning message to logger. 
     * Shortcut to {@code getWarnOutputStream(LoggerFactory.getLogger(logger, name))}.
     *
     * @param logger the logger data is reported to.
     * @param name the name of operation or feature appended to the logger name.
     * @return the OutputStream that writes its data to the logger.
     */
    public static OutputStream getWarnOutputStream(final Logger logger, final String name) {
        return getWarnOutputStream(getLogger(logger, name));
    }

    /**
     * Returns a {@link OutputStream} whose close and flush methods write the unformatted data as an error message to logger. 
     * unformatted data as a error level message to to the logger.
     * Shortcut to {@code getErrorOutputStream(LoggerFactory.getLogger(logger, name))}.
     *
     * @param logger the logger data is reported to.
     * @param name the name of operation or feature appended to the logger name.
     * @return the OutputStream that writes its data to the logger.
     */
    public static OutputStream getErrorOutputStream(final Logger logger, final String name) {
        return getErrorOutputStream(getLogger(logger, name));
    }
}
