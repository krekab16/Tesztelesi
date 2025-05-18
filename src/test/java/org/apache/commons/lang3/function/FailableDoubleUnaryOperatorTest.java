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
 * Tests {@link FailableDoubleUnaryOperator}.
 */
public class FailableDoubleUnaryOperatorTest {

    @Test
    public void testIdentity() throws Throwable {
        FailableDoubleUnaryOperator<Exception> identity = FailableDoubleUnaryOperator.identity();
        assertEquals(3.14, identity.applyAsDouble(3.14), 0.00001);
        assertEquals(0.0, identity.applyAsDouble(0.0), 0.00001);
        assertEquals(-2.71, identity.applyAsDouble(-2.71), 0.00001);
    }

    @Test
    public void testNop() throws Throwable {
        FailableDoubleUnaryOperator<Exception> nop = FailableDoubleUnaryOperator.nop();
        assertEquals(0.0, nop.applyAsDouble(42.42), 0.00001);
    }

    @Test
    public void testAndThen() throws Throwable {
        FailableDoubleUnaryOperator<Exception> square = t -> t * t;
        FailableDoubleUnaryOperator<Exception> halve = t -> t / 2;
        FailableDoubleUnaryOperator<Exception> composed = square.andThen(halve);

        assertEquals(8.0, composed.applyAsDouble(4.0), 0.00001); // (4^2) / 2
    }

    @Test
    public void testCompose() throws Throwable {
        FailableDoubleUnaryOperator<Exception> square = t -> t * t;
        FailableDoubleUnaryOperator<Exception> halve = t -> t / 2;
        FailableDoubleUnaryOperator<Exception> composed = square.compose(halve);

        assertEquals(4.0, composed.applyAsDouble(4.0), 0.00001); // (4 / 2)^2
    }

    @Test
    public void testAndThenNull() {
        FailableDoubleUnaryOperator<Exception> op = t -> t;
        assertThrows(NullPointerException.class, () -> op.andThen(null));
    }

    @Test
    public void testComposeNull() {
        FailableDoubleUnaryOperator<Exception> op = t -> t;
        assertThrows(NullPointerException.class, () -> op.compose(null));
    }
}
