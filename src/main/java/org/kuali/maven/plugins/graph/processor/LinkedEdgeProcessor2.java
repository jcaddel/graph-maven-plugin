package org.kuali.maven.plugins.graph.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.kuali.maven.plugins.graph.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkedEdgeProcessor2 implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(LinkedEdgeProcessor2.class);
    int removeCount = 0;

    @Override
    public void process(Node<MavenContext> node) {
        // processEdges(node);
        // logger.info("removed " + removeCount + " redundant edges");
    }

    public void processEdges(Node<MavenContext> node) {
        for (Node<MavenContext> child : node.getChildren()) {
            handleState(child);
            processEdges(child);
        }
    }

    protected void handleEdges(Node<MavenContext> node) {
        List<Edge> edges = node.getObject().getGraphNode().getEdges();
        if (Helper.isEmpty(edges)) {
            return;
        }
        List<Node<MavenContext>> edgeChildren = getEdgeTreeList(node);
        Iterator<Edge> itr = edges.iterator();
        while (itr.hasNext()) {
            Edge edge = itr.next();
            if (contains(edge, edgeChildren)) {
                itr.remove();
            }
        }
    }

    protected boolean contains(Edge edge, List<Node<MavenContext>> nodes) {
        GraphNode gn1 = edge.getChild();
        for (Node<MavenContext> node : nodes) {
            MavenContext context = node.getObject();
            GraphNode gn2 = context.getGraphNode();
            if (gn1.getId() == gn2.getId()) {
                return true;
            }
        }
        return false;
    }

    protected List<Node<MavenContext>> getEdgeTreeList(Node<MavenContext> node) {
        List<Node<MavenContext>> edgeTreeList = new ArrayList<Node<MavenContext>>();
        List<Edge> edges = node.getObject().getGraphNode().getEdges();
        if (Helper.isEmpty(edges)) {
            return edgeTreeList;
        }
        for (Edge edge : edges) {
            GraphNode graphNodeChild = edge.getChild();
            Node<MavenContext> mavenChild = findNode(node, graphNodeChild.getId());
            edgeTreeList.add(mavenChild);
            edgeTreeList.addAll(getEdgeTreeList(mavenChild));
        }
        return edgeTreeList;
    }

    protected void handleState(Node<MavenContext> node) {
        switch (node.getObject().getState()) {
        case INCLUDED:
        case CYCLIC:
        case UNKNOWN:
        case CONFLICT:
            return;
        case DUPLICATE:
            handleDuplicate(node);
            return;
        default:
            throw new IllegalStateException("Unknown state " + node.getObject().getState());
        }
    }

    protected void handleDuplicate(Node<MavenContext> node) {
        MavenContext context = node.getObject();
        logger.info("handling node {}", context.getId());

        // Find the node that replaced us
        String id = context.getArtifactIdentifier();
        Node<MavenContext> replacement = TreeHelper.findRequiredIncludedNode(node.getRoot(), id);

        // Cleanup edges
        removeRedundantEdges(node.getParent(), replacement);
    }

    protected void removeRedundantEdges(Node<MavenContext> parent, Node<MavenContext> child) {
        // parent=43 child=51
        logger.info("parent={} child={}", parent.getObject().getId(), child.getObject().getId());
        // find any nodes where their recursive list of edges contains an edge with a child graph node of 43
        // any nodes like this, that also have an edge who's child graph node is 51 can have the edge pointing to graph
        // node 51 removed

        // find any nodes with an edge who's child graph node is 2
        // any nodes like this, that also have an edge who's child graph node is 21 can have the edge pointing to graph
        // node 21 removed
        int childGraphNodeId = parent.getObject().getGraphNode().getId();
        int redundantChildGraphNodeId = child.getObject().getGraphNode().getId();
        logger.debug("cgni={} rcgni={}", childGraphNodeId, redundantChildGraphNodeId);
        List<Node<MavenContext>> list = parent.getRoot().getBreadthFirstList();
        for (Node<MavenContext> element : list) {
            if (element.getObject().getGraphNode().getId() == parent.getObject().getGraphNode().getId()) {
                continue;
            }
            boolean hasRedundantEdge = hasRedundantEdge2(element, childGraphNodeId, redundantChildGraphNodeId);
            if (hasRedundantEdge) {
                int fromId = element.getObject().getGraphNode().getId();
                logger.info("removing edge from {} to {}", fromId, redundantChildGraphNodeId);
                removeEdgesRunningToThisGraphNode(element, redundantChildGraphNodeId);
                removeCount++;
            }
        }
    }

    protected List<Edge> getEdges(Node<MavenContext> node) {
        List<Edge> edges = new ArrayList<Edge>();
        MavenContext context = node.getObject();
        GraphNode graphNode = context.getGraphNode();
        if (Helper.isEmpty(graphNode.getEdges())) {
            return new ArrayList<Edge>();
        } else {
            edges.addAll(graphNode.getEdges());
            for (Edge edge : graphNode.getEdges()) {
                GraphNode child = edge.getChild();
                int graphNodeId = child.getId();
                Node<MavenContext> childNode = findNode(node, graphNodeId);
                edges.addAll(getEdges(childNode));
            }
        }
        return edges;
    }

    protected boolean hasRedundantEdge2(Node<MavenContext> node, int childGraphNodeId, int redundantChildGraphNodeId) {
        List<Edge> edges = getEdges(node);
        logger.debug("node {} edges.size()={}", node.getObject().getId(), edges.size());
        if (Helper.isEmpty(edges)) {
            return false;
        }
        boolean hasEdgeToRealNode = hasEdgeRunningToThisGraphNode(node, childGraphNodeId);
        boolean hasEdgeToRedundantNode = hasEdgeRunningToThisGraphNode(node, redundantChildGraphNodeId);
        logger.info("node {} real=" + hasEdgeToRealNode + " redundant=" + hasEdgeToRedundantNode, node.getObject()
                .getId());
        return hasEdgeToRedundantNode;
    }

    protected boolean hasRedundantEdge(Node<MavenContext> node, int childGraphNodeId, int redundantChildGraphNodeId) {
        List<Edge> edges = node.getObject().getGraphNode().getEdges();
        if (Helper.isEmpty(edges)) {
            return false;
        }
        boolean hasEdgeToRealNode = hasEdgeRunningToThisGraphNode(node, childGraphNodeId);
        boolean hasEdgeToRedundantNode = hasEdgeRunningToThisGraphNode(node, redundantChildGraphNodeId);
        return hasEdgeToRealNode && hasEdgeToRedundantNode;
    }

    protected boolean hasEdgeRunningToThisGraphNode(Node<MavenContext> node, int graphNodeId) {
        List<Edge> edges = node.getObject().getGraphNode().getEdges();
        if (Helper.isEmpty(edges)) {
            return false;
        }
        for (Edge edge : edges) {
            GraphNode childGraphNode = edge.getChild();
            if (childGraphNode.getId() == graphNodeId) {
                return true;
            }
        }
        return false;
    }

    protected void removeEdgesRunningToThisGraphNode(Node<MavenContext> node, int childGraphNodeId) {
        List<Edge> edges = node.getObject().getGraphNode().getEdges();
        if (Helper.isEmpty(edges)) {
            return;
        }
        Iterator<Edge> itr = edges.iterator();
        while (itr.hasNext()) {
            Edge edge = itr.next();
            GraphNode child = edge.getChild();
            if (child.getId() == childGraphNodeId) {
                itr.remove();
            }
        }
    }

    protected Node<MavenContext> findNode(Node<MavenContext> node, int graphNodeId) {
        List<Node<MavenContext>> list = node.getRoot().getBreadthFirstList();
        for (Node<MavenContext> element : list) {
            MavenContext context = element.getObject();
            GraphNode graphNode = context.getGraphNode();
            if (graphNode.getId() == graphNodeId) {
                return element;
            }
        }
        throw new IllegalStateException("Can't find a node for graph node " + graphNodeId);
    }

}
