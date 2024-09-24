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
package com.unitvectory.consistgen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Test the StaticEpochTimeProvider class.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
class StaticEpochTimeProviderTest {

    @Test
    public void testDefaultConstructor() {
        StaticEpochTimeProvider provider = StaticEpochTimeProvider.builder().build();
        assertEquals(0, provider.epochTimeMilliseconds());
    }

    @Test
    public void testConstructorWithMilliseconds() {
        StaticEpochTimeProvider provider = StaticEpochTimeProvider.builder()
                .epochTimeMilliseconds(123456789L)
                .build();
        assertEquals(123456789L, provider.epochTimeMilliseconds());
    }

    @Test
    public void testConstructorWithSeconds() {
        StaticEpochTimeProvider provider = StaticEpochTimeProvider.builder()
                .epochTimeSeconds(123456L)
                .build();
        assertEquals(123456000L, provider.epochTimeMilliseconds());
    }

    @Test
    public void testConstructorWithBothValues() {
        // When both milliseconds and seconds are provided always use milliseconds
        StaticEpochTimeProvider provider = StaticEpochTimeProvider.builder()
                .epochTimeMilliseconds(123456789L)
                .epochTimeSeconds(123456L)
                .build();
        assertEquals(123456789L, provider.epochTimeMilliseconds());
    }
}