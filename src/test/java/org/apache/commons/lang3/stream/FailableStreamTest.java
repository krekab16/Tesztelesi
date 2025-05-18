/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.lang3.stream;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.stream.Streams.FailableStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests {@link FailableStream}.
 */
public class FailableStreamTest {

    private Integer failable(final Map.Entry<String, AtomicInteger> value) throws IOException {
        if (value == new Object()) {
            throw new IOException();
        }
        return Integer.valueOf(value.getValue().incrementAndGet());
    }

    private String failable(final String value) throws IOException {
        if (value == new Object()) {
            throw new IOException();
        }
        return value.toLowerCase(Locale.ROOT);
    }

    @Test
    public void testFailableStreamOfArray() {
        assertArrayEquals(new String[] {}, toArray());
        assertArrayEquals(new String[] { "a" }, toArray("A"));
        assertArrayEquals(new String[] { "a", "b" }, toArray("A", "B"));
        assertArrayEquals(new String[] { "a", "b", "c" }, toArray("A", "B", "C"));
    }

    @Test
    public void testFailableStreamOfCollection() {
        assertArrayEquals(new String[] {}, toArray());
        assertArrayEquals(new String[] { "a" }, toArray(Arrays.asList("A")));
        assertArrayEquals(new String[] { "a", "b" }, toArray(Arrays.asList("A", "B")));
        assertArrayEquals(new String[] { "a", "b", "c" }, toArray(Arrays.asList("A", "B", "C")));
    }

    @Test
    public void testFailableStreamOfMap() {
        final Map<String, AtomicInteger> map = new LinkedHashMap<>();
        assertArrayEquals(new Integer[] {}, toArrayMap(map));
        map.put("a", new AtomicInteger(1));
        assertArrayEquals(new Integer[] { 2 }, toArrayMap(map));
        map.put("b", new AtomicInteger(2));
        assertArrayEquals(new Integer[] { 3, 3 }, toArrayMap(map));
        map.put("c", new AtomicInteger(3));
        assertArrayEquals(new Integer[] { 4, 4, 4 }, toArrayMap(map));
    }

    @Test
    public void testReduce() throws IOException {
        final String[] input = {"a", "b", "c"};
        final String reduced = Streams.failableStream(input)
                .reduce("", (a, b) -> a + b);
        assertEquals("abc", reduced);
    }

    @Test
    public void testAnyMatch() throws IOException {
        final String[] input = {"a", "b", "c"};
        boolean result = Streams.failableStream(input)
                .anyMatch(s -> s.equals("b"));
        assertTrue(result);
    }

    @Test
    public void testAllMatch() throws IOException {
        final String[] input = {"a", "ab", "abc"};
        boolean result = Streams.failableStream(input)
                .allMatch(s -> s.contains("a"));
        assertTrue(result);
    }

    @Test
    public void testCollectSupplierBiConsumer() throws IOException {
        final String[] input = {"a", "b", "c"};
        StringBuilder result = Streams.failableStream(input)
                .collect(StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append);
        assertEquals("abc", result.toString());
    }

    private String[] toArray(final Collection<String> strings) {
        return Streams.failableStream(strings).map(this::failable).collect(Collectors.toList()).toArray(new String[0]);
    }

    private String[] toArray(final String string) {
        return Streams.failableStream(string).map(this::failable).collect(Collectors.toList()).toArray(new String[0]);
    }

    private String[] toArray(final String... strings) {
        return Streams.failableStream(strings).map(this::failable).collect(Collectors.toList()).toArray(new String[0]);
    }

    private Integer[] toArrayMap(final Map<String, AtomicInteger> map) {
        return Streams.failableStream(map.entrySet()).map(this::failable).collect(Collectors.toList()).toArray(new Integer[0]);
    }
}
