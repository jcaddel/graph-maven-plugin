/**
 * Copyright 2011-2012 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.maven.plugins.graph.util;

import org.springframework.util.Assert;

/**
 * <p>
 * Thread safe counter.
 * </p>
 */
public class Counter implements Comparable<Counter> {

    private int value;

    public Counter() {
        this(0);
    }

    public Counter(int value) {
        super();
        this.value = value;
    }

    public synchronized int increment() {
        Assert.state(value < Integer.MAX_VALUE, "Maximum counter value exceeded");
        return value++;
    }

    public synchronized int decrement() {
        Assert.state(value > Integer.MIN_VALUE, "Minimum counter value exceeded");
        return value--;
    }

    public synchronized int getValue() {
        return value;
    }

    @Override
    public synchronized int compareTo(Counter other) {
        return value > other.value ? 1 : value < other.value ? -1 : 0;
    }

}
