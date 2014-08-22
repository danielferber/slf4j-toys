/*
 * Copyright 2013 Daniel Felix Ferber
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
package br.com.danielferber.slf4jtoys.slf4j.profiler.meter;

import br.com.danielferber.slf4jtoys.slf4j.profiler.ProfilingSession;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;

/**
 *
 * @author Daniel Felix Ferber
 */
public class Meter extends MeterData implements Closeable {

    private static final long serialVersionUID = 1L;
    transient private final Logger logger;
    private static final String NULL_VALUE = "<null>";
    /**
     * How many times each event has been executed.
     */
    private static final ConcurrentMap<String, AtomicLong> eventCounterByName = new ConcurrentHashMap<String, AtomicLong>();
    private long timeLimit = 0;
    transient long lastProgressTime = 0;
    transient long lastProgressIteration = 0;
    transient long limitProgressTime = 2000000;

    /**
     * Creates a new meter.
     *
     * @param logger
     */
    public Meter(Logger logger) {
        super();
        this.sessionUuid = ProfilingSession.uuid;
        this.logger = logger;
        this.eventCategory = logger.getName();
        eventCounterByName.putIfAbsent(this.eventCategory, new AtomicLong(0));
        this.eventPosition = eventCounterByName.get(this.eventCategory).incrementAndGet();
        this.createTime = System.nanoTime();
    }

    /**
     * Logger that receives messages from this meter.
     *
     * @return
     */
    public Logger getLogger() {
        return logger;
    }

    // ========================================================================
    /**
     * Creates a new mwter whose name is under the hierarchy of this meter.
     * Useful if a large task may be subdivided into smaller task and reported
     * individually. The new meter uses the name of this meter, appended my its
     * name, similar as logger do.
     *
     * @param name
     * @return
     */
    public Meter sub(String name) {
        final Meter m = MeterFactory.getMeter(eventCategory + '.' + name);
        if (this.context != null) {
            m.context = new HashMap<String, String>(this.context);
        }
        return m;
    }

    // ========================================================================
    /**
     * Configures the meter with a human readable message that explains the task
     * purpose.
     *
     * @param message fixed message
     * @return reference to the meter itself.
     */
    public Meter m(String message) {
        this.description = message;
        return this;
    }

    /**
     * Configures the meter with a human readable message that explains the task
     * purpose.
     *
     * @param message message format ({@link String#format(java.lang.String, java.lang.Object...)
     * })
     * @param args message arguments
     * @return reference to the meter itself.
     */
    public Meter m(String message, Object... args) {
        try {
            this.description = String.format(message, args);
        } catch (IllegalFormatException e) {
            logger.error("Illegal string format in Meter.setMessage(message, args...)", e);
        }
        return this;
    }

    /**
     * Configures the meter with an threshold for reasonable, typical execution
     * time for the task represented by the meter.
     *
     * @param timeLimit time threshold
     * @return reference to the meter itself.
     */
    public Meter limit(long timeLimit) {
        this.timeLimit = timeLimit;
        return this;
    }

    /**
     * Configures the meter as representing a task made up of iterations or
     * steps. Such meters are allows to call {@link #progress() } an arbitrarily
     * number of times between {@link #start() } and {@link #ok() }/{@link #fail(java.lang.Throwable)
     * } method calls.
     *
     * @param i Number of expected iterations or steps that make up the task
     * @return reference to the meter itself.
     */
    public Meter iterations(long i) {
        this.expectedIteration = i;
        return this;
    }

    // ========================================================================
    /**
     * Adds an entry to the context map. The entry has no value and is
     * interpreted as a marker.
     *
     * @param name key of the entry to add.
     * @return reference to the meter itself.
     */
    public Meter ctx(String name) {
        if (context == null) {
            this.context = new HashMap<String, String>();
        }
        context.put(name, null);
        return this;
    }

    /**
     * Adds an entry to the context map.
     *
     * @param name key of the entry to add.
     * @param value value of the entry to add.
     * @return reference to the meter itself.
     */
    public Meter ctx(String name, int value) {
        if (context == null) {
            this.context = new HashMap<String, String>();
        }
        context.put(name, Integer.toString(value));
        return this;
    }

    /**
     * Adds an entry to the context map.
     *
     * @param name key of the entry to add.
     * @param value value of the entry to add.
     * @return reference to the meter itself.
     */
    public Meter ctx(String name, long value) {
        if (context == null) {
            this.context = new HashMap<String, String>();
        }
        context.put(name, Long.toString(value));
        return this;
    }

