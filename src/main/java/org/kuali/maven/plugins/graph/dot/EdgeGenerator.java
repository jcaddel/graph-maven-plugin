/**
 * Copyright 2011-2013 The Kuali Foundation
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
package org.kuali.maven.plugins.graph.dot;

import java.util.ArrayList;

import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphDescriptor;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.Scope;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.pojo.Style;
import org.kuali.maven.plugins.graph.processor.StyleProcessor;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.kuali.maven.plugins.graph.util.Counter;

/**
 * <p>
 * Draw one parent->child edge for each node in the graph.
 * </p>
 */
public class EdgeGenerator {
    StyleProcessor sp = new StyleProcessor();
    Counter counter = new Counter(1);
    Boolean showEdgeLabels;

    public EdgeGenerator(GraphDescriptor gd) {
        this.showEdgeLabels = gd.getShowEdgeLabels();
    }

    /**
     * <p>
     * Add this edge to the GraphNode contained in this node. If there are no edges on the GraphNode yet, create a
     * <code>List<Edge></code> and store it on the GraphNode
     * </p>
     */
    public void addEdge(Node<MavenContext> node, Edge edge) {
        MavenContext context = node.getObject();
        GraphNode graphNode = context.getGraphNode();
        if (graphNode.getEdges() == null) {
            graphNode.setEdges(new ArrayList<Edge>());
        }
        graphNode.getEdges().add(edge);
    }

    /**
     * <p>
     * Create an edge running from this node's parent to itself
     * </p>
     */
    public Edge getParentChildEdge(Node<MavenContext> node) {
        GraphNode parent = node.getParent().getObject().getGraphNode();
        MavenContext context = node.getObject();
        GraphNode child = context.getGraphNode();
        boolean optional = context.isOptional();
        State state = context.getState();
        Scope scope = Scope.getScope(context.getArtifact().getScope());
        return getStyledEdge(parent, child, optional, scope, state);
    }

    /**
     * <p>
     * Create an edge running from this node's parent to the replacement node
     * </p>
     */
    public Edge getParentChildEdge(Node<MavenContext> node, Node<MavenContext> replacement, State state) {
        GraphNode parent = node.getParent().getObject().getGraphNode();
        MavenContext context = node.getObject();
        GraphNode child = replacement.getObject().getGraphNode();
        boolean optional = context.isOptional();
        Scope scope = Scope.getScope(context.getArtifact().getScope());
        return getStyledEdge(parent, child, optional, scope, state);
    }

    /**
     * <p>
     * Create an edge running from this node's parent to the replacement node
     * </p>
     */
    public Edge getParentChildEdge(Node<MavenContext> node, Node<MavenContext> replacement) {
        return getParentChildEdge(node, replacement, node.getObject().getState());
    }

    /**
     * <p>
     * Create an edge between the indicated parent and child using styling for the indicated scope, state, and optional
     * settings
     * </p>
     */
    public Edge getStyledEdge(GraphNode parent, GraphNode child, boolean optional, Scope scope, State state) {
        Style style = sp.getStyle(scope, optional, state);
        int id = counter.increment();
        String label = TreeHelper.getRelationshipLabel(scope, optional, state);
        Edge edge = new Edge(parent, child);
        edge.setId(id);
        sp.copyStyleProperties(edge, style);

        if (!Boolean.FALSE.equals(showEdgeLabels)) {
            edge.setLabel(label);
        }

        return edge;
    }
}
