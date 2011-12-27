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

public class ConflictsProcessor2 implements PostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ConflictsProcessor2.class);

    @Override
    public void process(GraphContext context, Node<MavenContext> node, List<Edge> edges, List<GraphNode> nodes) {
        List<MavenContext> conflicts = getContexts(node, State.CONFLICT);
        List<MavenContext> included = getContexts(node, State.INCLUDED);
        logger.debug("conflicts size={}", conflicts.size());
        for (MavenContext conflict : conflicts) {
            String id1 = TreeHelper.getArtifactId(conflict.getArtifact());
            String id2 = TreeHelper.getArtifactId(conflict.getReplacement());
            MavenContext conflictNode = findDisplayedConflictNode(node, id1);
            MavenContext replacementNode = getReplacement(id2, included);
            conflictNode.getGraphNode().setHidden(false);
            replacementNode.getGraphNode().setHidden(false);
            boolean conflictNodeIsUs = conflict.getId() == conflictNode.getId();
            if (!conflictNodeIsUs) {
                conflict.getGraphNode().setHidden(true);
            }
        }
    }

    protected MavenContext getReplacement(String id1, List<MavenContext> included) {
        for (MavenContext includedContext : included) {
            String id2 = includedContext.getArtifactIdentifier();
            if (id1.equals(id2)) {
                return includedContext;
            }
        }
        throw new IllegalStateException("Inconsistent tree state.  No included node for duplicate dependency: " + id1);
    }

    protected MavenContext findDisplayedConflictNode(Node<MavenContext> root, String id) {
        List<Node<MavenContext>> nodeList = root.getBreadthFirstList();
        for (Node<MavenContext> nodeElement : nodeList) {
            MavenContext context = nodeElement.getObject();
            if (nodeElement.isRoot()) {
                continue;
            }
            boolean hasEdges = !Helper.isEmpty(context.getEdges());
            boolean correctState = context.getState() == State.CONFLICT;
            if (hasEdges && correctState) {
                return nodeElement.getObject();
            }
        }
        throw new IllegalStateException("Invalid state");
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
