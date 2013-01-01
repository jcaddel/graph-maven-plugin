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
