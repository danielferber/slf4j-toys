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
package br.com.danielferber.slf4jtoys.slf4j.profiler.watcher;

import br.com.danielferber.slf4jtoys.slf4j.profiler.status.SystemStatusData;

/**
 *
 * @author Daniel Felix Ferber
 */
public class WatcherEvent extends SystemStatusData {

    /**
     * Unique ID of session that is collecting system status data.
     */
    protected String uuid;
    /**
     * How many times this watcher collection system status data since its creation.
     */
    protected long counter = 0;
    /**
     * When system this status data was collected.
     */
    protected long time = 0;

    @Override
    public void reset() {
        super.reset();
        this.uuid = null;
        this.counter = 0;
        this.time = 0;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (counter ^ (counter >>> 32));
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        WatcherEvent other = (WatcherEvent) obj;
        if (counter != other.counter) {
            return false;
        }
        if (uuid == null) {
            return false;
        } else if (!uuid.equals(other.uuid)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.uuid + ":" + this.counter;
    }
}
