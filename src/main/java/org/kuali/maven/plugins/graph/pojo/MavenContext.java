package org.kuali.maven.plugins.graph.pojo;

import org.apache.maven.shared.dependency.tree.DependencyNode;

public class MavenContext {
    int id;
    String artifactIdentifier;
    State state;
    GraphNode graphNode;
    DependencyNode dependencyNode;

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

}
