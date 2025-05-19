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

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BinaryOperator;

import static org.junit.jupiter.api.Assertions.*;

class EnumerationSpliteratorTest {

    @Test
    void testTryAdvance() {
        Vector<String> vector = new Vector<>();
        vector.add("one");
        vector.add("two");

        Enumeration<String> enumeration = vector.elements();
        Spliterator<String> spliterator = Streams.enumerationSpliterator(vector.size(), 0, enumeration);

        StringBuilder result = new StringBuilder();
        while (spliterator.tryAdvance(result::append)) {}

        assertEquals("onetwo", result.toString());
    }

    @Test
    void testForEachRemaining() {
        Vector<String> vector = new Vector<>();
        vector.add("A");
        vector.add("B");

        Enumeration<String> enumeration = vector.elements();
        Spliterator<String> spliterator = Streams.enumerationSpliterator(vector.size(), 0, enumeration);

        StringBuilder result = new StringBuilder();
        spliterator.forEachRemaining(result::append);

        assertEquals("AB", result.toString());
    }

    @Test
    void testTryAdvanceOnEmptyEnumeration() {
        Vector<String> vector = new Vector<>();

        Enumeration<String> enumeration = vector.elements();
        Spliterator<String> spliterator = Streams.enumerationSpliterator(vector.size(), 0, enumeration);

        boolean advanced = spliterator.tryAdvance(s -> fail("Should not be called"));
        assertFalse(advanced);
    }
}