    /**
     * Adds an entry to the context map.
     *
     * @param name key of the entry to add.
     * @param value value of the entry to add.
     * @return reference to the meter itself.
     */
    public Meter ctx(String name, boolean value) {
        if (context == null) {
            this.context = new HashMap<String, String>();
        }
        context.put(name, Boolean.toString(value));
        return this;
    }

    /**
     * Adds an entry to the context map.
     *
     * @param name key of the entry to add.
     * @param value value of the entry to add.
     * @return reference to the meter itself.
     */
    public Meter ctx(String name, float value) {
        if (context == null) {
            this.context = new HashMap<String, String>();
        }
        context.put(name, Float.toString(value));
        return this;
    }

    /**
     * Adds an entry to the context map.
     *
     * @param name key of the entry to add.
     * @param value value of the entry to add.
     * @return reference to the meter itself.
     */
    public Meter ctx(String name, double value) {
        if (context == null) {
            this.context = new HashMap<String, String>();
        }
        context.put(name, Double.toString(value));
        return this;
    }

    /**
     * Adds an entry to the context map.
     *
     * @param name key of the entry to add.
     * @param value value of the entry to add.
     * @return reference to the meter itself.
     */
    public Meter ctx(String name, Integer value) {
        if (context == null) {
            this.context = new HashMap<String, String>();
        }
        context.put(name, value == null ? NULL_VALUE : value.toString());
        return this;
    }

    /**
     * Adds an entry to the context map.
     *
     * @param name key of the entry to add.
     * @param value value of the entry to add.
     * @return reference to the meter itself.
     */
    public Meter ctx(String name, Long value) {
        if (context == null) {
            this.context = new HashMap<String, String>();
        }
        context.put(name, value == null ? NULL_VALUE : value.toString());
        return this;
    }

    /**
     * Adds an entry to the context map.
     *
     * @param name key of the entry to add.
     * @param value value of the entry to add.
     * @return reference to the meter itself.
     */
    public Meter ctx(String name, Boolean value) {
        if (context == null) {
            this.context = new HashMap<String, String>();
        }
        context.put(name, value == null ? NULL_VALUE : value.toString());
        return this;
    }

    /**
     * Adds an entry to the context map.
     *
     * @param name key of the entry to add.
     * @param value value of the entry to add.
     * @return reference to the meter itself.
     */
    public Meter ctx(String name, Float value) {
        if (context == null) {
            this.context = new HashMap<String, String>();
        }
        context.put(name, value == null ? NULL_VALUE : value.toString());
        return this;
    }

    /**
     * Adds an entry to the context map.
     *
     * @param name key of the entry to add.
     * @param value value of the entry to add.
     * @return reference to the meter itself.
     */
    public Meter ctx(String name, Double value) {
        if (context == null) {
            this.context = new HashMap<String, String>();
        }
        context.put(name, value == null ? NULL_VALUE : value.toString());
        return this;
    }

    /**
     * Adds an entry to the context map.
     *
     * @param name key of the entry to add.
     * @param object object which string representation is used for the value of
     * the entry to add
     * @return reference to the meter itself.
     */
    public Meter ctx(String name, Object object) {
        if (context == null) {
            this.context = new HashMap<String, String>();
        }
        context.put(name, object == null ? NULL_VALUE : object.toString());
        return this;
    }

    /**
     * Adds an entry to the context map.
     *
     * @param name key of the entry to add.
     * @param value value of the entry to add.
     * @return reference to the meter itself.
     */
    public Meter ctx(String name, String value) {
        if (context == null) {
            this.context = new HashMap<String, String>();
        }
        context.put(name, value);
        return this;
    }

    /**
     * Adds an entry to the context map. The entry value is made up of a
     * formatted message with arguments.
     *
     * @param name key of the entry to add.
     * @param format message format ({@link String#format(java.lang.String, java.lang.Object...)
     * })
     * @param objects message arguments
     * @return reference to the meter itself.
     */
    public Meter ctx(String name, String format, Object... objects) {
        if (context == null) {
            this.context = new HashMap<String, String>();
        }
        try {
            ctx(name, String.format(format, objects));
        } catch (IllegalFormatException e) {
            logger.error("Illegal string format in Meter.ctx(name, format, objects...)", e);
        }
        return this;
    }

