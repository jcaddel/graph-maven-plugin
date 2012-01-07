/**
 * Copyright 2011-2012 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
