/**
 * Copyright 2010-2011 The Kuali Foundation
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

public class MavenContext {
    int id;
    String artifactIdentifier;
    String partialArtifactIdentifier;
    State state;
    GraphNode graphNode;
    DependencyNode dependencyNode;
    MavenContext replacement;

    public MavenContext() {
        this(null, null);
    }

    public MavenContext(GraphNode graphNode, DependencyNode dependencyNode) {
        super();
        this.graphNode = graphNode;
        this.dependencyNode = dependencyNode;
    }

    public GraphNode getGraphNode() {
        return graphNode;
    }

    public void setGraphNode(GraphNode graphNode) {
        this.graphNode = graphNode;
    }

    public DependencyNode getDependencyNode() {
        return dependencyNode;
    }

    public void setDependencyNode(DependencyNode dependencyNode) {
        this.dependencyNode = dependencyNode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MavenContext other = (MavenContext) obj;
        return id == other.id;
    }

    public String getArtifactIdentifier() {
        return artifactIdentifier;
    }

    public void setArtifactIdentifier(String artifactId) {
        this.artifactIdentifier = artifactId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public MavenContext getReplacement() {
        return replacement;
    }

    public void setReplacement(MavenContext replacement) {
        this.replacement = replacement;
    }

    public String getPartialArtifactIdentifier() {
        return partialArtifactIdentifier;
    }

    public void setPartialArtifactIdentifier(String partialArtifactIdentifier) {
        this.partialArtifactIdentifier = partialArtifactIdentifier;
    }

}
