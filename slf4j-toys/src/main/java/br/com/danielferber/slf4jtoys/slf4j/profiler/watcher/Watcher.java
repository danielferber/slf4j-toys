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
package br.com.danielferber.slf4jtoys.slf4j.profiler.watcher;

import br.com.danielferber.slf4jtoys.slf4j.profiler.internal.LoggerMessageWriter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.internal.Session;
import br.com.danielferber.slf4jtoys.slf4j.profiler.internal.Syntax;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class Watcher extends WatcherEvent {
    public static final Marker WATCHER_MARKER = MarkerFactory.getMarker("WATCHER");
    private final Logger logger;
    private final WatcherTask watcherTask;
    private final LoggerMessageWriter writer = new LoggerMessageWriter(new Syntax());

    public class WatcherTask extends TimerTask {

        @Override
        public void run() {
            Watcher.this.collectData();

            if (logger.isInfoEnabled()) {
                final StringBuilder buffer = new StringBuilder();
                WatcherEvent.readableString(Watcher.this, buffer);
                logger.info(buffer.toString());
            }
            if (logger.isTraceEnabled()) {
                final StringBuilder buffer = new StringBuilder();
                writeToString(Watcher.this.writer, Watcher.this, buffer);
                logger.trace(Watcher.WATCHER_MARKER, buffer.toString());
            }
        }
    }

    protected Watcher(final Logger logger) {
        super();
        this.logger = logger;
        this.uuid = Session.uuid;
        this.watcherTask = new WatcherTask();
    }

    public Watcher start() {
        logger.info("Watcher started. uuid={}", uuid);
        try {
            Session.timer.scheduleAtFixedRate(watcherTask, 1000, 1000);
        } catch (IllegalStateException e) {
            /* WatcherTask já estava programada. */
        }
        return this;
    }

    public Watcher stop() {
        watcherTask.cancel();
        logger.info("Watcher stopped. uuid={}", uuid);
        return this;
    }

}
