/**
 * Copyright 2011-2013 The Kuali Foundation
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
package org.kuali.maven.plugins.graph.pojo;

import org.apache.maven.shared.dependency.tree.DependencyNode;

public enum State {
    INCLUDED(DependencyNode.INCLUDED) //
    , DUPLICATE(DependencyNode.OMITTED_FOR_DUPLICATE) //
    , CYCLIC(DependencyNode.OMITTED_FOR_CYCLE) //
    , CONFLICT(DependencyNode.OMITTED_FOR_CONFLICT)//
    , UNKNOWN(-1);

    private State(int mavenIntValue) {
        this.mavenIntValue = mavenIntValue;
    }

    private final int mavenIntValue;

    public int getMavenIntValue() {
        return mavenIntValue;
    }

    public static final State getState(int mavenIntValue) {
        State[] states = values();
        for (State state : states) {
            if (mavenIntValue == state.getMavenIntValue()) {
                return state;
            }
        }
        return null;
    }

    public String getValue() {
        return name().toLowerCase();
    }

    @Override
    public String toString() {
        return getValue();
    }
}
