/**
 * Copyright 2010-2011 The Kuali Foundation
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
import java.util.Collections;
import java.util.List;

import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.Scope;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.pojo.Style;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;

/**
 * <p>
 * Draw one parent->child edge for each node in the graph.
 * </p>
 */
public class FlatEdgeHandler implements EdgeHandler {
    TreeHelper helper = new TreeHelper();

    @Override
    public List<Edge> getEdges(Node<MavenContext> node) {
        // Return a single edge running from parent to child
        List<Edge> edges = new ArrayList<Edge>(Collections.singletonList(getEdge(node)));
        node.getObject().setEdges(edges);
        return Collections.singletonList(getEdge(node));
    }

    protected Edge getEdge(Node<MavenContext> node) {
        GraphNode parent = node.getParent().getObject().getGraphNode();
        MavenContext context = node.getObject();
        GraphNode child = context.getGraphNode();
        boolean optional = context.isOptional();
        State state = context.getState();
        Scope scope = Scope.getScope(context.getArtifact().getScope());
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
