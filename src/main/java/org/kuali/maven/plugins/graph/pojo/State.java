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
