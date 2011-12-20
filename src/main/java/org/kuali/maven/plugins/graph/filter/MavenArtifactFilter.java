package org.kuali.maven.plugins.graph.filter;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;

/**
 * Implementation of Maven's ArtifactFilter that uses a <code>Filter<Artifact></code>
 */
public class MavenArtifactFilter implements ArtifactFilter {
    Filter<Artifact> filter;

    public MavenArtifactFilter() {
        this(null);
    }

    public MavenArtifactFilter(Filter<Artifact> filter) {
        super();
        this.filter = filter;
    }

    @Override
    public boolean include(Artifact artifact) {
        return filter.isMatch(artifact);
    }

    public Filter<Artifact> getFilter() {
        return filter;
    }

    public void setFilter(Filter<Artifact> filter) {
        this.filter = filter;
    }

}
