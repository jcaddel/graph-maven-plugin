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
package org.kuali.maven.plugins.graph;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.kuali.maven.plugins.graph.dot.edge.CondensedEdgeHandler;
import org.kuali.maven.plugins.graph.dot.edge.EdgeHandler;
import org.kuali.maven.plugins.graph.pojo.Edge;
import org.kuali.maven.plugins.graph.pojo.GraphException;
import org.kuali.maven.plugins.graph.pojo.GraphNode;
import org.kuali.maven.plugins.graph.pojo.MavenContext;
import org.kuali.maven.plugins.graph.pojo.State;
import org.kuali.maven.plugins.graph.tree.Node;
import org.kuali.maven.plugins.graph.tree.TreeHelper;

/**
 * <p>
 * This mojo displays any dependencies where conflict resolution has taken place.
 * </p>
 *
 * <p>
 * Maven supports the resolution of artifact versions by way of nearest-wins. That is, for any set of dependencies
 * sharing the same [groupId]:[artifactId]:[type]:[classifier], the one declared nearest to the current project in the
 * dependency tree is selected for use.
 * </p>
 *
 * @goal conflicts
 * @requiresDependencyResolution compile|test|runtime
 */
public class ConflictsMojo extends BaseMojo {

    /**
     * The file the graph will be written to
     *
     * @parameter expression="${graph.file}" default-value="${project.build.directory}/graph/conflicts.png"
     */
    private File file;

    /**
     * This filter restricts the display to conflicts only.
     *
     * @parameter expression="${graph.conflictsFilter}" default-value="::conflict"
     */
    private String conflictsFilter;

    @Override
    protected EdgeHandler getEdgeHandler() {
        return new CondensedEdgeHandler();
    }

    @Override
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public void execute() {
        setShow(getShow() == null ? conflictsFilter : getShow() + "," + conflictsFilter);
        super.execute();
    }

    @Override
    protected void postProcess(Node<MavenContext> node, List<GraphNode> nodes, List<Edge> edges) {
        super.postProcess(node, nodes, edges);
        TreeHelper helper = new TreeHelper();
        List<MavenContext> contexts = getContexts(node);
        List<Edge> conflictEdges = getEdges(contexts, edges);
        for (Edge conflictEdge : conflictEdges) {
            conflictEdge.getChild().setHidden(false);
        }
        List<Node<MavenContext>> treeNodes = getNodes(node, conflictEdges);
        for (Node<MavenContext> treeNode : treeNodes) {
            helper.showPath(treeNode);
        }
    }

    protected List<Node<MavenContext>> getNodes(Node<MavenContext> node, List<Edge> edges) {
        List<Node<MavenContext>> nodes = new ArrayList<Node<MavenContext>>();
        for (Edge edge : edges) {
            nodes.add(getNode(node, edge));
        }
        return nodes;
    }

    protected Node<MavenContext> getNode(Node<MavenContext> node, Edge edge) {
        List<Node<MavenContext>> nodes = node.getRoot().getBreadthFirstList();
        for (Node<MavenContext> elementNode : nodes) {
            GraphNode graphNode = elementNode.getObject().getGraphNode();
            int id = graphNode.getId();
            if (id == edge.getChild().getId()) {
                return elementNode;
            }
        }
        throw new GraphException("Inconsistent tree state.  Unable to locate node " + edge.getChild().getId());
    }

    protected List<Edge> getEdges(List<MavenContext> contexts, List<Edge> edges) {
        List<Edge> newEdges = new ArrayList<Edge>();
        for (MavenContext context : contexts) {
            newEdges.add(getEdge(context, edges));
        }
        return newEdges;
    }

    protected Edge getEdge(MavenContext context, List<Edge> edges) {
        for (Edge edge : edges) {
            GraphNode parent = edge.getParent();
            int parentId = parent.getId();
            if (parentId == context.getGraphNode().getId()) {
                return edge;
            }
        }
        throw new GraphException("Inconsistent tree state.  Unable to locate an edge for "
                + context.getArtifactIdentifier());
    }

    protected List<MavenContext> getContexts(Node<MavenContext> node) {
        List<Node<MavenContext>> nodeList = node.getBreadthFirstList();
        List<MavenContext> contexts = new ArrayList<MavenContext>();
        for (Node<MavenContext> nodeElement : nodeList) {
            MavenContext context = nodeElement.getObject();
            State state = context.getState();
            boolean hidden = context.getGraphNode().isHidden();
            if (!hidden && state == State.CONFLICT) {
                contexts.add(context);
            }
        }
        return contexts;
    }

    public String getConflictsFilter() {
        return conflictsFilter;
    }

    public void setConflictsFilter(String conflictsFilter) {
        this.conflictsFilter = conflictsFilter;
    }

}