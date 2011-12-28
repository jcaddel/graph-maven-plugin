package org.kuali.maven.plugins.graph.processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConflictsFlatProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(ConflictsFlatProcessor.class);
    TreeHelper helper = new TreeHelper();

    @Override
    public void process(Node<MavenContext> root) {
        List<Node<MavenContext>> conflicts = helper.getNodeList(root, State.CONFLICT);
        Set<String> conflictArtifactIds = new HashSet<String>();
        for (Node<MavenContext> conflict : conflicts) {
            GraphNode gn = conflict.getObject().getGraphNode();
            if (!gn.isHidden()) {
                String artifactId = TreeHelper.getPartialArtifactId(conflict.getObject().getReplacement());
                conflictArtifactIds.add(artifactId);
            }
        }
        logger.info("displayed conflicts size={}", conflictArtifactIds.size());
        for (String artifactId : conflictArtifactIds) {
            Node<MavenContext> included = findIncluded(root, artifactId);
            helper.showPath(included);
        }
    }

    protected Node<MavenContext> findIncluded(Node<MavenContext> root, String partialArtifactId) {
        List<Node<MavenContext>> included = helper.getNodeList(root, State.INCLUDED);
        for (Node<MavenContext> context : included) {
            Artifact a = context.getObject().getArtifact();
            String partialId = TreeHelper.getPartialArtifactId(a);
            if (partialId.equals(partialArtifactId)) {
                return context;
            }
        }
        throw new IllegalStateException("Invalid state");
    }

}