    /**
     * Removes an entry from the context map.
     *
     * @param name key of the entry to remove.
     * @return reference to the meter itself.
     */
    public Meter unctx(String name) {
        if (context == null) {
            return this;
        }
        context.remove(name);
        return this;
    }

    // ========================================================================
    /**
     * Notifies the meter in order to claim immediate execution start of the
     * task represented by the meter. Sends a message to logger using debug
     * level. Sends a message with system status and partial context to log
     * using trace level.
     *
     * @return reference to the meter itself.
     */
    public Meter start() {
        assert createTime != 0;
        try {
            if (startTime != 0) {
                /* Log exception to provide stacktrace to inconsistent meter call. */
                logger.error(Slf4JMarkers.INCONSISTENT_START, "Meter already started: " + this.eventCategory + ":" + this.eventPosition, new Exception("Meter.start(): startTime != 0"));
            }

            Thread currentThread = Thread.currentThread();
            this.threadStartId = currentThread.getId();
            this.threadStartName = currentThread.getName();
            this.lastProgressTime = this.startTime = System.nanoTime();

            if (logger.isDebugEnabled()) {
                collectSystemStatus();
                logger.debug(Slf4JMarkers.MSG_START, readableString(new StringBuilder()).toString());
            }
            if (logger.isTraceEnabled()) {
                logger.trace(Slf4JMarkers.DATA_START, write(new StringBuilder(), 'M').toString());
            }

        } catch (Exception t) {
            logger.error(Slf4JMarkers.BUG, "Meter start threw exception: " + this.eventCategory + ":" + this.eventPosition, t);
        }
        return this;
    }

    // ========================================================================
    /**
     * Notifies the meter that one more iteration or step completed that make up
     * the task successfully.
     *
     * @return reference to the meter itself.
     */
    public Meter inc() {
        this.currentIteration++;
        return this;
    }

    /**
     * Notifies the meter that more of iterations or steps that make up the task
     * completed successfully.
     *
     * @param i the number of iterations or steps
     * @return reference to the meter itself.
     */
    public Meter incBy(long i) {
        this.currentIteration += i;
        return this;
    }

    /**
     * Notifies the meter that a number of iterations or steps that make up the
     * task already completed successfully.
     *
     * @param i the number of iterations or steps
     * @return reference to the meter itself.
     */
    public Meter incTo(long i) {
        this.currentIteration = i;
        return this;
    }

    /**
     * Allow informing about successful completion of iterations or steps making
     * up the task represented by the meter. Only applicable for meters that
     * called {@link #iterations(long i)} before calling {@link #start() }.
     * Sends a message to logger using info level, only periodically and if
     * progress was observed, to minimize performance degradation.
     *
     * @return reference to the meter itself.
     */
    public Meter progress() {
        try {
            long now;
            if (currentIteration > lastProgressIteration && ((now = System.nanoTime()) - lastProgressTime) > limitProgressTime) {
                lastProgressIteration = currentIteration;
                lastProgressTime = now;

                if (logger.isInfoEnabled()) {
                    collectSystemStatus();
                    logger.info(Slf4JMarkers.MSG_OK, readableString(new StringBuilder()).toString());
                }
                if (startTime != 0 && timeLimit != 0 && now - startTime > timeLimit) {
                    logger.trace(Slf4JMarkers.DATA_SLOW_PROGRESS, write(new StringBuilder(), 'M').toString());
                } else if (logger.isTraceEnabled()) {
                    logger.trace(Slf4JMarkers.DATA_PROGRESS, write(new StringBuilder(), 'M').toString());
                }
            }
        } catch (Exception t) {
            /* Prevent bugs from disrupting the application. Log exception to provide stacktrace to bug. */
            logger.error(Slf4JMarkers.BUG, "Meter confirmation threw exception: " + this.eventCategory + ":" + this.eventPosition, t);
        }
        return this;
    }

