package org.kuali.maven.plugins.graph.filter;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArtifactFilterWrapper implements NodeFilter<MavenContext> {
    private static final Logger logger = LoggerFactory.getLogger(ArtifactFilterWrapper.class);

    Filter<Artifact> filter;

    public ArtifactFilterWrapper() {
        this(null);
    }

    public ArtifactFilterWrapper(Filter<Artifact> filter) {
        super();
        this.filter = filter;
    }

    @Override
    public boolean isMatch(Node<MavenContext> node) {
        MavenContext context = node.getObject();
        DependencyNode dependencyNode = context.getDependencyNode();
        Artifact artifact = dependencyNode.getArtifact();
        logger.debug("examining {}", artifact);
        return filter.isMatch(artifact);
    }

    public Filter<Artifact> getFilter() {
        return filter;
    }

    public void setFilter(Filter<Artifact> filter) {
        this.filter = filter;
    }

}
