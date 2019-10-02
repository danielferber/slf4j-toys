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

import org.usefultoys.slf4j.SessionConfig;
import org.usefultoys.slf4j.internal.EventData;
import org.usefultoys.slf4j.internal.EventReader;
import org.usefultoys.slf4j.internal.EventWriter;
import org.usefultoys.slf4j.internal.SystemData;
import org.usefultoys.slf4j.utils.UnitFormatter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Augments the {@link SystemData} with start, stop, failure and flow information measured by Meter.
 *
 * @author Daniel Felix Ferber
 */
public class MeterData extends SystemData {

    private static final long serialVersionUID = 2L;
    public static final char DETAILED_MESSAGE_PREFIX = 'M';

    public MeterData() {
    }

    /**
     * Identifier that categorizes events.
     */
    protected String eventCategory = null;
    /**
     * Identifier of the event.
     */
    protected String eventName = null;
    /**
     * Full ID of the parent event.
     */
    protected String eventParent = null;
    /**
     * An arbitrary short, human readable message to describe the task being measured.
     */
    protected String description = null;
    /**
     * For successful execution, the string token that identifies the execution pathId.
     */
    protected String pathId;
    /**
     * For rejected execution, the string token that identifies the rejection cause.
     */
    protected String rejectId;
    /**
     * When the job was created/scheduled.
     */
    protected long createTime = 0;
    /**
     * When the job started execution.
     */
    protected long startTime = 0;
    /**
     * When the job finished execution.
     */
    protected long stopTime = 0;
    /**
     * How many iterations were run by the operation.
     */
    protected long iteration = 0;
    /**
     * How many iteration were expected by the operation.
     */
    protected long expectedIterations = 0;
    /**
     * For failure result, the exception class that caused the failure.
     */
    protected String failClass = null;
    /**
     * For failure result, the exception message that caused the failure.
     */
    protected String failMessage = null;
    /**
     * Time considered reasonable for successful execution.
     */
    protected long timeLimitNanoseconds = 0;

    /**
     * Additional meta data describing the job.
     */
    protected Map<String, String> context;

