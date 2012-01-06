package org.kuali.maven.plugins.graph.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReduceClutterProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(ReduceClutterProcessor.class);
    int removeCount = 0;

    @Override
    public void process(Node<MavenContext> node) {
        recurse(node);
        logger.debug("removed {} redundant edges", removeCount);
    }

    public void recurse(Node<MavenContext> node) {
        handleEdges(node);
        for (Node<MavenContext> child : node.getChildren()) {
            recurse(child);
        }
    }

    protected void handleEdges(Node<MavenContext> node) {
        List<Edge> edges = getEdges(node);
        List<Edge> edgesToRemove = getEdgesToRemove(edges, node);
        remove(edges, edgesToRemove);
    }

    protected List<Edge> getEdges(Node<MavenContext> node) {
        List<Edge> edges = node.getObject().getGraphNode().getEdges();
        if (edges == null) {
            return new ArrayList<Edge>();
        } else {
            return edges;
        }
    }

    protected List<Edge> getEdgesToRemove(List<Edge> edges, Node<MavenContext> node) {
        List<Edge> edgesToRemove = new ArrayList<Edge>();
        for (Edge edge : edges) {
            boolean redundant = isRedundantEdge(edge, edges, node);
            if (redundant) {
                edgesToRemove.add(edge);
            }
        }
        return edgesToRemove;
    }

    protected boolean isRedundantEdge(Edge edge, List<Edge> edges, Node<MavenContext> node) {
        List<Edge> subList = getEdgesThatAreNotMe(edge, edges);
        GraphNode child = edge.getChild();
        return isReachable(child, subList, node);
    }

    protected void remove(List<Edge> edges, List<Edge> edgesToRemove) {
        Iterator<Edge> itr = edges.iterator();
        while (itr.hasNext()) {
            Edge edge = itr.next();
            boolean contains = contains(edgesToRemove, edge);
            if (contains) {
                itr.remove();
                logger.debug("removing edge {}", edge.getId());
                removeCount++;
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
        List<Node<MavenContext>> list = new ArrayList<Node<MavenContext>>();
        recursivelyFillNodeList(node, edges, list);
        logger.debug("nodeList.size()={}", list.size());
        for (Node<MavenContext> element : list) {
            MavenContext context = element.getObject();
            GraphNode gn = context.getGraphNode();
            if (gn.getId() == graphNode.getId()) {
                return true;
            }
        }
        return false;
    }

    protected void recursivelyFillNodeList(Node<MavenContext> node, List<Edge> edges, List<Node<MavenContext>> list) {
        edges = Helper.toEmpty(edges);
        for (Edge edge : edges) {
            GraphNode graphNode = edge.getChild();
            Node<MavenContext> foundNode = findNode(node, graphNode.getId());
            State state = foundNode.getObject().getState();
            if (state == State.CONFLICT) {
                continue;
            }
            if (!contains(list, foundNode)) {
                list.add(foundNode);
                List<Edge> foundEdges = foundNode.getObject().getGraphNode().getEdges();
                recursivelyFillNodeList(node, foundEdges, list);
            }
        }
    }

    protected boolean contains(List<Node<MavenContext>> list, Node<MavenContext> node) {
        int targetId = node.getObject().getId();
        for (Node<MavenContext> element : list) {
            int id = element.getObject().getId();
            if (id == targetId) {
                return true;
            }
        }
        return false;
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
