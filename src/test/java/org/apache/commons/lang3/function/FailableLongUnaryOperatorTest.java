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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link FailableLongUnaryOperator}.
 */
public class FailableLongUnaryOperatorTest {

    @Test
    public void testIdentity() throws Throwable {
        FailableLongUnaryOperator<Exception> identity = FailableLongUnaryOperator.identity();
        assertEquals(123L, identity.applyAsLong(123L));
        assertEquals(0L, identity.applyAsLong(0L));
        assertEquals(-456L, identity.applyAsLong(-456L));
    }

    @Test
    public void testNop() throws Throwable {
        FailableLongUnaryOperator<Exception> nop = FailableLongUnaryOperator.nop();
        assertEquals(0L, nop.applyAsLong(999L));
    }

    @Test
    public void testAndThen() throws Throwable {
        FailableLongUnaryOperator<Exception> doubleIt = t -> t * 2;
        FailableLongUnaryOperator<Exception> addTen = t -> t + 10;
        FailableLongUnaryOperator<Exception> composed = doubleIt.andThen(addTen);

        assertEquals(20L, composed.applyAsLong(5L)); // (5 * 2) + 10
    }

    @Test
    public void testCompose() throws Throwable {
        FailableLongUnaryOperator<Exception> doubleIt = t -> t * 2;
        FailableLongUnaryOperator<Exception> addTen = t -> t + 10;
        FailableLongUnaryOperator<Exception> composed = doubleIt.compose(addTen);

        assertEquals(30L, composed.applyAsLong(5L)); // (5 + 10) * 2
    }

    @Test
    public void testAndThenNull() {
        FailableLongUnaryOperator<Exception> op = t -> t;
        assertThrows(NullPointerException.class, () -> op.andThen(null));
    }

    @Test
    public void testComposeNull() {
        FailableLongUnaryOperator<Exception> op = t -> t;
        assertThrows(NullPointerException.class, () -> op.compose(null));
    }
}
