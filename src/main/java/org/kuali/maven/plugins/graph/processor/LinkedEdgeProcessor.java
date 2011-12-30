package org.kuali.maven.plugins.graph.processor;

import org.kuali.maven.plugins.graph.dot.EdgeGenerator;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.Scope;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkedEdgeProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(LinkedEdgeProcessor.class);
    public static final String REPLACEMENT_LABEL = "replacement";
    EdgeGenerator generator = new EdgeGenerator();

    @Override
    public void process(Node<MavenContext> node) {
        for (Node<MavenContext> child : node.getChildren()) {
            // Create an edge running from the child's parent to itself
            Edge edge = generator.getParentChildEdge(child);
            // Add this edge to the parent's list
            generator.addEdge(child.getParent(), edge);
            // Might need to create another edge
            handleState(child);
            // Continue processing the tree
            process(child);
        }
    }

    protected void handleState(Node<MavenContext> node) {
        logger.debug("handling node {}", node.getObject().getId());
        switch (node.getObject().getState()) {
        case INCLUDED:
        case CYCLIC:
        case UNKNOWN:
            // Nothing further to do. Styling draws attention to CYCLIC and UNKNOWN nodes
            return;
        case DUPLICATE:
            // Draw a line from our parent to the included node that contains the same artifact
            handleDuplicate(node);
            return;
        case CONFLICT:
            // Draw a line from us to the included node containing the replacement artifact
            handleConflict(node);
            return;
        default:
            throw new IllegalStateException("Unknown state " + node.getObject().getState());
        }
    }

    protected void handleConflict(Node<MavenContext> node) {
        MavenContext context = node.getObject();

        // This is the id of the artifact Maven is actually going to use
        // The validator's and sanitizer's guarantee that the replacement artifact will always be
        // found among the included nodes.
        String replacementId = TreeHelper.getArtifactId(context.getReplacement());

        // Get the node containing the replacement artifact
        Node<MavenContext> replacement = TreeHelper.findRequiredIncludedNode(node.getRoot(), replacementId);

        // Extract the correct parent/child graph nodes
        GraphNode parent = context.getGraphNode();
        GraphNode child = replacement.getObject().getGraphNode();

        // Draw an edge saying "replacement" from ourself to the artifact that replaced us
        boolean optional = context.isOptional();
        Edge edge = generator.getStyledEdge(parent, child, optional, Scope.DEFAULT_SCOPE, State.CONFLICT);
        edge.setLabel(REPLACEMENT_LABEL);

        // Add this edge to our list
        generator.addEdge(node, edge);

    }

    protected void handleDuplicate(Node<MavenContext> node) {
        MavenContext context = node.getObject();

        // Find the node that replaces us
        String id = context.getArtifactIdentifier();
        Node<MavenContext> replacement = TreeHelper.findRequiredIncludedNode(node.getRoot(), id);

        // This is our parent in the tree
        GraphNode parent = node.getParent().getObject().getGraphNode();

        // This is the node that is being used instead of us
        GraphNode child = replacement.getObject().getGraphNode();

        // Use our optional/scope settings
        boolean optional = context.isOptional();
        Scope scope = Scope.getScope(context.getArtifact().getScope());

        // Draw an edge from our parent to the node that replaced us
        Edge edge = generator.getStyledEdge(parent, child, optional, scope, State.INCLUDED);

        // Add this edge to our parents list
        generator.addEdge(node.getParent(), edge);
    }

}
