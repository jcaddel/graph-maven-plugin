package org.kuali.maven.plugins.graph.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkedEdgeProcessor3 implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(LinkedEdgeProcessor3.class);

    @Override
    public void process(Node<MavenContext> node) {
        processEdges(node);
    }

    public void processEdges(Node<MavenContext> node) {
        handleEdges(node);
        for (Node<MavenContext> child : node.getChildren()) {
            processEdges(child);
        }
    }

    protected void handleEdges(Node<MavenContext> node) {
        MavenContext context = node.getObject();
        GraphNode graphNode = context.getGraphNode();
        List<Edge> edges = graphNode.getEdges();
        if (Helper.isEmpty(edges)) {
            return;
        }
        List<Edge> edgesToRemove = new ArrayList<Edge>();
        for (Edge edge : edges) {
            List<Edge> subList = getEdgesThatAreNotMe(edge, edges);
            GraphNode child = edge.getChild();
            boolean isReachable = isReachable(child, subList, node);
            if (isReachable) {
                edgesToRemove.add(edge);
                logger.debug("reached graph node id {}", child.getId());
            }
        }
        Iterator<Edge> itr = edges.iterator();
        while (itr.hasNext()) {
            Edge edge = itr.next();
            boolean contains = contains(edgesToRemove, edge);
            if (contains) {
                itr.remove();
                logger.info("removing edge {}", edge.getId());
            }
        }
    }

    protected boolean contains(List<Edge> edges, Edge edge) {
        for (Edge element : edges) {
            if (edge.getId() == element.getId()) {
                return true;
            }
        }
        return false;
    }

    protected boolean isReachable(GraphNode graphNode, List<Edge> edges, Node<MavenContext> node) {
        List<Node<MavenContext>> nodeList = getRecursiveNodeList(node, edges);
        for (Node<MavenContext> element : nodeList) {
            MavenContext context = element.getObject();
            GraphNode gn = context.getGraphNode();
            if (gn.getId() == graphNode.getId()) {
                return true;
            }
        }
        return false;
    }

    protected List<Node<MavenContext>> getRecursiveNodeList(Node<MavenContext> node, List<Edge> edges) {
        if (Helper.isEmpty(edges)) {
            return new ArrayList<Node<MavenContext>>();
        }
        List<Node<MavenContext>> recursiveList = new ArrayList<Node<MavenContext>>();
        for (Edge edge : edges) {
            GraphNode graphNode = edge.getChild();
            Node<MavenContext> foundNode = findNode(node, graphNode.getId());
            recursiveList.add(foundNode);
            recursiveList.addAll(getRecursiveNodeList(node, foundNode.getObject().getGraphNode().getEdges()));
        }
        return recursiveList;
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
        throw new IllegalStateException("Can't locate a node for graph node id " + graphNodeId);
    }

    protected List<Edge> getEdgesThatAreNotMe(Edge me, List<Edge> edges) {
        List<Edge> newEdges = new ArrayList<Edge>();
        for (Edge edge : edges) {
            if (edge.getId() != me.getId()) {
                newEdges.add(edge);
            }
        }
        return newEdges;
    }
}
