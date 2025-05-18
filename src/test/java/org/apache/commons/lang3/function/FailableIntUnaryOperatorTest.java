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

import org.junit.jupiter.api.Test;

/**
 * Tests {@link FailableIntUnaryOperator}.
 */
public class FailableIntUnaryOperatorTest {

    @Test
    public void testIdentity() throws Throwable {
        FailableIntUnaryOperator<Exception> identity = FailableIntUnaryOperator.identity();
        assertEquals(42, identity.applyAsInt(42));
    }

    @Test
    public void testNop() throws Throwable {
        FailableIntUnaryOperator<Exception> nop = FailableIntUnaryOperator.nop();
        assertEquals(0, nop.applyAsInt(123));
        assertEquals(0, nop.applyAsInt(-999));
    }

    @Test
    public void testAndThen() throws Throwable {
        FailableIntUnaryOperator<Exception> addOne = t -> t + 1;
        FailableIntUnaryOperator<Exception> timesTwo = t -> t * 2;

        FailableIntUnaryOperator<Exception> combined = addOne.andThen(timesTwo);
        assertEquals(10, combined.applyAsInt(4)); // (4 + 1) * 2 = 10
    }

    @Test
    public void testCompose() throws Throwable {
        FailableIntUnaryOperator<Exception> addOne = t -> t + 1;
        FailableIntUnaryOperator<Exception> timesTwo = t -> t * 2;

        FailableIntUnaryOperator<Exception> composed = addOne.compose(timesTwo);
        assertEquals(9, composed.applyAsInt(4)); // (4 * 2) + 1 = 9
    }
}