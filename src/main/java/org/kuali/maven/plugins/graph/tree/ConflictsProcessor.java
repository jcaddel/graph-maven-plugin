package org.kuali.maven.plugins.graph.tree;

import java.util.ArrayList;
import java.util.List;

import org.kuali.maven.plugins.graph.GraphContext;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphException;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;

public class ConflictsProcessor implements Processor {

    @Override
    public void process(GraphContext context, Node<MavenContext> node, List<GraphNode> nodes, List<Edge> edges) {
        TreeHelper helper = new TreeHelper();
        List<MavenContext> contexts = getContexts(node);
        List<Edge> conflictEdges = getEdges(contexts, edges);
        for (Edge conflictEdge : conflictEdges) {
            conflictEdge.getChild().setHidden(false);
        }
        List<Node<MavenContext>> treeNodes = getNodes(node, conflictEdges);
        for (Node<MavenContext> treeNode : treeNodes) {
            helper.showPath(treeNode);
        }
    }

    protected List<Node<MavenContext>> getNodes(Node<MavenContext> node, List<Edge> edges) {
        List<Node<MavenContext>> nodes = new ArrayList<Node<MavenContext>>();
        for (Edge edge : edges) {
            nodes.add(getNode(node, edge));
        }
        return nodes;
    }

    protected Node<MavenContext> getNode(Node<MavenContext> node, Edge edge) {
        List<Node<MavenContext>> nodes = node.getRoot().getBreadthFirstList();
        for (Node<MavenContext> elementNode : nodes) {
            GraphNode graphNode = elementNode.getObject().getGraphNode();
            int id = graphNode.getId();
            if (id == edge.getChild().getId()) {
                return elementNode;
            }
        }
        throw new GraphException("Inconsistent tree state.  Unable to locate node " + edge.getChild().getId());
    }

    protected List<Edge> getEdges(List<MavenContext> contexts, List<Edge> edges) {
        List<Edge> newEdges = new ArrayList<Edge>();
        for (MavenContext context : contexts) {
            newEdges.add(getEdge(context, edges));
        }
        return newEdges;
    }

    protected Edge getEdge(MavenContext context, List<Edge> edges) {
        for (Edge edge : edges) {
            GraphNode parent = edge.getParent();
            int parentId = parent.getId();
            if (parentId == context.getGraphNode().getId()) {
                return edge;
            }
        }
        throw new GraphException("Inconsistent tree state.  Unable to locate an edge for "
                + context.getArtifactIdentifier());
    }

    protected List<MavenContext> getContexts(Node<MavenContext> node) {
        List<Node<MavenContext>> nodeList = node.getBreadthFirstList();
        List<MavenContext> contexts = new ArrayList<MavenContext>();
        for (Node<MavenContext> nodeElement : nodeList) {
            MavenContext context = nodeElement.getObject();
            State state = context.getState();
            boolean hidden = context.getGraphNode().isHidden();
            if (!hidden && state == State.CONFLICT) {
                contexts.add(context);
            }
        }
        return contexts;
    }
}
