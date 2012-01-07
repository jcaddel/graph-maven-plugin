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

/**
 * Remove redundant edges from the graph. For the purposes of this processor, an edge is considered redundant if it
 * points to a dependency that is included by a downstream dependency. If your pom declares a dependency on
 * commons-logging-1.1.1 and your pom also declares a dependency on commons-beanutils-1.8.3 (which declares a dependency
 * on commons-logging-1.1.1), the graph will not display a line from your pom to commons-logging.
 *
 * @author jeffcaddel
 */
public class ReduceClutterProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(ReduceClutterProcessor.class);
    // Track how many edges we remove
    int removeCount = 0;

    /**
     * Process the tree
     */
    @Override
    public void process(Node<MavenContext> node) {
        recurse(node);
        logger.debug("removed {} redundant edges", removeCount);
    }

    /**
     * Recursively traverse the tree invoking <code>handleEdges</code> on each node
     *
     * @param node
     */
    protected void recurse(Node<MavenContext> node) {
        handleEdges(node);
        for (Node<MavenContext> child : node.getChildren()) {
            recurse(child);
        }
    }

    /**
     * Extract the list of edges stored at this node, determine if any of them should be removed, and then remove them
     *
     * @param node
     */
    protected void handleEdges(Node<MavenContext> node) {
        List<Edge> edges = Helper.toEmpty(node.getObject().getGraphNode().getEdges());
        List<Edge> edgesToRemove = getEdgesToRemove(edges, node);
        remove(edges, edgesToRemove);
    }

    /**
     * Accumulate a list of edges that are "redundant" as defined in the javadoc for this processor
     *
     * @param edges
     * @param node
     * @return
     */
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

    /**
     * Get a list of the edges attached to this node (besides the one we are currently examining). Traverse the tree's
     * pointed to by those edges. If we find the same GraphNode that the edge we are working with points to, remove our
     * edge, since it is "redundant"
     *
     * @param edge
     * @param edges
     * @param node
     * @return
     */
    protected boolean isRedundantEdge(Edge edge, List<Edge> edges, Node<MavenContext> node) {
        List<Edge> subList = getEdgesThatAreNotMe(edge, edges);
        GraphNode child = edge.getChild();
        return isReachable(child, subList, node);
    }

    /**
     * Remove edges as needed, tracking the overall count of edges we remove
     *
     * @param edges
     * @param edgesToRemove
     */
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

    /**
     * Return true if the list contains this edge.
     *
     * TODO Override equals and hashcode so we don't need this
     *
     * @param edges
     * @param edge
     * @return
     */
    protected boolean contains(List<Edge> edges, Edge edge) {
        for (Edge element : edges) {
            if (edge.getId() == element.getId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true if we can reach <code>graphNode</code> by traversing the trees pointed to by <code>edges</code>
     *
     * @param graphNode
     * @param edges
     * @param node
     * @return
     */
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

    /**
     * Return true if we need to recurse the edges stored at this node. More specifically, return true only if the node
     * is not hidden, is not a <code>CONFLICT</code> node, and is not already in the list of nodes we have found.
     *
     * @param node
     * @param list
     * @return
     */
    protected boolean isInclude(Node<MavenContext> node, List<Node<MavenContext>> list, Edge edge) {
        boolean hidden = edge.getParent().isHidden() || edge.getChild().isHidden();
        State state = node.getObject().getState();
        return !hidden && state != State.CONFLICT && !contains(list, node);
    }

    /**
     * Recurse the trees pointed to by <code>edges</code> and generate a list of <code>nodes</code> that are reachable
     * by traversing those trees.
     *
     * @param node
     * @param edges
     * @param list
     */
    protected void recursivelyFillNodeList(Node<MavenContext> node, List<Edge> edges, List<Node<MavenContext>> list) {
        edges = Helper.toEmpty(edges);
        for (Edge edge : edges) {
            GraphNode graphNode = edge.getChild();
            Node<MavenContext> foundNode = findRequiredNode(node, graphNode.getId());
            boolean recurse = isInclude(foundNode, list, edge);
            if (recurse) {
                list.add(foundNode);
                List<Edge> foundEdges = foundNode.getObject().getGraphNode().getEdges();
                recursivelyFillNodeList(node, foundEdges, list);
            }
        }
    }

    /**
     * Return true if the list contains the node, false otherwise
     *
     * @param list
     * @param node
     * @return
     */
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

    /**
     * Find the node in the tree that contains this graphNode. The graphNode must be located or an IllegalStateException
     * is thrown.
     *
     * @param node
     * @param graphNodeId
     * @return
     */
    protected Node<MavenContext> findRequiredNode(Node<MavenContext> node, int graphNodeId) {
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

    /**
     * Return a subset of the list passed in where <code>me</code> is not included in the returned list.
     *
     * @param me
     * @param edges
     * @return
     */
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
