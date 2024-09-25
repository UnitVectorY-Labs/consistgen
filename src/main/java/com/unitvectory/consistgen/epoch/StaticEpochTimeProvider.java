/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.unitvectory.consistgen.epoch;

import lombok.Builder;

/**
 * Provides static epoch time.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class StaticEpochTimeProvider implements EpochTimeProvider {

    /**
     * The epoch time in milliseconds.
     */
    private final long epochTimeMilliseconds;

    /**
     * Creates a new StaticEpochTimeProvider.
     * 
     * @param epochTimeMilliseconds the epoch time in milliseconds
     * @param epochTimeSeconds      the epoch time in seconds
     */
    @Builder
    public StaticEpochTimeProvider(Long epochTimeMilliseconds, Long epochTimeSeconds) {
        if (epochTimeMilliseconds == null && epochTimeSeconds == null) {
            this.epochTimeMilliseconds = 0;
        } else if (epochTimeMilliseconds != null) {
            this.epochTimeMilliseconds = epochTimeMilliseconds;
        } else {
            this.epochTimeMilliseconds = epochTimeSeconds * 1000;
        }
    }

    @Override
    public long epochTimeMilliseconds() {
        return this.epochTimeMilliseconds;
    }
}