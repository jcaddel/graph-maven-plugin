package org.kuali.maven.plugins.graph.filter;

import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DependencyNodeFilterWrapper implements NodeFilter<MavenContext> {
    private static final Logger logger = LoggerFactory.getLogger(DependencyNodeFilterWrapper.class);

    Filter<DependencyNode> filter;

    public DependencyNodeFilterWrapper() {
        this(null);
    }

    public DependencyNodeFilterWrapper(Filter<DependencyNode> filter) {
        super();
        this.filter = filter;
    }

    @Override
    public boolean isMatch(Node<MavenContext> node) {
        MavenContext context = node.getObject();
        DependencyNode dependencyNode = context.getDependencyNode();
        boolean match = filter.isMatch(dependencyNode);
        logger.debug("match={} for {}", match, dependencyNode.getArtifact());
        return match;
    }

    public Filter<DependencyNode> getFilter() {
        return filter;
    }

    public void setFilter(Filter<DependencyNode> filter) {
        this.filter = filter;
    }

}