    public String getFullID() {
        if (eventName == null) {
            return eventCategory + '/' + eventPosition;
        }
        return eventCategory + '/' + eventName + '#' + eventPosition;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventParent() {
        return eventParent;
    }

    public String getDescription() {
        return description;
    }

    public String getPathId() {
        return pathId;
    }

    public String getRejectId() {
        return rejectId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public long getCurrentIteration() {
        return iteration;
    }

    public long getExpectedIterations() {
        return expectedIterations;
    }

    public String getExceptionClass() {
        return failClass;
    }

    public String getExceptionMessage() {
        return failMessage;
    }

    public boolean isStarted() {
        return startTime != 0;
    }

    public boolean isStopped() {
        return stopTime != 0;
    }

    public boolean isOK() {
        return (stopTime != 0) && (failClass == null && rejectId == null);
    }

    public boolean isReject() {
        return (stopTime != 0) && (rejectId != null);
    }

    public boolean isFail() {
        return (stopTime != 0) && (failClass != null);
    }

    public long getTimeLimitNanoseconds() {
        return timeLimitNanoseconds;
    }

    public Map<String, String> getContext() {
        if (context == null) {
            return null;
        }
        return Collections.unmodifiableMap(context);
    }

    @Override
    protected void resetImpl() {
        super.resetImpl();
        this.eventCategory = null;
        this.eventName = null;
        this.eventParent = null;
        this.description = null;
        this.pathId = null;
        this.rejectId = null;
        this.createTime = 0;
        this.startTime = 0;
        this.stopTime = 0;
        this.iteration = 0;
        this.expectedIterations = 0;
        this.failClass = null;
        this.failMessage = null;
        this.timeLimitNanoseconds = 0;
        this.context = null;
    }

    @Override
    protected boolean isCompletelyEqualsImpl(final EventData obj) {
        final MeterData other = (MeterData) obj;
        if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
            return false;
        }
        if ((this.eventCategory == null) ? (other.eventCategory != null) : !this.eventCategory.equals(other.eventCategory)) {
            return false;
        }
        if ((this.eventName == null) ? (other.eventName != null) : !this.eventName.equals(other.eventName)) {
            return false;
        }
        if ((this.eventParent == null) ? (other.eventParent != null) : !this.eventParent.equals(other.eventParent)) {
            return false;
        }
        if ((this.pathId == null) ? (other.pathId != null) : !this.pathId.equals(other.pathId)) {
            return false;
        }
        if ((this.rejectId == null) ? (other.rejectId != null) : !this.rejectId.equals(other.rejectId)) {
            return false;
        }
        if (this.createTime != other.createTime) {
            return false;
        }
        if (this.startTime != other.startTime) {
            return false;
        }
        if (this.stopTime != other.stopTime) {
            return false;
        }
        if (this.iteration != other.iteration) {
            return false;
        }
        if (this.expectedIterations != other.expectedIterations) {
            return false;
        }
        if ((this.failClass == null) ? (other.failClass != null) : !this.failClass.equals(other.failClass)) {
            return false;
        }
        if ((this.failMessage == null) ? (other.failMessage != null) : !this.failMessage.equals(other.failMessage)) {
            return false;
        }
        if (this.timeLimitNanoseconds != other.timeLimitNanoseconds) {
            return false;
        }
        if (this.context != other.context && (this.context == null || !this.context.equals(other.context))) {
            return false;
        }
        return super.isCompletelyEqualsImpl(obj);
    }

    @Override
    protected void collectRuntimeStatus() {
        super.collectRuntimeStatus();
    }

    @Override
    public StringBuilder readableString(final StringBuilder builder) {
        if (MeterConfig.printStatus) {
            if (stopTime != 0) {
                if (failClass == null && rejectId == null) {
                    if (timeLimitNanoseconds != 0 && startTime != 0 && stopTime - startTime > timeLimitNanoseconds) {
                        builder.append("OK (Slow)");
                    } else {
                        builder.append("OK");
                    }
                } else if (rejectId != null) {
                    builder.append("REJECT");
                } else if (failClass != null) {
                    builder.append("FAIL");
                }
            } else if (startTime != 0 && iteration == 0) {
                builder.append("STARTED");
            } else if (startTime != 0) {
                builder.append("PROGRESS");
            } else {
                builder.append("SCHEDULED");
            }
            builder.append(": ");
        }

        if (MeterConfig.printCategory) {
            final int index = eventCategory.lastIndexOf('.') + 1;
            builder.append(eventCategory.substring(index));
        }
        if (eventName != null) {
            if (MeterConfig.printCategory) {
                builder.append('/');
            }
            builder.append(eventName);
        }
        if (MeterConfig.printPosition) {
            builder.append('#');
            builder.append(eventPosition);
        }

        if (pathId != null) {
            builder.append(" [");
            builder.append(pathId);
            builder.append(']');
        }
        if (rejectId != null) {
            builder.append(" [");
            builder.append(rejectId);
            builder.append(']');
        }
        if (failClass != null || failMessage != null) {
            builder.append(" [");
            if (failClass != null) {
                builder.append(failClass);
            }
            if (failClass != null && failMessage != null) {
                builder.append("; ");
            }
            if (failMessage != null) {
                builder.append(failMessage);
            }
            builder.append(']');
        }

        if (startTime != 0 && iteration > 0) {
            builder.append(' ');
            builder.append(UnitFormatter.iterations(this.iteration));
            if (this.expectedIterations > 0) {
                builder.append('/');
                builder.append(UnitFormatter.iterations(this.expectedIterations));
            }
        }

        if (this.description != null) {
            builder.append(" '");
            builder.append(this.description);
            builder.append('\'');
        }

        if (context != null) {
            for (final Entry<String, String> entry : context.entrySet()) {
                builder.append("; ");
                builder.append(entry.getKey());
                if (entry.getValue() != null) {
                    builder.append("=");
                    builder.append(entry.getValue());
                }
            }
        }

        if (this.startTime != 0) {
            long executionTime = getExecutionTime();
            if (executionTime > MeterConfig.progressPeriodMilliseconds) {
                builder.append("; ");
                builder.append(UnitFormatter.nanoseconds(executionTime));
            }
            if (this.iteration > 0) {
                builder.append("; ");
                final double iterationsPerSecond = getIterationsPerSecond();
                builder.append(UnitFormatter.iterationsPerSecond(iterationsPerSecond));
                builder.append(' ');
                final double nanoSecondsPerIteration = 1.0F / iterationsPerSecond * 1000000000;
                builder.append(UnitFormatter.nanoseconds(nanoSecondsPerIteration));
            }
        } else {
            builder.append("; ");
            builder.append(UnitFormatter.nanoseconds(getWaitingTime()));
        }

        if (MeterConfig.printMemory && this.runtime_usedMemory > 0) {
            builder.append("; ");
            builder.append(UnitFormatter.bytes(this.runtime_usedMemory));
        }
        if (MeterConfig.printLoad && this.systemLoad > 0) {
            builder.append("; ");
            builder.append(Math.round(this.systemLoad * 100));
            builder.append("%");
        }
        if (SessionConfig.uuidSize != 0 && this.sessionUuid != null) {
            builder.append("; ");
            builder.append(this.sessionUuid.substring(SessionConfig.UUID_LENGHT - SessionConfig.uuidSize));
        }

        return builder;
    }

    public boolean isSlow() {
        return timeLimitNanoseconds != 0 && stopTime != 0 && startTime != 0 && stopTime - startTime > timeLimitNanoseconds;
    }

    public long getExecutionTime() {
        if (startTime == 0) {
            return 0;
        } else if (stopTime == 0) {
            return System.nanoTime() - startTime;
        }
        return stopTime - startTime;
    }

    public long getWaitingTime() {
        if (startTime == 0) {
            return System.nanoTime() - createTime;
        }
        return startTime - createTime;
    }

    public double getIterationsPerSecond() {
        if (iteration == 0 || startTime == 0) {
            return 0.0d;
        }
        final float executionTimeNS = getExecutionTime();
        if (executionTimeNS == 0) {
            return 0.0d;
        }
        return ((double) this.iteration) / executionTimeNS * 1000000000;
    }

    public static final String PROP_DESCRIPTION = "d";
    public static final String PROP_PATH_ID = "p";
    public static final String PROP_REJECT_ID = "r";
    public static final String PROP_FAIL_ID = "f";
    public static final String PROP_CREATE_TIME = "t0";
    public static final String PROP_START_TIME = "t1";
    public static final String PROP_STOP_TIME = "t2";
    public static final String PROP_ITERATION = "i";
    public static final String PROP_EXPECTED_ITERATION = "ei";
    public static final String PROP_LIMIT_TIME = "tl";
    public static final String PROP_CONTEXT = "ctx";
    public static final String EVENT_CATEGORY = "c";
    public static final String EVENT_NAME = "n";
    public static final String EVENT_PARENT = "ep";

    @Override
    public void writePropertiesImpl(final EventWriter w) {
        if (this.description != null) {
            w.property(PROP_DESCRIPTION, this.description);
        }
        if (this.rejectId != null) {
            w.property(PROP_REJECT_ID, this.rejectId);
        }
        if (this.pathId != null) {
            w.property(PROP_PATH_ID, this.pathId);
        }
        if (this.failClass != null) {
            w.property(PROP_FAIL_ID, this.failClass, this.failMessage != null ? this.failMessage : "");
        }
        if (this.eventCategory != null) {
            w.property(EVENT_CATEGORY, this.eventCategory);
        }
        if (this.eventName != null) {
            w.property(EVENT_NAME, this.eventName);
        }
        if (this.eventParent != null) {
            w.property(EVENT_PARENT, this.eventParent);
        }

        /* Create, start, stop time. */
        if (this.createTime != 0) {
            w.property(PROP_CREATE_TIME, this.createTime);
        }
        if (this.startTime != 0) {
            w.property(PROP_START_TIME, this.startTime);
        }
        if (this.stopTime != 0) {
            w.property(PROP_STOP_TIME, this.stopTime);
        }
        if (this.iteration != 0) {
            w.property(PROP_ITERATION, this.iteration);
        }
        if (this.expectedIterations != 0) {
            w.property(PROP_EXPECTED_ITERATION, this.expectedIterations);
        }
        if (this.timeLimitNanoseconds != 0) {
            w.property(PROP_LIMIT_TIME, timeLimitNanoseconds);
        }

        /* Context */
        if (this.context != null && !this.context.isEmpty()) {
            w.property(PROP_CONTEXT, this.context);
        }

        super.writePropertiesImpl(w);
    }

    @Override
    protected boolean readPropertyImpl(final EventReader r, final String propertyName) throws IOException {
        if (PROP_DESCRIPTION.equals(propertyName)) {
            this.description = r.readString();
            return true;
        } else if (EVENT_CATEGORY.equals(propertyName)) {
            this.eventCategory = r.readString();
            return true;
        } else if (EVENT_NAME.equals(propertyName)) {
            this.eventName = r.readString();
            return true;
        } else if (EVENT_PARENT.equals(propertyName)) {
            this.eventParent = r.readString();
            return true;
        } else if (PROP_FAIL_ID.equals(propertyName)) {
            this.failClass = r.readString();
            this.failMessage = r.readString();
            return true;
        } else if (PROP_REJECT_ID.equals(propertyName)) {
            this.rejectId = r.readString();
            return true;
        } else if (PROP_PATH_ID.equals(propertyName)) {
            this.pathId = r.readString();
            return true;
        } else if (PROP_CREATE_TIME.equals(propertyName)) {
            this.createTime = r.readLong();
            return true;
        } else if (PROP_START_TIME.equals(propertyName)) {
            this.startTime = r.readLong();
            return true;
        } else if (PROP_STOP_TIME.equals(propertyName)) {
            this.stopTime = r.readLong();
            return true;
        } else if (PROP_ITERATION.equals(propertyName)) {
            this.iteration = r.readLong();
            return true;
        } else if (PROP_EXPECTED_ITERATION.equals(propertyName)) {
            this.expectedIterations = r.readLong();
            return true;
        } else if (PROP_LIMIT_TIME.equals(propertyName)) {
            this.timeLimitNanoseconds = r.readLong();
            return true;
        } else if (PROP_CONTEXT.equals(propertyName)) {
            this.context = r.readMap();
            return true;
        }
        return super.readPropertyImpl(r, propertyName);
    }

    public final boolean read(final String message) {
        return this.read(message, DETAILED_MESSAGE_PREFIX);
    }

    public final String write() {
        return write(new StringBuilder(), DETAILED_MESSAGE_PREFIX).toString();
    }

    public final String readableWrite() {
        return readableString(new StringBuilder()).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeterData meterData = (MeterData) o;

        if (eventCategory != null ? !eventCategory.equals(meterData.eventCategory) : meterData.eventCategory != null)
            return false;
        if (sessionUuid != null ? !sessionUuid.equals(meterData.sessionUuid) : meterData.sessionUuid != null)
            return false;
        if (eventPosition != meterData.eventPosition) return false;
        return eventName != null ? eventName.equals(meterData.eventName) : meterData.eventName == null;
    }

    @Override
    public int hashCode() {
        int result = eventCategory != null ? eventCategory.hashCode() : 0;
        result = 31 * result + (eventName != null ? eventName.hashCode() : 0);
        result = 31 * result + (int) (eventPosition ^ (eventPosition >>> 32));
        result = 31 * result + (sessionUuid != null ? sessionUuid.hashCode() : 0);
        return result;
    }
}
