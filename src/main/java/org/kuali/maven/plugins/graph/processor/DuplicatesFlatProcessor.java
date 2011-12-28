package org.kuali.maven.plugins.graph.processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DuplicatesFlatProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(DuplicatesFlatProcessor.class);
    TreeHelper helper = new TreeHelper();

    @Override
    public void process(Node<MavenContext> root) {
        List<Node<MavenContext>> duplicates = helper.getNodeList(root, State.DUPLICATE);
        Set<String> duplicateArtifactIds = new HashSet<String>();
        for (Node<MavenContext> duplicate : duplicates) {
            GraphNode gn = duplicate.getObject().getGraphNode();
            if (!gn.isHidden()) {
                duplicateArtifactIds.add(duplicate.getObject().getArtifactIdentifier());
            }
        }
        logger.info("displayed duplicates size={}", duplicateArtifactIds.size());
        for (String artifactId : duplicateArtifactIds) {
            Node<MavenContext> included = findIncluded(root, artifactId);
            helper.showPath(included);
        }
    }

    protected Node<MavenContext> findIncluded(Node<MavenContext> root, String artifactId) {
        List<Node<MavenContext>> included = helper.getNodeList(root, State.INCLUDED);
        for (Node<MavenContext> context : included) {
            String id = context.getObject().getArtifactIdentifier();
            if (id.equals(artifactId)) {
                return context;
            }
        }
        throw new IllegalStateException("Invalid state");
    }

}
