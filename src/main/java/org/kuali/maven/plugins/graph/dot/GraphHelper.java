package org.kuali.maven.plugins.graph.dot;

import java.util.ArrayList;
import java.util.List;

import org.kuali.maven.plugins.graph.pojo.Direction;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.Graph;
import org.kuali.maven.plugins.graph.pojo.GraphDecorator;
import org.kuali.maven.plugins.graph.pojo.GraphNode;

public class GraphHelper {

    public Graph getGraph(String title) {
        return getGraph(title, Direction.DEFAULT_DIRECTION, new ArrayList<GraphNode>(), new ArrayList<Edge>());
    }

    public Graph getGraph(String title, List<GraphNode> nodes) {
        return getGraph(title, Direction.DEFAULT_DIRECTION, nodes, new ArrayList<Edge>());
    }

    public Graph getGraph(String title, Direction direction, List<GraphNode> nodes, List<Edge> edges) {
        GraphDecorator decorator = new GraphDecorator();
        decorator.setRankdir(direction.name());
        decorator.setLabel(title);
        Graph graph = new Graph();
        graph.setGraphDecorator(decorator);
        graph.setNodes(nodes);
        graph.setEdges(edges);
        return graph;
    }

}
