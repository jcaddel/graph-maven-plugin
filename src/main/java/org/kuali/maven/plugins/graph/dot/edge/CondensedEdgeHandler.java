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
package org.kuali.maven.plugins.graph.dot.edge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.kuali.maven.plugins.graph.dot.GraphException;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.Scope;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CondensedEdgeHandler extends FlatEdgeHandler {
    private static final Logger logger = LoggerFactory.getLogger(CondensedEdgeHandler.class);
    public static final String REPLACEMENT_LABEL = "replacement";
    TreeHelper helper = new TreeHelper();

    Map<String, MavenContext> conflictsMap = new HashMap<String, MavenContext>();

    @Override
    public List<Edge> getEdges(Node<MavenContext> node) {
        List<Edge> edges = new ArrayList<Edge>();
        handleNode(node, edges);
        return edges;
    }

    protected void handleNode(Node<MavenContext> node, List<Edge> edges) {
        logger.debug("handling node {}", node.getObject().getId());
        switch (node.getObject().getState()) {
        case INCLUDED:
        case CYCLIC:
        case UNKNOWN:
            // Just draw a line from parent to child
            // Styling draws attention to CYCLIC and UNKNOWN nodes
            edges.addAll(super.getEdges(node));
            return;
        case DUPLICATE:
            // Draw a line from our parent to the included node containing our same artifact
            handleDuplicate(node, edges);
            return;
        case CONFLICT:
            /**
             * Draw two lines. One from our parent to the node that has been conflicted out (might be us, might be
             * another node if the same artifact has been conflicted out twice). Draw a second line from that node to
             * the node containing the artifact Maven replaced it with.
             */
            handleConflict(node, edges);
            return;
        default:
            throw new GraphException("Unknown state " + node.getObject().getState());
        }
    }

    protected void handleConflict(Node<MavenContext> node, List<Edge> edges) {
        MavenContext context = node.getObject();

        // Find the node containing the replacement artifact Maven is actually going to use
        Node<MavenContext> replacement = null;// node.getObject().getReplacement();
        String artifactIdentifier = replacement.getObject().getArtifactIdentifier();

        // Check to see if we've encountered this same conflict before
        MavenContext contextToUse = conflictsMap.get(artifactIdentifier);
        if (contextToUse == null) {
            contextToUse = context;
            conflictsMap.put(artifactIdentifier, contextToUse);

            // Draw an edge from contextToUse to the replacement
            GraphNode parent = contextToUse.getGraphNode();
            GraphNode child = replacement.getObject().getGraphNode();
            Edge edge = getEdge(parent, child, false, Scope.DEFAULT_SCOPE, State.CONFLICT);
            edge.setLabel(REPLACEMENT_LABEL);
            edges.add(edge);
        } else {
            // Hide ourself since contextToUse represents the same artifact
            context.getGraphNode().setHidden(true);
        }

        // Draw an edge from our parent to contextToUse
        GraphNode parent = node.getParent().getObject().getGraphNode();
        GraphNode child = contextToUse.getGraphNode();
        Edge edge = getEdge(parent, child, false, Scope.DEFAULT_SCOPE, State.CONFLICT);

        edges.add(edge);

    }

    protected void handleDuplicate(Node<MavenContext> node, List<Edge> edges) {
        MavenContext context = node.getObject();
        DependencyNode dn = context.getDependencyNode();

        // Find the node that replaces us
        Node<MavenContext> replacement = findIncludedNode(node.getRoot(), context.getArtifactIdentifier());
        // This is our parent in the tree
        GraphNode parent = node.getParent().getObject().getGraphNode();
        // This is the node that is being used instead of us
        GraphNode child = replacement.getObject().getGraphNode();
        // Use our optional/scope settings
        boolean optional = dn.getArtifact().isOptional();
        Scope scope = Scope.getScope(dn.getArtifact().getScope());

        // Draw an edge from our parent to the node that replaced us
        Edge edge = getEdge(parent, child, optional, scope, State.INCLUDED);

        // Add this new edge to the list
        edges.add(edge);

        // Hide ourself
        context.getGraphNode().setHidden(true);
    }

    protected Node<MavenContext> findIncludedNode(Node<MavenContext> root, String artifactId) {
        List<Node<MavenContext>> nodes = root.getBreadthFirstList();
        for (Node<MavenContext> node : nodes) {
            MavenContext context = node.getObject();
            State state = context.getState();
            String artifactIdentifier = context.getArtifactIdentifier();
            if (state == State.INCLUDED && artifactIdentifier.equals(artifactId)) {
                return node;
            }
        }
        throw new GraphException("Inconsistent tree.  Can't locate " + artifactId);
    }
}
