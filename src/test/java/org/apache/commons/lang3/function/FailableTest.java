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

package org.apache.commons.lang3.function;

import org.apache.commons.lang3.stream.Streams;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FailableTest {

    @Test
    void testStreamFromCollection() {
        List<String> data = Arrays.asList("a", "b");
        Streams.FailableStream<String> failableStream = Failable.stream(data);
        assertNotNull(failableStream);
    }

    @Test
    void testStreamFromStream() {
        Stream<String> stream = Stream.of("x", "y");
        Streams.FailableStream<String> failableStream = Failable.stream(stream);
        assertNotNull(failableStream);
    }

    @Test
    void testGetAsShort() {
        short result = Failable.getAsShort(() -> (short) 42);
        assertEquals(42, result);
    }

    @Test
    void testGetAsShortThrows() {
        assertThrows(UncheckedIOException.class, () -> Failable.getAsShort(() -> {
            throw new IOException("fail");
        }));
    }

    @Test
    void testGetAsInt() {
        int result = Failable.getAsInt(() -> 100);
        assertEquals(100, result);
    }

    @Test
    void testGetAsIntThrows() {
        assertThrows(UncheckedIOException.class, () -> Failable.getAsInt(() -> {
            throw new IOException("fail");
        }));
    }

    @Test
    void testGetAsLong() {
        long result = Failable.getAsLong(() -> 123456789L);
        assertEquals(123456789L, result);
    }

    @Test
    void testGetAsLongThrows() {
        assertThrows(UncheckedIOException.class, () -> Failable.getAsLong(() -> {
            throw new IOException("fail");
        }));
    }

    @Test
    void testGetAsDouble() {
        double result = Failable.getAsDouble(() -> 3.14);
        assertEquals(3.14, result);
    }

    @Test
    void testGetAsDoubleThrows() {
        assertThrows(UncheckedIOException.class, () -> Failable.getAsDouble(() -> {
            throw new IOException("fail");
        }));
    }

    @Test
    void testGetAsBoolean() {
        boolean result = Failable.getAsBoolean(() -> true);
        assertTrue(result);
    }

    @Test
    void testGetAsBooleanThrows() {
        assertThrows(UncheckedIOException.class, () -> Failable.getAsBoolean(() -> {
            throw new IOException("fail");
        }));
    }

    @Test
    void testGet() {
        String result = Failable.get(() -> "test");
        assertEquals("test", result);
    }

    @Test
    void testGetThrows() {
        assertThrows(UncheckedIOException.class, () -> Failable.get(() -> {
            throw new IOException("fail");
        }));
    }

    @Test
    void testRun() {
        Failable.run(() -> {
            // no-op
        });
    }

    @Test
    void testRunThrows() {
        assertThrows(UncheckedIOException.class, () -> Failable.run(() -> {
            throw new IOException("fail");
        }));
    }

    @Test
    void testTryWithResourcesNoErrorHandler() {
        Failable.tryWithResources(() -> {}, () -> {});
    }

    @Test
    void testTryWithResourcesWithErrorHandler() {
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        Failable.tryWithResources(() -> {
            throw new Exception("test");
        }, errorRef::set, () -> {});
        assertEquals("test", errorRef.get().getMessage());
    }

    @Test
    void testTryWithResourcesThrowsUnchecked() {
        assertThrows(UncheckedIOException.class, () ->
                Failable.tryWithResources(() -> {
                    throw new IOException("fail");
                }, () -> {})
        );
    }

    @Test
    void testTryWithResourcesErrorInResourceCleanup() {
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        Failable.tryWithResources(() -> {}, errorRef::set, () -> {
            throw new Exception("close fail");
        });
        assertEquals("close fail", errorRef.get().getMessage());
    }
}
