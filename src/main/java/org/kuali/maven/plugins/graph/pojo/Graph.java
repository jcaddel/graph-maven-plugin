package org.kuali.maven.plugins.graph.pojo;

import java.util.List;


public class Graph {
    GraphDecorator graphDecorator = new GraphDecorator();
    NodeDecorator nodeDecorator = new NodeDecorator();
    EdgeDecorator edgeDecorator = new EdgeDecorator();
    List<GraphNode> nodes;
    List<Edge> edges;

    public GraphDecorator getGraphDecorator() {
        return graphDecorator;
    }

    public void setGraphDecorator(GraphDecorator graphDecorator) {
        this.graphDecorator = graphDecorator;
    }

    public NodeDecorator getNodeDecorator() {
        return nodeDecorator;
    }

    public void setNodeDecorator(NodeDecorator nodeDecorator) {
        this.nodeDecorator = nodeDecorator;
    }

    public EdgeDecorator getEdgeDecorator() {
        return edgeDecorator;
    }

    public void setEdgeDecorator(EdgeDecorator edgeDecorator) {
        this.edgeDecorator = edgeDecorator;
    }

    public List<GraphNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<GraphNode> nodes) {
        this.nodes = nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

}