    // ========================================================================
    /**
     * Confirms the meter in order to claim successful completion of the task
     * represented by the meter. Sends a message to logger using info level.
     * Sends a message with system status and partial context to log using trace
     * level. Sends a message if the task executed for more time than the
     * optionally configured time threshold.
     *
     * @return reference to the meter itself.
     */
    public Meter ok() {
        assert createTime != 0;
        try {
            if (stopTime != 0) {
                /* Log exception to provide stacktrace to inconsistent meter call. */
                logger.error(Slf4JMarkers.INCONSISTENT_OK, "Meter already refused or confirmed: " + this.eventCategory + ":" + this.eventPosition, new Exception("Meter.ok(...): stopTime != 0"));
            }
            stopTime = System.nanoTime();
            if (startTime == 0) {
                /* Log exception to provide stacktrace to inconsistent meter call. */
                logger.error(Slf4JMarkers.INCONSISTENT_OK, "Meter confirmed but not started: " + this.eventCategory + ":" + this.eventPosition, new Exception("Meter.ok(...): startTime == 0"));
            }
            success = true;

            Thread currentThread = Thread.currentThread();
            this.threadStopId = currentThread.getId();
            this.threadStopName = currentThread.getName();

            if (logger.isInfoEnabled()) {
                collectSystemStatus();
                logger.info(Slf4JMarkers.MSG_OK, readableString(new StringBuilder()).toString());
            }
            if (startTime != 0 && timeLimit != 0 && stopTime - startTime > timeLimit) {
                logger.trace(Slf4JMarkers.DATA_SLOW_OK, write(new StringBuilder(), 'M').toString());
            } else if (logger.isTraceEnabled()) {
                logger.trace(Slf4JMarkers.DATA_OK, write(new StringBuilder(), 'M').toString());
            }
        } catch (Exception t) {
            /* Prevent bugs from disrupting the application. Log exception to provide stacktrace to bug. */
            logger.error(Slf4JMarkers.BUG, "Meter confirmation threw exception: " + this.eventCategory + ":" + this.eventPosition, t);
        }
        return this;
    }

    // ========================================================================
    /**
     * Refuses the meter in order to claim incomplete or inconsistent execution
     * of the task represented by the meter. Sends a message with the the
     * exception to logger using warn level. Sends a message with system status,
     * statistics and complete context to log using trace level.
     *
     * @param throwable Exception that represents the failure. MAy be null if no
     * exception applies.
     * @return reference to the meter itself.
     */
    public Meter fail(Throwable throwable) {
        try {
            assert createTime != 0;
            if (stopTime != 0) {
                /* Log exception to provide stacktrace to inconsistent meter call. */
                logger.error(Slf4JMarkers.INCONSISTENT_FAIL, "Meter already refused or confirmed: " + this.eventCategory + ":" + this.eventPosition, new Exception("Meter.fail(): stopTime != 0"));
            }
            stopTime = System.nanoTime();
            if (startTime == 0) {
                /* Log exception to provide stacktrace to inconsistent meter call. */
                logger.error(Slf4JMarkers.INCONSISTENT_FAIL, "Meter refused, but not started: " + this.eventCategory + ":" + this.eventPosition, new Exception("Meter.fail(): startTime == 0"));
            }
            if (throwable != null) {
                exceptionClass = throwable.getClass().getName();
                exceptionMessage = throwable.getLocalizedMessage();
            }
            success = false;

            Thread currentThread = Thread.currentThread();
            this.threadStopId = currentThread.getId();
            this.threadStopName = currentThread.getName();

            if (logger.isWarnEnabled()) {
                collectSystemStatus();
                logger.warn(Slf4JMarkers.MSG_FAIL, readableString(new StringBuilder()).toString());
            }
            if (logger.isTraceEnabled()) {
                logger.trace(Slf4JMarkers.DATA_FAIL, write(new StringBuilder(), 'M').toString());
            }
        } catch (Exception t) {
            /* Prevent bugs from disrupting the application. Log exception to provide stacktrace to bug. */
            logger.error(Slf4JMarkers.BUG, "Meter refusal threw exception: " + this.eventCategory + ":" + this.eventPosition, t);
        }
        return this;
    }

    // ========================================================================
    /**
     * Checks if meters the meter has been forgotten to be confirmed or refused.
     * Useful to track those meters that do not follow the start(), ok()/fail()
     * idiom for all execution flows
     */
    @Override
    protected void finalize() throws Throwable {
        if (stopTime == 0) {
            /* Log exception to provide stacktrace to inconsistent meter call. */
            logger.error(Slf4JMarkers.INCONSISTENT_FINALIZED, "Meter finalized but not refused nor confirmed: " + this.eventCategory + ":" + this.eventPosition, new Exception("Meter.finalize(): stopTime == 0"));
        }
        super.finalize();
    }

    // ========================================================================
    /**
     * Compliance with {@link Closeable}. Assumes failure and refuses the meter
     * if the meter has not yet been marked as confirmed.
     */
    @Override
    public void close() {
        if (stopTime == 0) {
            fail(null);
        }
    }
}
