package org.kuali.maven.plugins.graph.dot.edge;

import java.util.Collections;
import java.util.List;

import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.Scope;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.pojo.Style;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;

public class FlatEdgeHandler implements EdgeHandler {
    TreeHelper helper = new TreeHelper();

    @Override
    public List<Edge> getEdges(Node<MavenContext> node) {
        // Return a single edge running from parent to child
        return Collections.singletonList(getEdge(node));
    }

    protected Edge getEdge(Node<MavenContext> node) {
        GraphNode parent = node.getParent().getObject().getGraphNode();
        MavenContext context = node.getObject();
        DependencyNode dn = context.getDependencyNode();
        GraphNode child = context.getGraphNode();
        boolean optional = dn.getArtifact().isOptional();
        State state = context.getState();
        Scope scope = Scope.getScope(dn.getArtifact().getScope());
        return getEdge(parent, child, optional, scope, state);
    }

    protected Edge getEdge(GraphNode parent, GraphNode child, boolean optional, Scope scope, State state) {
        Style style = helper.getStyle(scope, optional, state);
        String label = TreeHelper.getRelationshipLabel(scope, optional, state);
        Edge edge = new Edge(parent, child);
        helper.copyStyleProperties(edge, style);
        edge.setLabel(label);
        return edge;
    }
}
