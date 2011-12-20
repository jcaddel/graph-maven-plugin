package org.kuali.maven.plugins.graph.pojo;

import org.apache.maven.artifact.Artifact;

public enum Scope {

    COMPILE(Artifact.SCOPE_COMPILE) //
    , TEST(Artifact.SCOPE_TEST) //
    , RUNTIME(Artifact.SCOPE_RUNTIME) //
    , PROVIDED(Artifact.SCOPE_PROVIDED) //
    , SYSTEM(Artifact.SCOPE_SYSTEM) //
    , IMPORT(Artifact.SCOPE_IMPORT);

    private Scope(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }

    public static final Scope DEFAULT_SCOPE = COMPILE;

    public static final Scope getScope(String value) {
        Scope[] scopes = values();
        for (Scope scope : scopes) {
            if (scope.getValue().equals(value)) {
                return scope;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
