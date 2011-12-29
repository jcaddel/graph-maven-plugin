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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphException;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.Scope;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CondensedEdgeHandlerOld extends FlatEdgeHandler {
    private static final Logger logger = LoggerFactory.getLogger(CondensedEdgeHandlerOld.class);
    public static final String REPLACEMENT_LABEL = "replacement";
    TreeHelper helper = new TreeHelper();

    Map<String, MavenContext> conflictsMap = new HashMap<String, MavenContext>();

    @Override
    public List<Edge> getEdges(Node<MavenContext> node) {
        List<Edge> edges = new ArrayList<Edge>();
        // Draw a line from parent to child
        handleNode(node, edges);
        return edges;
    }

    protected void handleNode(Node<MavenContext> node, List<Edge> edges) {
        logger.debug("handling node {}", node.getObject().getId());
        switch (node.getObject().getState()) {
        case INCLUDED:
        case CYCLIC:
        case UNKNOWN:
            edges.addAll(super.getEdges(node));
            // Nothing further to do. Styling draws attention to CYCLIC and UNKNOWN nodes
            return;
        case DUPLICATE:
            edges.addAll(super.getEdges(node));
            // Draw a line from our parent to the included node that contains the same artifact
            handleDuplicate(node, edges);
            return;
        case CONFLICT:
            // Always draw one line. Possibly two.
            // The first line connects our parent to the node that has been conflicted out
            // In the case where 2 nodes share the same conflict, this first line may be drawn
            // from our parent to a different node in the tree.

            // The 2nd line is drawn from the conflicted out node to it's replacement. This 2nd line is only
            // drawn the first time a conflict is encountered. The 2nd time that same conflict is encountered
            // the line connecting the conflicted out node, to its replacement, is already drawn. No need to draw it
            // again.
            handleConflict(node, edges);
            return;
        default:
            throw new IllegalStateException("Unknown state " + node.getObject().getState());
        }
    }

    protected void handleConflict(Node<MavenContext> node, List<Edge> edges) {
        MavenContext context = node.getObject();

        // This is the id of the artifact Maven is actually going to use
        // The validator's and sanitizer's guarantee that the replacement artifact will always be
        // found among the included nodes.
        String replacementArtifactId = TreeHelper.getArtifactId(context.getReplacement());

        // Get the node containing the replacement artifact
        Node<MavenContext> replacement = findIncludedNode(node.getRoot(), replacementArtifactId);

        // Check to see if this is the first time this conflict has occurred
        boolean newConflict = true; // conflictsMap.get(replacementArtifactId) == null;

        // If so, we need to draw a line from the conflict node to its replacement
        if (newConflict) {

            // Extract the correct parent/child graph nodes
            GraphNode parent = context.getGraphNode();
            GraphNode child = replacement.getObject().getGraphNode();

            // Draw an edge saying "replacement" from ourself to the artifact that replaced us
            Edge edge = getEdge(parent, child, false, Scope.DEFAULT_SCOPE, State.CONFLICT);
            edge.setLabel(REPLACEMENT_LABEL);

            // Add the edge to the overall list
            edges.add(edge);

            // Add the edge to our list
            if (context.getEdges() == null) {
                context.setEdges(new ArrayList<Edge>());
            }
            context.getEdges().add(edge);

            // Store the conflict in case it happens again
            conflictsMap.put(replacementArtifactId, context);
        }

        // Extract the conflictNode from the map
        MavenContext conflictNode = conflictsMap.get(replacementArtifactId);

        // Determine if we are the conflict node
        boolean conflictNodeIsUs = context.getId() == conflictNode.getId();

        // If so draw a line from our parent to ourself
        if (conflictNodeIsUs) {
            edges.addAll(super.getEdges(node));
        } else {
            // if not, draw a line from our parent to the conflict node

            // Extract the correct parent/child graph nodes
            GraphNode parent = node.getParent().getObject().getGraphNode();
            GraphNode child = conflictNode.getGraphNode();

            // Use our scope and optional settings
            boolean optional = context.isOptional();
            Scope scope = Scope.getScope(context.getArtifact().getScope());

            // Draw an edge from our parent to the conflictNode
            Edge edge = getEdge(parent, child, optional, scope, State.CONFLICT);

            // Add the edge to the parent of the conflict node
            List<Edge> parentEdges = node.getParent().getObject().getEdges();
            if (parentEdges == null) {
                parentEdges = new ArrayList<Edge>();
                node.getParent().getObject().setEdges(parentEdges);
            }
            parentEdges.add(edge);

            node.getObject().getGraphNode().setHidden(true);

            // Add it to the list
            edges.add(edge);
        }
    }

    protected void handleDuplicate(Node<MavenContext> node, List<Edge> edges) {
        MavenContext context = node.getObject();

        // Find the node that replaces us
        Node<MavenContext> replacement = findIncludedNode(node.getRoot(), context.getArtifactIdentifier());
        // This is our parent in the tree
        GraphNode parent = node.getParent().getObject().getGraphNode();
        // This is the node that is being used instead of us
        GraphNode child = replacement.getObject().getGraphNode();
        // Use our optional/scope settings
        boolean optional = context.isOptional();
        Scope scope = Scope.getScope(context.getArtifact().getScope());

        // Draw an edge from our parent to the node that replaced us
        Edge edge = getEdge(parent, child, optional, scope, State.INCLUDED);

        // Add this new edge to the list
        edges.add(edge);

        // Hide ourself
        // context.getGraphNode().setHidden(true);
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
