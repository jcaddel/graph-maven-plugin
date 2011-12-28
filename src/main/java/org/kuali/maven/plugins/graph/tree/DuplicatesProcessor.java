package org.kuali.maven.plugins.graph.tree;

import java.util.ArrayList;
import java.util.List;

import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphContext;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DuplicatesProcessor {
    private static final Logger logger = LoggerFactory.getLogger(DuplicatesProcessor.class);

    public void process(GraphContext context, Node<MavenContext> node, List<Edge> edges, List<GraphNode> nodes) {
        List<MavenContext> duplicates = getContexts(node, State.DUPLICATE);
        List<MavenContext> included = getContexts(node, State.INCLUDED);
        logger.debug("duplicates size=" + duplicates.size());
        for (MavenContext duplicate : duplicates) {
            MavenContext replacement = getReplacement(duplicate, included);
            replacement.getGraphNode().setHidden(false);
            duplicate.getGraphNode().setHidden(true);
        }
    }

    protected MavenContext getReplacement(MavenContext duplicate, List<MavenContext> included) {
        String id1 = duplicate.getArtifactIdentifier();
        for (MavenContext includedContext : included) {
            String id2 = includedContext.getArtifactIdentifier();
            if (id1.equals(id2)) {
                return includedContext;
            }
        }
        throw new IllegalStateException("Inconsistent tree state.  No included node for duplicate dependency: " + id1);
    }

    protected List<MavenContext> getContexts(Node<MavenContext> node, State state) {
        List<Node<MavenContext>> nodeList = node.getBreadthFirstList();
        List<MavenContext> contexts = new ArrayList<MavenContext>();
        for (Node<MavenContext> nodeElement : nodeList) {
            MavenContext context = nodeElement.getObject();
            if (nodeElement.isRoot()) {
                continue;
            }
            boolean hidden = nodeElement.getParent().getObject().getGraphNode().isHidden();
            boolean correctState = state == context.getState();
            if (!hidden && correctState) {
                contexts.add(context);
            }
        }
        return contexts;
    }
}
